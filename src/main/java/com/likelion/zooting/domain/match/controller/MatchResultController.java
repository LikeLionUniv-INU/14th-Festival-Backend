package com.likelion.zooting.domain.match.controller;

import com.likelion.zooting.domain.match.dto.MatchResultRequest;
import com.likelion.zooting.domain.match.dto.MatchResultResponse;
import com.likelion.zooting.domain.match.service.MatchResultService;
import com.likelion.zooting.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchResultController {

  private final MatchResultService matchResultService;

  @PostMapping("/result")
  public ResponseEntity<ApiResponse<MatchResultResponse>> getMatchResult(
      @Valid @RequestBody MatchResultRequest request
  ) {
    MatchResultResponse response = matchResultService.getMatchResult(request);

    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
