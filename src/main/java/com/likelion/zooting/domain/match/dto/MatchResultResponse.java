package com.likelion.zooting.domain.match.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MatchResultResponse(
    boolean isMatched,
    String partnerInstagramId
) {

  public static MatchResultResponse matched(String partnerInstagramId) {
    return new MatchResultResponse(true, partnerInstagramId);
  }

  public static MatchResultResponse notMatched() {
    return new MatchResultResponse(false, null);
  }
}