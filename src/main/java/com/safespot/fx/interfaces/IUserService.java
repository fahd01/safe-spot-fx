package com.safespot.fx.interfaces;


import com.safespot.fx.models.User;

import java.util.List;

public interface IUserService {
    boolean login(String email, String password);
    boolean signup(User user);
    boolean updateAccount(User user);
    List<User> getAllUsers();

    User findById(int id);
}
