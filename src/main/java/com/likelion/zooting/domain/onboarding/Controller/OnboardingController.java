package com.likelion.zooting.domain.onboarding.Controller;

import com.likelion.zooting.domain.onboarding.dto.OnboardingRequest;
import com.likelion.zooting.domain.onboarding.dto.OnboardingResponse;
import com.likelion.zooting.domain.onboarding.service.OnboardingService;
import com.likelion.zooting.global.exception.GeneralException;
import com.likelion.zooting.global.exception.code.GlobalErrorCode;
import com.likelion.zooting.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

  private final OnboardingService onboardingService;

  @PostMapping("/submit")
  public ResponseEntity<ApiResponse<OnboardingResponse>> submit(
      Authentication authentication,
      @Valid @RequestBody OnboardingRequest request) {

    if (authentication == null) {
      throw new GeneralException(GlobalErrorCode.UNAUTHORIZED);
    }

    Long userId = Long.valueOf(authentication.getName());

    // 서비스 로직 수행 (DB 저장) & 성공 응답 객체 생성
    OnboardingResponse responseData = new OnboardingResponse(onboardingService.submitOnboarding(userId, request));
    return ResponseEntity.ok(ApiResponse.success(responseData));
  }
}
