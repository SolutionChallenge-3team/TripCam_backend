package com.TripCam.gdgoc3.Photo.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PhotoResponseDTO {
    private MultipartFile imageFile;
    private String spaceName;
    private String spaceDescription;
    private String spaceStory;
    private LocalDateTime createdAt;

}
