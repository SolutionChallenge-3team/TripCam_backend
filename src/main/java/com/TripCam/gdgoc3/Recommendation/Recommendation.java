package com.TripCam.gdgoc3.Recommendation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id", nullable = false)
    private Long recommendationId;

    @Column(name = "photo_id2", nullable = false)
    private Long photoId2;

    @Column(name = "user_id2", nullable = false)
    private Long userId2;

    @Column(name = "recommendation_name", nullable = false, length = 255)
    private String recommendationName;

    @Column(name = "recommendation_description", columnDefinition = "TEXT")
    private String recommendationDescription;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private Double latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private Double longitude;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
}
