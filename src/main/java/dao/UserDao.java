package dao;

import domain.Credential;
import domain.Group;
import domain.Tweet;
import domain.User;
import exception.UserNotFoundException;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public interface UserDao {

    void addUser(User user);
    void removeUser(User user);
    List<User> getUsers();
    List<User> searchUser(String searchquery, int amount);
    User findByUsername(String name) throws UserNotFoundException;
    void FollowUser(User user, User userToFollow);
    void UnfollowUser(User user, User userToUnfollow) ;

    void addGroup(User user, Group g);
    void removeGroup(User user, Group g);

    void authenticate(Credential cred) throws LoginException;
}
