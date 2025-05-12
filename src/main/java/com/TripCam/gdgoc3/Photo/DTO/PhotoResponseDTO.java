package com.TripCam.gdgoc3.Photo.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class PhotoResponseDTO {
    private Long photoId;
    private String locationName;
    private String description;
    private String story;
}
