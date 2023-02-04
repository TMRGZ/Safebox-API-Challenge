package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.model.user.User;
import com.rviewer.skeletons.domain.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String generateToken(User user) {
        return null;
    }
}
