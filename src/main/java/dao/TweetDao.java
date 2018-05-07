package dao;

import domain.Tweet;
import exception.TweetNotFoundException;
import exception.UserNotFoundException;

import java.util.List;

public interface TweetDao {

    Tweet addTweet(Tweet tweet);
    void removeTweet(Tweet tweet);
    List<Tweet> getTweets();
    List<Tweet> getRecentTweets(String username, int count) throws UserNotFoundException;
    Tweet findTweetById(Long id) throws TweetNotFoundException;
    List<Tweet> findTweetsByUsername(String name) throws UserNotFoundException;
    long getTweetCount(String username) throws UserNotFoundException;

    List<Tweet> getFeed(String username) throws UserNotFoundException;

    void Like(Tweet tweet);
    void Unlike(Tweet tweet);
}
