package dao;

import model.User;

public interface UserDAO {
    User findByUsername(String username);
    boolean createUser(User user);
}
