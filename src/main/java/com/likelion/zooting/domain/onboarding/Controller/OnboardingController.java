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

  @PostMapping("/submit")
  public ResponseEntity<ApiResponse<OnboardingResponse>> submit(
      @RequestAttribute Long userId,
      @Valid @RequestBody OnboardingRequest request) {

    // 서비스 로직 수행 (DB 저장) & 성공 응답 객체 생성
    OnboardingResponse responseData = new OnboardingResponse(onboardingService.submitOnboarding(userId, request));
    return ResponseEntity.ok(ApiResponse.success(responseData));
  }
}
