package edu.esprit.user.services;

import edu.esprit.user.entities.User;

import java.util.List;

public interface IUserService {
    boolean login(String email, String password);
    boolean signup(User user);
    boolean updateAccount(User user);
    List<User> getAllUsers();
}
