package com.rviewer.skeletons.infrastructure.service.impl;


import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthRegisteredUserDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserApi userApi;

    @Override
    public String createUser(String username, String password) {
        AuthRegisteredUserDto registeredUserDto;

        try {
            registeredUserDto = userApi.postUser(new AuthUserDto().username(username).password(password));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new SafeboxAlreadyExistsException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException();
        }

        return registeredUserDto.getId();
    }
}
