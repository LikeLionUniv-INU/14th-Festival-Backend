package com.likelion.zooting.domain.profile.controller.docs;

import com.likelion.zooting.domain.profile.dto.ProfileResponse;
import com.likelion.zooting.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Profile", description = "프로필 관련 API")
public interface ProfileControllerDocs {

  @Operation(
      summary = "생성된 프로필 조회 API",
      description = """
                    사용자의 생성된 프로필 정보를 조회합니다.
                    
                    - Authorization 헤더에 Bearer 토큰이 필요합니다.
                    - JWT 토큰에서 사용자 ID를 추출하여 프로필 정보를 조회합니다.
                    - 생성된 프로필 정보가 없을 경우 404 에러를 반환합니다.
                    """,
      security = @SecurityRequirement(name = "bearerAuth")
  )
  ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
      @AuthenticationPrincipal Long userId
  );
}