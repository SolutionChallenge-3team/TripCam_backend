package com.TripCam.gdgoc3.Photo.Recommendation;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationSpaceDTO {
    private Long recommendationId;
    private String recommendedName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String recommendedDescription;
}
