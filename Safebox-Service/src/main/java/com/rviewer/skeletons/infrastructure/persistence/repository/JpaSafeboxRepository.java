package com.rviewer.skeletons.infrastructure.persistence.repository;

import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaSafeboxRepository extends JpaRepository<SafeboxDao, Long> {

    Optional<SafeboxDao> findByOwner(String owner);

}
