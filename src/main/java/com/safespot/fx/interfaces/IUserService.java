package com.safespot.fx.interfaces;

import com.safespot.fx.models.User;

public interface IUserService {
    User findById(int id);
}
