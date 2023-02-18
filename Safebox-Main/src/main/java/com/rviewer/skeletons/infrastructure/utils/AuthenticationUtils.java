package com.rviewer.skeletons.infrastructure.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public final class AuthenticationUtils {

    public static String retrieveCurrenUserToken() {
        return SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
    }
}
