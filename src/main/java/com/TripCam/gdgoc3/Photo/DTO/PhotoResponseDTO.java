package com.TripCam.gdgoc3.Photo.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class PhotoResponseDTO {
    private Long photoId;
    private MultipartFile imageFile;
    private String spaceNameDescription;
    private String spaceStoryDescription;

}
