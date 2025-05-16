package com.TripCam.gdgoc3.user.DTO;

import com.TripCam.gdgoc3.Photo.Recommendation.RecommendationSpaceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDetailsResponseDTO {
    private Long photoId;
    private byte[] base64image;
    private String locationName;
    private String description;
    private String story;
    private LocalDateTime createdAt;
    private boolean isRecommended;
    private List<RecommendationSpaceDTO> recommendationSpaces;
}
