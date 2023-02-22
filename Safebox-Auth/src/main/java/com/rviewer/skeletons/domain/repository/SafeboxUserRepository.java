package com.rviewer.skeletons.domain.repository;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;

import java.util.Optional;

public interface SafeboxUserRepository {

    Optional<SafeboxUser> findById(String id);

    Optional<SafeboxUser> findByName(String username);

    SafeboxUser save(SafeboxUser safeboxUser);

}
