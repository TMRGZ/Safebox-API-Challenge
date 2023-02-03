package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.user.User;

public interface UserService {

    User createUser(String username, String password);

    String loginUser(String username, String password);

}
