package com.rviewer.skeletons.infrastructure.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public final class AuthenticationUtils {

    public static String getTokenUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
    }

    public static String getLoggedInUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
