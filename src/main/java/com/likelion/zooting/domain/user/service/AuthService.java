package com.likelion.zooting.domain.user.service;

import com.likelion.zooting.domain.user.dto.AuthRequest;
import com.likelion.zooting.domain.user.dto.AuthResponse;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.exception.UserErrorCode;
import com.likelion.zooting.domain.user.mapper.AuthMapper;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.global.exception.GeneralException;
import com.likelion.zooting.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMapper authMapper;

    public AuthResponse authenticate(AuthRequest request) {
        return userRepository.findByInstagramId(request.instagramId())
                .map(user -> loginExistingUser(user, request.verificationPin()))
                .orElseGet(() -> registerAndLogin(request));
    }

    private AuthResponse loginExistingUser(User user, String rawPin) {
        if (!user.getUserPw().equals(rawPin)) {
            throw new GeneralException(UserErrorCode.PIN_MISMATCH);
        }

        if (isSurveyCompleted(user.getStatus())) {
            throw new GeneralException(UserErrorCode.ALREADY_COMPLETED);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getInstagramId());
        return authMapper.toAuthResponse(user, accessToken);
    }

    private AuthResponse registerAndLogin(AuthRequest request) {
        User newUser = authMapper.toEntity(request);
        User savedUser = userRepository.save(newUser);

        String accessToken = jwtTokenProvider.createAccessToken(savedUser.getInstagramId());
        return authMapper.toAuthResponse(savedUser, accessToken);
    }

    private boolean isSurveyCompleted(Status status) {
        return status == Status.SUBMITTED
                || status == Status.MATCHED
                || status == Status.FAILED;
    }
}