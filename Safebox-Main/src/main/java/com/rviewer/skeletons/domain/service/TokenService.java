package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.User;

public interface TokenService {

    User decodeToken(String token);

    String retrieveCurrenUserToken();
}
