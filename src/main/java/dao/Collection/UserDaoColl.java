package dao.Collection;

import dao.UserDao;
import domain.Credential;
import domain.Group;
import domain.User;
import exception.UserNotFoundException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Default
public class UserDaoColl implements UserDao {
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
    public List<User> getUsers() {
        return users;
    }

    @Override
    public List<User> searchUser(String searchquery, int amount) {
        return null;
    }

    @Override
    public User findByUsername(String name) throws UserNotFoundException {
        for (User u : users) {
            if (u.getUsername().equals(name)) {
                return u;
            }
        }

        throw new UserNotFoundException(name);
    }

    @Override
    public void FollowUser(User user, User userToFollow) {
        user.followUser(userToFollow);
    }

    @Override
    public void UnfollowUser(User user, User userToUnfollow) {
        user.unFollowUser(userToUnfollow);
    }

    @Override
    public void addGroup(User user, Group g) {
        user.addGroup(g);
    }

    @Override
    public void removeGroup(User user, Group g) {
        user.removeGroup(g);
    }

    @Override
    public void authenticate(Credential cred) throws LoginException {

    }
}
