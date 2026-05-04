package com.likelion.zooting.domain.match.controller;

import com.likelion.zooting.domain.match.dto.MatchResultRequest;
import com.likelion.zooting.domain.match.dto.MatchResultResponse;
import com.likelion.zooting.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Match", description = "매칭 관련 API")
public interface MatchResultControllerDocs {

  @Operation(
      summary = "매칭 결과 확인 API",
      description = """
                    사용자가 입력한 인스타 ID와 본인확인용 4자리 숫자를 검증한 후,
                    18시 이후 매칭 결과를 조회합니다.
                    
                    - Request Body로 instagramId, verificationPin을 전달합니다.
                    - 18시 이전에는 매칭 결과를 조회할 수 없습니다.
                    - 매칭 성공 시 상대방 인스타 ID를 반환합니다.
                    - 매칭된 사용자가 없을 경우 isMatched=false를 반환합니다.
                    """
  )
  ResponseEntity<ApiResponse<MatchResultResponse>> getMatchResult(
      @Valid @RequestBody MatchResultRequest request
  );
}