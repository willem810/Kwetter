package dao;

import domain.Role;
import domain.Tweet;
import domain.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserDao {

    void addUser(User user);
    void removeUser(User user);
    ArrayList<User> getUsers();
    User findByUsername(String name);
}
