package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean checkPassword(String encodedPassword, String plainPassword) {
        return passwordEncoder.matches(plainPassword, encodedPassword);
    }
}
