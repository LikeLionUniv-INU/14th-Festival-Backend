package com.likelion.zooting.domain.user.service;

import com.likelion.zooting.domain.user.dto.PrivacyRequest;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.exception.UserErrorCode;
import com.likelion.zooting.domain.user.mapper.PrivacyMapper;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.global.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivacyService {

    private final UserRepository userRepository;
    private final PrivacyMapper privacyMapper;

    public void submitPrivacyConsent(String instagramId, PrivacyRequest request) {
        User user = userRepository.findByInstagramId(instagramId)
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        if (Boolean.FALSE.equals(request.privacyConsent())) {
            throw new GeneralException(UserErrorCode.PRIVACY_CONSENT_DENIED);
        }

        privacyMapper.updatePrivacyConsent(user, request);
    }
}
