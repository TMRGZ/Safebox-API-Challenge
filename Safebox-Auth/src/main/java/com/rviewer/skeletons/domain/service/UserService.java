package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;

public interface UserService {

    SafeboxUser createUser(String username, String password);

    void loginUser(String username, String password);

    String generateUserToken(String userId);

}
