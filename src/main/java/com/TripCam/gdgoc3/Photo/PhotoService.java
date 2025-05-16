package com.TripCam.gdgoc3.Photo;

import com.TripCam.gdgoc3.Photo.DTO.*;
import com.TripCam.gdgoc3.Photo.Recommendation.Recommendation;
import com.TripCam.gdgoc3.Photo.Recommendation.RecommendationRepository;
import com.TripCam.gdgoc3.Photo.Recommendation.RecommendationSpaceDTO;
import com.TripCam.gdgoc3.user.User;
import com.TripCam.gdgoc3.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${GEMINI_API_URL}")
    private String apiUrl;

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    @Transactional
    public PhotoResponseDTO createAnalyze(InputStream imageInputStream) throws IOException {

        // 주변장소 추천 기능 구현해야함

        String requestUrl = apiUrl + "?key=" + apiKey;

//        Long userId = 1L;
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 1. 파일 데이터 읽기
        String base64Image = encodeImageToBase64(imageInputStream);

        GeminiReqDTO.Part.InlineData inlineData = GeminiReqDTO.Part.InlineData.builder()
                .mime_type("image/jpeg")
                .data(base64Image)
                .build();

        GeminiReqDTO.Part imagePart = GeminiReqDTO.Part.builder()
                .inline_data(inlineData)
                .build();

        GeminiReqDTO.Part textPart = GeminiReqDTO.Part.builder()
                .text("사진의 장소 이름을 알려주고, 장소에 대한 설명을 50자 이내로 설명한 후 역사/문화적 스토리에 대해 설명해줘. 응답 형식은 json으로 {\"locationName\": 장소 이름, \"description\": 장소에 대한 100자 이내의 문장 형태의 설명, \"story\": 6문장 내외의 역사/문화적 스토리} 이렇게 출력해줘. JSON으로만 응답해줘. 설명하지 말고, 마크다운 포맷이나 백틱(```)도 붙이지 마.")
                .build();

        GeminiReqDTO.Content content = GeminiReqDTO.Content.builder()
                .role("user")
                .parts(List.of(textPart, imagePart))
                .build();

        GeminiReqDTO requestDTO = new GeminiReqDTO(List.of(content));

        System.out.println("List.of(content) = " + List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiReqDTO> request = new HttpEntity<>(requestDTO, headers);

        // API 요청
        ResponseEntity<GeminiResDTO> response = restTemplate.postForEntity(
                requestUrl,
                request,
                GeminiResDTO.class
        );

        String analysisResult = "";

        // 결과 파싱
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            GeminiResDTO res = response.getBody();
            analysisResult = res.getCandidates().get(0).getContent().getParts().get(0).getText();
        } else {
            throw new RuntimeException("Gemini Vision API 응답 오류: " + response.getStatusCode());
        }
        analysisResult = analysisResult.replaceAll("(?s)```json|```", "").trim();
        System.out.println("analysisResult = " + analysisResult);


        Map<String, String> photoResponse = parseJsonText(analysisResult);

        Long userId = 1L;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 3. DB에 저장
        Photo photo = new Photo();
        photo.setUser(user);
        photo.setBase64image(base64Image);
        photo.setLocationName(photoResponse.get("locationName"));
        photo.setDescription(photoResponse.get("description"));
        photo.setStory(photoResponse.get("story"));
        photo.setCreatedAt(LocalDateTime.now());

        photoRepository.save(photo);

        PhotoResponseDTO responseDTO = new PhotoResponseDTO();
        responseDTO.setPhotoId(photo.getPhotoId());
        responseDTO.setLocationName(photoResponse.get("locationName"));
        responseDTO.setDescription(photoResponse.get("description"));
        responseDTO.setStory(photoResponse.get("story"));


        return responseDTO;

    }

    // 장소 추천 기능
    @Transactional
    public List<RecommendationSpaceDTO> recommendNearby(Long photoId) throws IOException {
        // 1. 해당 photo id 조회
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found with id: \" + photoId"));

        photo.setRecommended(true);

        // 2. base64 이미지 가져오기
        String base64Image = photo.getBase64image();


        // 3. Gemini 요청 구성
        GeminiReqDTO.Part.InlineData inlineData = GeminiReqDTO.Part.InlineData.builder()
                .mime_type("image/jpeg")
                .data(base64Image)
                .build();

        GeminiReqDTO.Part imagePart = GeminiReqDTO.Part.builder()
                .inline_data(inlineData)
                .build();

        GeminiReqDTO.Part textPart = GeminiReqDTO.Part.builder()
                .text("이 사진이 찍힌 장소의 주변 로컬 체험/소규모 전통 가게 등의 관광지를 5곳 추천해줘. 이름과 해당 관광지의 위도,경도, 100자 이내의 간단한 설명을 함께 알려줘. 응답 형식은 List로, 그 안에 json으로 장소값이 들어 있어야 해. [{\"recommendedName\" : 주변 관광지 이름, \"latitude\" : 위도, \"longitude\" : 경도, \"recommendedDescription\" : 100자 이내의 간단한 설명}, { ... } , ... ] 형식으로 작성해줘.")
                .build();

        GeminiReqDTO.Content content = GeminiReqDTO.Content.builder()
                .role("user")
                .parts(List.of(textPart, imagePart))
                .build();

        GeminiReqDTO requestDTO = new GeminiReqDTO(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiReqDTO> request = new HttpEntity<>(requestDTO, headers);
        String requestUrl = apiUrl + "?key=" + apiKey;

        ResponseEntity<GeminiResDTO> response = restTemplate.postForEntity(
                requestUrl,
                request,
                GeminiResDTO.class
        );

        String recommendResult = "";

        // 4. 결과 파싱
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            recommendResult = response.getBody().getCandidates()
                    .get(0).getContent().getParts().get(0).getText();
        } else {
            throw new RuntimeException("Gemini Vision API 응답 오류: " + response.getStatusCode());
        }
        recommendResult = recommendResult.replaceAll("(?s)```json|```", "").trim();

        String recommendationString = extractBracketContent(recommendResult);

        System.out.println("recommendationString = " + recommendationString);


        List<RecommendationSpaceDTO> recommendations = parseRecommendationList(recommendationString);

        for (RecommendationSpaceDTO recommendation : recommendations) {


            System.out.println("recommendation = " + recommendation);


            Recommendation recomm = new Recommendation();

            Long userId = 1L;

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            recomm.setPhoto(photo);
            recomm.setUser(user);
            // recomm.setRecommendationId(recommendation.getRecommendationId());
            recomm.setRecommendationName(recommendation.getRecommendedName());
            recomm.setRecommendationDescription(recommendation.getRecommendedDescription());
            recomm.setLatitude(recommendation.getLatitude());
            recomm.setLongitude(recommendation.getLongitude());
            recomm.setCreatedAt(LocalDateTime.now());


            recommendationRepository.save(recomm);
        }



        return recommendations;
    }

    private String encodeImageToBase64(InputStream imageInputStream) throws IOException {
        byte[] imageBytes = imageInputStream.readAllBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private Map<String, String> parseJsonText(String jsonText) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonText, new TypeReference<Map<String, String>>() {});
    }

    private List<RecommendationSpaceDTO> parseRecommendationList(String recommendResult) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(recommendResult, new TypeReference<List<RecommendationSpaceDTO>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractBracketContent(String input) {
        System.out.println("input = " + input);
        int start = input.indexOf('[');
        int end = input.lastIndexOf(']');

        if (start != -1 && end != -1 && start < end) {
            return input.substring(start, end + 1);
        }

        // 유효한 범위가 없으면 원본 반환 또는 빈 문자열
        return input;
    }



}
