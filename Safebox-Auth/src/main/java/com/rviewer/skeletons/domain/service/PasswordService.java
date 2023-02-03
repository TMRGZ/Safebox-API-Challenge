package com.rviewer.skeletons.domain.service;

public interface PasswordService {

    String encodePassword(String plainPassword);

    boolean checkPassword(String encodedPassword, String plainPassword);

}
