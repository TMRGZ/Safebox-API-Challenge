package com.rviewer.skeletons.infrastructure.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public final class AuthenticationUtils {

    public static String getTokenUsername() {
        Optional<Object> usernameOptional = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getDetails());
        return usernameOptional.map(Object::toString).orElse("");
    }

    public static String getLoggedInUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
