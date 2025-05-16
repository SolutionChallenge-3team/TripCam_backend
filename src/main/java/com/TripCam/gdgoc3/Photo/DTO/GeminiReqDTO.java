package com.TripCam.gdgoc3.Photo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiReqDTO {

    private List<Content> contents;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String role; // e.g. "user"
        private List<Part> parts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
        private InlineData inline_data;  // 이미지 전송용 필드 (선택적 사용)

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class InlineData {
            private String mime_type; // e.g., "image/jpeg"
            private String data;      // base64 encoded image
        }
    }
}
