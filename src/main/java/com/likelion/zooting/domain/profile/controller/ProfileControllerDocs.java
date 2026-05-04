package com.likelion.zooting.domain.profile.controller;

import com.likelion.zooting.domain.profile.dto.ProfileResponse;
import com.likelion.zooting.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;

@Tag(name = "Profile", description = "프로필 관련 API")
public interface ProfileControllerDocs {

  @Operation(
      summary = "내 프로필 정보 조회",
      description = "로그인한 사용자의 프로필 정보를 조회합니다. 사용자의 관심사 중 하나를 활용한 <b>프로필 태그(수식어 + 동물상)</b>와 매칭 결과 공개 시간을 반환합니다."
  )
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "프로필 조회 성공",
          content = @Content(schema = @Schema(implementation = ProfileResponse.class))
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "404",
          description = "사용자 또는 온보딩 정보를 찾을 수 없음 (USER_4041, USER_4042)",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "401",
          description = "유효하지 않은 토큰 혹은 사용자 정보 없음 (AUTH_4011)",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      )
  })
  ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
      @Parameter(hidden = true) @RequestAttribute Long userId
  );
}