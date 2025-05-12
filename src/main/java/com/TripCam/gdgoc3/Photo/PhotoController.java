package com.TripCam.gdgoc3.Photo;

import com.TripCam.gdgoc3.Photo.DTO.PhotoRequestDTO;
import com.TripCam.gdgoc3.Photo.DTO.PhotoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
@Tag(name = "/api/photo")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "/api/photo/analyze")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = PhotoResponseDTO.class)))
    public ResponseEntity<PhotoResponseDTO> createAnalyze(@ModelAttribute PhotoRequestDTO requestDTO) throws IOException {

        MultipartFile image = requestDTO.getImage();
        try {

            PhotoResponseDTO result = photoService.createAnalyze(image.getInputStream());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}


