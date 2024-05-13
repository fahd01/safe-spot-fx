package com.safespot.fx.utils;

import com.safespot.fx.services.UserService;
import com.safespot.fx.models.User;

import java.util.Objects;

public class SecurityUtils {

    public static final boolean DEV_MODE = true;

    public static User getCurrentUser(){
        if (DEV_MODE)
            return getCurrentUserInDevMode();

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (Objects.isNull(currentUser))
            throw new IllegalStateException("No logged-in user");
        return currentUser;
    }

    private static User getCurrentUserInDevMode(){
        User currentUser = new UserService().findById(1);
        if (Objects.isNull(currentUser))
            throw new IllegalStateException("Dev mode enabled! DB must contain a user with id 1 to be used as current logged-in user");
        return currentUser;
    }


}
