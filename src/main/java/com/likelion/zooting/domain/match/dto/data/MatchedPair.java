package com.likelion.zooting.domain.match.dto.data;

import com.likelion.zooting.domain.user.entity.User;

/**
 * 매칭 후 남성 사용자와 여성 사용자의 짝을 담은 dto
 *
 * @param maleUser
 * @param femaleUser
 */
public record MatchedPair(
        User maleUser,
        User femaleUser,
        Integer score
) {
}
