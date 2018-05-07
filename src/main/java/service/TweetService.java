package service;

import dao.JPA.JPA;
import dao.TweetDao;
import domain.Hashtag;
import domain.Permissions;
import domain.Tweet;
import exception.TweetNotFoundException;
import exception.UserNotFoundException;
import interceptor.Authentication;
import interceptor.Logging;
import socket.TweetEndpoint;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@Logging
public class TweetService {

    @Inject @JPA
    TweetDao tweetDao;

    @Inject
    HashtagService hashtagService;

    @Inject
    TweetEndpoint tweetEndpoint;

    /**
     * addTweet adds a tweet to the persistence unit
     * @param tweet the tweet to add
     */
    public void addTweet(Tweet tweet) {
        tweet.setHashtags(getHashtags(tweet));
        tweet = tweetDao.addTweet(tweet);
        tweetEndpoint.sendTweet(tweet);
    }

    /**
     * removeTweet removes a tweet from the persistence unit
     * @param tweet the tweet to remove
     */
    public void removeTweet(Tweet tweet) {
        tweetDao.removeTweet(tweet);
    }

    /**
     * getTweets returns a list of all tweets
     * @return a list of all tweets
     */
    public List<Tweet> getTweets() {
        return tweetDao.getTweets();
    }

    /**
     * getRecentTweets returns a list of recent tweets of a user
     * @param username the user to return tweets from
     * @param count the amount of tweets to return
     * @return returns a list of the recent tweets
     * @throws UserNotFoundException is thrown when no user is found with the given username
     */
    public List<Tweet> getRecentTweets(String username, int count) throws UserNotFoundException{
        return tweetDao.getRecentTweets(username, count);
    }

    /**
     * findTweetByid returns a the tweet using it's id
     * @param id the id of the tweet
     * @return a tweet
     * @throws TweetNotFoundException when no tweet is found with the given id
     */
    public Tweet findTweetById(Long id) throws TweetNotFoundException {
       return tweetDao.findTweetById(id);
    }

    /**
     * getTweetsByUsername returns a list of all tweets of a user
     * @param name the name of the user
     * @return a list of all tweets of a user
     * @throws UserNotFoundException when no user is found with the given username
     */
    public List<Tweet> getTweetsByUsername(String name) throws UserNotFoundException {
        return tweetDao.findTweetsByUsername(name);
    }

    public long getTweetCount(String name) throws UserNotFoundException {
        return tweetDao.getTweetCount(name);
    }

    /**
     * like likes a tweet
     * @param tweet the tweet to like
     */
    public void like(Tweet tweet) {
        tweetDao.Like(tweet);
    }

    /**
     * unlikes a tweet
     * @param tweet the tweet to unlike
     */
    public void unlike(Tweet tweet) {
        tweetDao.Unlike(tweet);
    }

    /**
     * getHashtags abstracts all hashtags in a tweet message
     * @param tweet the tweet to check for hashtags
     * @return all hashtags found in the tweets message
     */
    public ArrayList<Hashtag> getHashtags(Tweet tweet) {
        ArrayList<Hashtag> tags = new ArrayList<>();
        if (tweet.getMessage() != null) {
            Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
            Matcher mat = MY_PATTERN.matcher(tweet.getMessage());
            while (mat.find()) {
                tags.add(hashtagService.findByName(mat.group(1)));
            }
        }

        return tags;
    }

    /**
     * getMentions abstracts all mentions in a tweet message
     * @param tweet the tweet to check for mentions
     * @return all mentions found in the tweets message
     */
    public ArrayList<String> getMentions(Tweet tweet) {
        ArrayList<String> mentions = new ArrayList<>();
        if (tweet.getMessage() != "") {
            Pattern MY_PATTERN = Pattern.compile("@(\\S+)");
            Matcher mat = MY_PATTERN.matcher(tweet.getMessage());
            while (mat.find()) {
                String mention = mat.group(1);
                mentions.add(mention);
            }
        }

        return mentions;
    }

    public List<Tweet> getFeed(String username) throws UserNotFoundException{
        return tweetDao.getFeed(username);
    }

    /**
     * setTweetDao sets the tweetDao
     * @param dao the tweetDao to set
     */
    @Authentication(Permissions.tweet_admin)
    public void setTweetDao(TweetDao dao) {
        this.tweetDao = dao;
    }

    /**
     * setHashtagService sets the hashtagService
     * @param service the hashtagService to set
     */
    @Authentication(Permissions.tweet_admin)
    public void setHashtagService(HashtagService service) {
        this.hashtagService = service;
    }
}
