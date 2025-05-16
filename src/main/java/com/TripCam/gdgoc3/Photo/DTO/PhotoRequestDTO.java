package com.TripCam.gdgoc3.Photo.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PhotoRequestDTO {
    private MultipartFile image;
}
