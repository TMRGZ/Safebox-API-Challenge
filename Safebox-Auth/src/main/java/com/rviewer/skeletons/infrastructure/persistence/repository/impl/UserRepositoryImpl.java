package com.rviewer.skeletons.infrastructure.persistence.repository.impl;

import com.rviewer.skeletons.domain.model.user.User;
import com.rviewer.skeletons.domain.repository.UserRepository;
import com.rviewer.skeletons.infrastructure.mapper.dao.UserDaoMapper;
import com.rviewer.skeletons.infrastructure.mapper.domain.UserMapper;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private UserDaoMapper userDaoMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id).map(userDao -> userDaoMapper.map(userDao));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userDao -> userDaoMapper.map(userDao));
    }

    @Override
    public User save(User user) {
        return userDaoMapper.map(userRepository.save(userMapper.map(user)));
    }
}
