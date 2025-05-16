package com.TripCam.gdgoc3.Auth;

import com.TripCam.gdgoc3.Auth.jwt.JwtTokenProvider;
import com.TripCam.gdgoc3.User.User;
import com.TripCam.gdgoc3.User.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.time.LocalDateTime;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/firebase")
    public ResponseEntity<?> firebaseLogin(@RequestBody String idToken) {
        System.out.println("🔥 받은 토큰: " + idToken); // 👈 로그 찍히는지 확인
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();

            // 사용자 확인 또는 등록
            Optional<User> userOpt = userRepository.findByUid(uid);
            User user = userOpt.orElseGet(() -> userRepository.save(
                    User.builder()
                            .uid(uid)
                            .email(email)
                            .realname(name != null ? name : "익명")
                            .nickname(name != null ? name : "사용자")  // <-- 이 줄 필수
                            .createdAt(LocalDateTime.now())           // ✅ 여기 추가
                            .updatedAt(LocalDateTime.now())           // ✅ 여기도 보통 같이 넣음
                            .build()
            ));

            // ✅ JWT 토큰 발급
            String jwt = jwtTokenProvider.generateToken(uid, email);

            return ResponseEntity.ok().body(jwt);

        } catch (FirebaseAuthException e) {
            e.printStackTrace(); // Firebase 관련 예외 로그 출력
            return ResponseEntity.badRequest().body("Firebase ID 토큰이 유효하지 않습니다");
        } catch (Exception e) {
            e.printStackTrace(); // 그 외 모든 예외도 로그 출력
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }

    }
}
