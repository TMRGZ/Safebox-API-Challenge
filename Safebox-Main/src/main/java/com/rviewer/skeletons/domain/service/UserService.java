package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.User;

public interface UserService {

    String createUser(String username, String password);

    User getUser(String token);
}
