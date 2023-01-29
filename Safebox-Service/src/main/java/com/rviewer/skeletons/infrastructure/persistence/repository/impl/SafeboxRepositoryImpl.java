package com.rviewer.skeletons.infrastructure.persistence.repository.impl;

import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.repository.SafeboxRepository;
import com.rviewer.skeletons.infrastructure.mapper.dao.SafeboxDaoMapper;
import com.rviewer.skeletons.infrastructure.mapper.domain.SafeboxMapper;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaSafeboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SafeboxRepositoryImpl implements SafeboxRepository {

    @Autowired
    private JpaSafeboxRepository safeboxRepository;

    @Autowired
    private SafeboxMapper safeboxMapper;

    @Autowired
    private SafeboxDaoMapper safeboxDaoMapper;

    @Override
    public Optional<Safebox> findById(String id) {
        return safeboxRepository.findById(UUID.fromString(id)).map(safeboxDao -> safeboxDaoMapper.map(safeboxDao));
    }

    @Override
    public Optional<Safebox> findByOwner(String owner) {
        return safeboxRepository.findByOwner(owner).map(safeboxDao -> safeboxDaoMapper.map(safeboxDao));
    }

    @Override
    public Safebox save(Safebox safebox) {
        return safeboxDaoMapper.map(safeboxRepository.save(safeboxMapper.map(safebox)));
    }
}
