package com.rviewer.skeletons.domain.service;

public interface TokenService {

    String generate();

    String decode(String token);

    String getDecodedUsername();

}
