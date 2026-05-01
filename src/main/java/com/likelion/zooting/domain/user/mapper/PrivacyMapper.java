package com.likelion.zooting.domain.user.mapper;

import com.likelion.zooting.domain.user.dto.PrivacyRequest;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PrivacyMapper {

    public void updatePrivacyConsent(User user, PrivacyRequest request) {
        user.updatePrivacyConsent(request.privacyConsent());
    }
}
