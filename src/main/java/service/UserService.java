package service;

import dao.JPA.JPA;
import dao.UserDao;
import domain.Credential;
import domain.Group;
import domain.Tweet;
import domain.User;
import exception.UserNotFoundException;
import interceptor.Logging;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Logging
public class UserService {
    @Inject
    @JPA
    public UserDao userDao;

    @Inject
    private TweetService tweetService;

    /**
     * addUser adds a user in the persistence unit
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        userDao.addUser(user);
    }

    /**
     * removeUser removes a user in the persistence unit
     *
     * @param user the user to remove
     */
    @RolesAllowed({"Admin", "Moderator"})
    public void removeUser(User user) {
        userDao.removeUser(user);
    }

    /**
     * removeUser removes a user in the persistence unit, requiring only it's name
     *
     * @param username the username from the user to remove
     * @throws UserNotFoundException when no user is found with the given username
     */
    @RolesAllowed({"Admin", "Moderator"})
    public void removeUser(String username) throws UserNotFoundException {
        User user = findByUsername(username);
        userDao.removeUser(user);
    }

    /**
     * getUsers returns a list of all users found in the persistence unit
     *
     * @return a list of users
     */
    public List<User> getUsers() {
        return userDao.getUsers();
    }


    public List<User> searchUser(String query, int amount) {
        return userDao.searchUser(query, amount);
    }

    /**
     * findByUsername returns a User object found by it's username
     *
     * @param name the username of the user to return
     * @return the found user
     * @throws UserNotFoundException when no user is found with the given username
     */
    public User findByUsername(String name) throws UserNotFoundException {
        return userDao.findByUsername(name);
    }

    /**
     * followUser follows a user
     *
     * @param user         the base user
     * @param userToFollow the user that the base user wants to follow
     */
    public void followUser(User user, User userToFollow) {
        userDao.FollowUser(user, userToFollow);
    }

    /**
     * unFollowUser unfollows a user
     *
     * @param user           the base user
     * @param userToUnfollow the user that the base user wants to unfollow
     */
    public void unFollowUser(User user, User userToUnfollow) {
        userDao.UnfollowUser(user, userToUnfollow);
    }

    /**
     * getTweets returns a list of all tweets of a user
     *
     * @param username the username of the user
     * @return a list of all tweets from the user
     * @throws UserNotFoundException when no user is found with the given username
     */
    public List<Tweet> getTweets(String username) throws UserNotFoundException {
        return tweetService.getTweetsByUsername(username);
    }

    public long getTweetCount(String username) throws UserNotFoundException {
        return tweetService.getTweetCount(username);
    }

    /**
     * getRecentTweets returns a list of the most recent tweets of a user.
     *
     * @param username the user from which to return recent tweets
     * @param i        amount of recent tweets to return
     * @return a list of recent tweets of the user
     * @throws UserNotFoundException is thrown when no user is found with the given username
     */
    public List<Tweet> getRecentTweets(String username, int i) throws UserNotFoundException {
        return tweetService.getRecentTweets(username, i);
    }


    /**
     * Adds a Group from a user
     *
     * @param user the user to add a Group to
     * @param g    the Group to add
     */
    public void addGroup(User user, Group g) {
        userDao.addGroup(user, g);
    }

    /**
     * Removes a Group from a user
     *
     * @param user the user to remove a Group from
     * @param g    the Group to remove
     */
    public void removeGroup(User user, Group g) {
        userDao.removeGroup(user, g);
    }

    /**
     * Tweet creates a new tweet
     *
     * @param user    the user that tweets
     * @param message the message the tweet should require
     */
    public void Tweet(User user, String message) {
        tweetService.addTweet(new Tweet(user, message));
    }

    /**
     * setUserDao sets the userDao
     *
     * @param dao userDao to set
     */
    public void setUserDao(UserDao dao) {
        this.userDao = dao;
    }

    /**
     * setTweetService sets the tweetService
     *
     * @param service tweetService to set
     */
    public void setTweetService(TweetService service) {
        this.tweetService = service;
    }

    public List<Tweet> getFeed(String username) throws UserNotFoundException {
        return this.tweetService.getFeed(username);
    }

    public void authenticate(Credential cred) throws LoginException {
        userDao.authenticate(cred);
    }

    public UserService() {
    }
}
