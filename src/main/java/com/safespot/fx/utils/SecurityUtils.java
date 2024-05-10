package com.safespot.fx.utils;

import com.safespot.fx.services.UserService;
import com.safespot.fx.models.User;

import java.util.Objects;

public class SecurityUtils {
    public static User getCurrentUser(){
        //User currentUser = new UserService().findById(1);
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (Objects.isNull(currentUser))
            throw new IllegalStateException("DB must contain a user with id 1 to be used as static current logged-in user");
        return currentUser;
    }
}
