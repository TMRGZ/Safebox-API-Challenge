package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.BadTokenException;
import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.exception.UserIsUnauthorizedException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.TokenApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenApi tokenApi;

    @Override
    public User decodeToken(String token) {
        User user;

        try {
            tokenApi.getApiClient().setBearerToken(token);
            AuthUserDto userDto = tokenApi.decodeToken();
            user = new User();
            user.setUsername(userDto.getUsername());

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BadTokenException();
            }

            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException();
        }

        return user;
    }

    @Override
    public String retrieveCurrenUserToken() {
        return SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
    }
}
