package com.safespot.fx.utils;

import com.safespot.fx.services.UserService;
import com.safespot.fx.models.User;

import java.util.Objects;

public class SecurityUtils {
    // TODO this needs to return the currently logged in user
    // hard coded current user until integration with user management functionality
    public static User getCurrentUser(){
        User currentUser = new UserService().findById(1);
        if (Objects.isNull(currentUser))
            throw new IllegalStateException("DB must contain a user with id 1 to be used as static current logged-in user");
        return currentUser;
    }
}
