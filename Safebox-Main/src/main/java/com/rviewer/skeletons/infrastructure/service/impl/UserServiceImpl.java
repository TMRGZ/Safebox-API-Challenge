package com.rviewer.skeletons.infrastructure.service.impl;


import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.infrastructure.mapper.UserMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthCreateUserDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthRegisteredUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserApi userApi;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String createUser(String username, String password) {
        User user;

        try {
            log.info("Attempting to create user {}", username);
            AuthRegisteredUserDto registeredUserDto = userApi.postUser(new AuthCreateUserDto().name(username).password(password));
            user = userMapper.map(registeredUserDto);

        } catch (HttpClientErrorException e) {
            log.error("Client error {} while attempting to create user {}", e.getStatusCode(), username);

            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new SafeboxAlreadyExistsException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            log.error("Server error {} while attempting to create user {}", e.getStatusCode(), username);

            throw new ExternalServiceException();
        } catch (ResourceAccessException e) {
            log.error("Unknown error while attempting to create user", e);
            throw new ExternalServiceException();
        }

        log.info("User {} successfully created", username);

        return user.getId();
    }
}
