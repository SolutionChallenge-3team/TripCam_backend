package com.TripCam.gdgoc3.Photo;


import com.TripCam.gdgoc3.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image", nullable = false, columnDefinition = "LONGTEXT")
    private String base64image;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "analyzed_story", columnDefinition = "TEXT")
    private String story;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_recommended")
    private boolean isRecommended = false;

}
