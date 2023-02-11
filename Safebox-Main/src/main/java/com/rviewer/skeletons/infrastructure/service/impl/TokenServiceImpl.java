package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.service.TokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String retrieveCurrenUserToken() {
        return SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
    }
}
