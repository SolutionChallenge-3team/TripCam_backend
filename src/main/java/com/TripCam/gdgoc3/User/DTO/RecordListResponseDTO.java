package com.TripCam.gdgoc3.User.DTO;

import com.TripCam.gdgoc3.User.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordListResponseDTO {
    private User user;
    private Long photoId;
    private byte[] image;
    private String locationName;
    private LocalDateTime date;
}
