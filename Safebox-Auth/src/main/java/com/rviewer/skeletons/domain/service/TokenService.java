package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.user.User;

public interface TokenService {

    String generateToken(User user);

}
