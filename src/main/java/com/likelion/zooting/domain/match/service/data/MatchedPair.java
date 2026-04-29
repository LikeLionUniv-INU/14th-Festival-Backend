package com.likelion.zooting.domain.match.service.data;

import com.likelion.zooting.domain.user.entity.User;

public record MatchedPair(
        User maleUser,
        User femaleUser,
        Integer score
) {
}
