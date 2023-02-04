package com.rviewer.skeletons.domain.service;

public interface TokenService {

    String generateToken(String userId);

    boolean validateToken(String token);

}
