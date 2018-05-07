package dao.JPA;

import dao.UserDao;
import domain.Credential;
import domain.Group;
import domain.Tweet;
import domain.User;
import exception.UserNotFoundException;
import interceptor.Managed;
import service.TweetService;
import util.PasswordHash;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Stateless
@JPA
public class UserDaoJPA implements UserDao {

    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;

    @Inject
    private TweetService tService;

    public UserDaoJPA() {
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void setTweetService(TweetService t) {
        this.tService = t;
    }

    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    @Managed
    public void removeUser(User user) {
        List<Tweet> result = new ArrayList<>();
        try {
            result = tService.getTweetsByUsername(user.getUsername());
        } catch (UserNotFoundException e) {
            System.out.println("User already deleted, or no tweets are found for user");
        }

        for (Tweet t : result) {
            tService.removeTweet(t);
        }

        em.remove(user);
    }

    @Override
    public List<User> getUsers() {
        Query query = em.createQuery("SELECT u FROM User u");
        return query.getResultList();
    }


    @Override
    public List<User> searchUser(String searchquery, int amount) {
        TypedQuery<User> query = em.createNamedQuery("user.searchByName", User.class);
        query.setParameter("query", "%"+ searchquery + "%");
        query.setMaxResults(amount);
        List<User> result = query.getResultList();
        return result;
    }

    @Override
    public User findByUsername(String name) throws UserNotFoundException {
        TypedQuery<User> query = em.createNamedQuery("user.findByName", User.class);
        query.setParameter("username", name);
        List<User> result = query.getResultList();
        if (result.size() <= 0) {
            throw new UserNotFoundException(name);
        }

        return result.get(0);
    }

    @Override
    @Managed
    public void FollowUser(User user, User userToFollow) {
        user.followUser(userToFollow);
    }

    @Override
    @Managed
    public void UnfollowUser(User user, User userToUnfollow) {
        user.unFollowUser(userToUnfollow);

    }

    @Override
    @Managed
    public void addGroup(User user, Group g) {
        user.addGroup(g);
    }

    @Override
    @Managed
    public void removeGroup(User user, Group g) {
        user.removeGroup(g);
    }

    @Override
    public void authenticate(Credential cred) throws LoginException {
        TypedQuery<String> query = em.createNamedQuery("user.getPassword", String.class);
        query.setParameter("username", cred.getUsername());
        List<String> result = query.getResultList();
        if (result.size() <= 0) {
            throw new LoginException("Did not find user " + cred.getUsername());
        }

        String userPass =  result.get(0);

        if(!userPass.equals(cred.getPasswordHash())) {
            throw new LoginException("Passwords did not match!");
        }
    }




}
