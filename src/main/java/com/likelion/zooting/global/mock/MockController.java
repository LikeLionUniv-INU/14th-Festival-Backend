package com.likelion.zooting.global.mock;

import com.likelion.zooting.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 프론트 테스트용 Mock API
 * 실제 로직 구현 시 삭제 또는 각 도메인으로 이동 필요
 */
@RestController
@RequestMapping("/api")
public class MockController {

//    @PostMapping("/onboarding/instagram")
//    public ApiResponse<Void> verifyInstagram() {
//        return ApiResponse.success();
//    }

//    @PostMapping("/onboarding/privacy")
//    public ApiResponse<Void> submitPrivacy() {
//        return ApiResponse.success();
//    }

    @PostMapping("/onboarding/submit")
    public ApiResponse<Void> submitOnboarding() {
        return ApiResponse.success();
    }

    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> getProfile() {
        return ApiResponse.success(Map.of(
                "profileTag", "감성적인 토끼",
                "animalType", "RABBIT",
                "releaseTime", "18:00"
        ));
    }

    @PostMapping("/match/result")
    public ApiResponse<Map<String, Object>> getMatchResult() {
        return ApiResponse.success(Map.of(
                "partnerName", "홍길동",
                "score", 95
        ));
    }
}
