package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.service.PasswordService;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    @Override
    public String encodePassword(String plainPassword) {
        return null;
    }

    @Override
    public boolean checkPassword(String encodedPassword, String plainPassword) {
        return false;
    }
}
