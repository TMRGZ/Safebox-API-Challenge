package com.rviewer.skeletons.domain.repository;

import com.rviewer.skeletons.domain.model.Safebox;

import java.util.Optional;

public interface SafeboxRepository {

    Optional<Safebox> findById(Long id);

    Optional<Safebox> findByOwner(String owner);

    Safebox save(Safebox safebox);

}
