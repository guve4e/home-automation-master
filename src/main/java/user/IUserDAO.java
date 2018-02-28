package user;

import models.User;

public interface IUserDAO {
    User getUser(int userId);
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
}