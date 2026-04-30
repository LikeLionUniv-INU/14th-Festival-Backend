package com.likelion.zooting.domain.user.mapper;

import com.likelion.zooting.domain.user.dto.AuthRequest;
import com.likelion.zooting.domain.user.dto.AuthResponse;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toEntity(AuthRequest request) {
        return new User(
                request.instagramId(),
                request.verificationPin()
        );
    }

    public AuthResponse toAuthResponse(User user, String accessToken) {
        return new AuthResponse(
                user.getUserId(),
                user.getInstagramId(),
                toIsComplete(user.getStatus()),
                user.isPrivacyConsent(),
                accessToken
        );
    }

    private boolean toIsComplete(Status status) {
        return status != Status.IN_PROGRESS;
    }
}
