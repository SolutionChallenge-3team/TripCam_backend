package com.TripCam.gdgoc3.User;

import com.TripCam.gdgoc3.Photo.Photo;
import com.TripCam.gdgoc3.Photo.PhotoRepository;
import com.TripCam.gdgoc3.Photo.Recommendation.Recommendation;
import com.TripCam.gdgoc3.Photo.Recommendation.RecommendationRepository;
import com.TripCam.gdgoc3.Photo.Recommendation.RecommendationSpaceDTO;
import com.TripCam.gdgoc3.User.DTO.RecordDetailsResponseDTO;
import com.TripCam.gdgoc3.User.DTO.RecordListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final RecommendationRepository recommendationRepository;

    public List<RecordListResponseDTO> getPhotos(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Photo> photos = photoRepository.findByUser(user);
        List<RecordListResponseDTO> list = new ArrayList<>();

        for(Photo photo : photos) {

            String imageByte = photo.getBase64image();
            byte[] imageBytes = Base64.getDecoder().decode(imageByte);


            RecordListResponseDTO listResponseDTO = new RecordListResponseDTO();
            listResponseDTO.setUser(user);
            listResponseDTO.setImage(imageBytes);
            listResponseDTO.setDate(photo.getCreatedAt());
            listResponseDTO.setLocationName(photo.getLocationName());
            listResponseDTO.setPhotoId(photo.getPhotoId());

            list.add(listResponseDTO);
        }

        return list;
    }


    public RecordDetailsResponseDTO getPhotoDetails(Long userId, Long photoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Photo> photos = photoRepository.findByUser(user);
        Photo thePhoto = null;
        for (Photo photo : photos) {
            if (photo.getPhotoId().equals(photoId)) {
                thePhoto = photo;
                break;
            }
        }

        String imageByte = thePhoto.getBase64image();
        byte[] imageBytes = Base64.getDecoder().decode(imageByte);

        RecordDetailsResponseDTO recordDetailsResponseDTO = new RecordDetailsResponseDTO();
        recordDetailsResponseDTO.setPhotoId(photoId);
        recordDetailsResponseDTO.setBase64image(imageBytes);
        recordDetailsResponseDTO.setLocationName(thePhoto.getLocationName());
        recordDetailsResponseDTO.setDescription(thePhoto.getDescription());
        recordDetailsResponseDTO.setStory(thePhoto.getStory());
        recordDetailsResponseDTO.setCreatedAt(thePhoto.getCreatedAt());

        // 추천장소 추천 받았는지
        if (thePhoto.isRecommended()) {
            // 추천 받았으면

            List<RecommendationSpaceDTO> list = new ArrayList<>();
            List<Recommendation> recommendations = recommendationRepository.findRecommendationByUser(user);
            for (Recommendation recommendation : recommendations) {
                RecommendationSpaceDTO recommendationSpaceDTO = new RecommendationSpaceDTO();
                recommendationSpaceDTO.setRecommendedName(recommendation.getRecommendationName());
                recommendationSpaceDTO.setRecommendedDescription(recommendation.getRecommendationDescription());
                recommendationSpaceDTO.setLatitude(recommendation.getLatitude());
                recommendationSpaceDTO.setLongitude(recommendation.getLongitude());

                list.add(recommendationSpaceDTO);

            }
            recordDetailsResponseDTO.setRecommendationSpaces(list);


            //User로 된 recommendation 찾아서 List에 저장



        } else {
            recordDetailsResponseDTO.setRecommendationSpaces(null);
        }


        return recordDetailsResponseDTO;
    }
}
