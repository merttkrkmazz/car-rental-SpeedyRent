package service.impl;

import service.interfaces.Authentication;
import dao.UserDAO;
import model.User;

public class AuthenticationImpl implements Authentication {
    private final UserDAO userDAO;

    public AuthenticationImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean login(String username, String password) {
        User user = userDAO.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public boolean register(String username, String password) {
        if (userDAO.findByUsername(username) != null) return false; // already exists
        return userDAO.createUser(new User(username, password));
    }
}
