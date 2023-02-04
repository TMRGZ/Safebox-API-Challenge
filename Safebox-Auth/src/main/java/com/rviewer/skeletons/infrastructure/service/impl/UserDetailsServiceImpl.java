package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.SafeboxAuthException;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SafeboxUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SafeboxUser user = userRepository.findByUsername(username).orElseThrow();
        SafeboxUserHistory lastHistory = user.getSafeboxUserHistory().stream()
                .findFirst().orElseThrow(SafeboxAuthException::new);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .accountLocked(lastHistory.getLocked())
                .build();
    }
}
