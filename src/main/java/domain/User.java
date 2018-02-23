package domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id;

    private String username;
    private String name;
    private String location;
    private String web;
    private String bio;

    public List<Tweet> Tweets = new ArrayList<>();
    public List<Role> Roles = new ArrayList<>();
    public List<User> UsersToFollow = new ArrayList<>();
    public List<User> Followers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Tweet> getTweets() {
        return Tweets;
    }

    public void addTweet(Tweet tweet) {
        Tweets.add(tweet);
    }

    public List<Role> getRoles() {
        return Roles;
    }

    public void addRoles(Role role) {
        Roles.add(role);
    }

    public List<User> getUsersToFollow() {
        return UsersToFollow;
    }

    public void addUsersToFollow(User userToFollow) {
        UsersToFollow.add(userToFollow);
    }

    public void Tweet(String message) {
        Tweet tweet = new Tweet(this, message);
        Tweets.add(tweet);
    }
}
