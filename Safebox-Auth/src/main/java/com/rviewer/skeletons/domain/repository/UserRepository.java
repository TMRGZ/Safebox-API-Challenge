package com.rviewer.skeletons.domain.repository;

import com.rviewer.skeletons.domain.model.user.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    User save(User user);

}
