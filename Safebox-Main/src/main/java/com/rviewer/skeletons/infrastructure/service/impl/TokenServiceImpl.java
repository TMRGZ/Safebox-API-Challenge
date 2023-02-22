package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.BadTokenException;
import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.infrastructure.mapper.UserMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.TokenApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import com.rviewer.skeletons.infrastructure.utils.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenApi tokenApi;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User decodeToken(String token) {
        User user;

        try {
            log.info("Attempting to decode token: {}", token);
            tokenApi.getApiClient().setBearerToken(token);

            AuthUserDto userDto = tokenApi.decodeToken();
            user = userMapper.map(userDto);

        } catch (HttpClientErrorException e) {
            log.error("Client error {} while attempting to decode token {}", e.getStatusCode(), token);

            if (e.getStatusCode() == HttpStatus.FORBIDDEN || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BadTokenException();
            }

            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            log.error("Server error {} while attempting to decode token {}", e.getStatusCode(), token);
            throw new ExternalServiceException();
        } catch (ResourceAccessException e) {
            log.error("Unknown error while attempting to decode token", e);
            throw new ExternalServiceException();
        }

        log.info("Decoding successful for token {}", token);

        return user;
    }

    @Override
    public String retrieveCurrenUserToken() {
        return AuthenticationUtils.retrieveCurrenUserToken();
    }
}
