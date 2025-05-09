package com.TripCam.gdgoc3.Photo;

import com.TripCam.gdgoc3.Photo.DTO.*;
import com.TripCam.gdgoc3.User.UserRepository;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    // private final GenerativeModel generativeModel;

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${GEMINI_API_URL}")
    private String apiUrl;

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    @Transactional
    public Map<String, String> createAnalyze(InputStream imageInputStream) throws IOException {

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
                .text("사진의 장소 이름을 알려주고, 장소에 대한 설명을 50자 이내로 설명한 후 역사/문화적 스토리에 대해 설명해줘. 응답 형식은 json으로 {\"locationName\": 장소 이름, \"description\": 장소에 대한 50자 이내의 설명, \"story\": 역사/문화적 스토리} 이렇게 출력해줘. JSON으로만 응답해줘. 설명하지 말고, 마크다운 포맷이나 백틱(```)도 붙이지 마.")
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


        Map<String, String> part = parseJsonText(analysisResult);


        // 3. DB에 저장
        Photo photo = new Photo();
        // photo.setUser(user);
        photo.setBase64image(base64Image);
        photo.setLocationName(part.get("locationName"));
        photo.setDescription(part.get("description"));
        photo.setStory(part.get("story"));
        photo.setCreatedAt(LocalDateTime.now());

        photoRepository.save(photo);
        System.out.println("photo = " + photo);


        return part;

    }

    private String encodeImageToBase64(InputStream imageInputStream) throws IOException {
        byte[] imageBytes = imageInputStream.readAllBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private Map<String, String> parseJsonText(String jsonText) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonText, new TypeReference<Map<String, String>>() {});
    }
}
