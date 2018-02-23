package dao;

import domain.Role;
import domain.Tweet;
import domain.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDaoColl implements UserDao{
    ArrayList<User> users = new ArrayList<>();



    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void removeUser(User user) {
    users.remove(user);
    }

    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public User findByUsername(String name) {
        for (User u: users) {
            if (u.getUsername() == name) {
                return u;
            }
        }
        return null;
    }
}
