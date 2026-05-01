package com.likelion.zooting.domain.onboarding.Controller;

import com.likelion.zooting.domain.onboarding.dto.OnboardingRequest;
import com.likelion.zooting.domain.onboarding.dto.OnboardingResponse;
import com.likelion.zooting.domain.onboarding.service.OnboardingService;
import com.likelion.zooting.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

  private final OnboardingService onboardingService;

  @PostMapping
  public ResponseEntity<ApiResponse<OnboardingResponse>> submit(
      @RequestAttribute Long userId,
      @Valid @RequestBody OnboardingRequest request) {

    // 1. 서비스 로직 수행 (DB 저장)
    onboardingService.submitOnboarding(userId, request);

    // 2. 성공 응답 객체 생성
    OnboardingResponse responseData = new OnboardingResponse(true);

    // 3. 작성하신 ApiResponse.success()를 사용하여 최종 반환
    // 결과 JSON:
    // {
    //   "isSuccess": true,
    //   "code": "COMMON_200",
    //   "message": "요청에 성공했습니다.",
    //   "result": { "profileCreated": true }
    // }
    return ResponseEntity.ok(ApiResponse.success(responseData));
  }
}
