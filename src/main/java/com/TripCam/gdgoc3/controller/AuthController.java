package com.TripCam.gdgoc3.controller;

import com.TripCam.gdgoc3.jwt.JwtTokenProvider;
import com.TripCam.gdgoc3.user.User;
import com.TripCam.gdgoc3.user.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/firebase")
    public ResponseEntity<?> firebaseLogin(@RequestBody String idToken) {
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
                            .build()
            ));

            // ✅ JWT 토큰 발급
            String jwt = jwtTokenProvider.generateToken(uid, email);

            return ResponseEntity.ok().body(jwt);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Firebase ID 토큰이 유효하지 않습니다");
        }
    }
}
