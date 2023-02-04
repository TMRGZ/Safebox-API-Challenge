package com.rviewer.skeletons.infrastructure.persistence.repository.impl;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.infrastructure.mapper.dao.UserDaoMapper;
import com.rviewer.skeletons.infrastructure.mapper.domain.UserMapper;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SafeboxUserRepositoryImpl implements SafeboxUserRepository {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private UserDaoMapper userDaoMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Optional<SafeboxUser> findById(String id) {
        return userRepository.findById(id).map(userDao -> userDaoMapper.map(userDao));
    }

    @Override
    public Optional<SafeboxUser> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userDao -> userDaoMapper.map(userDao));
    }

    @Override
    public SafeboxUser save(SafeboxUser safeboxUser) {
        return userDaoMapper.map(userRepository.save(userMapper.map(safeboxUser)));
    }
}
