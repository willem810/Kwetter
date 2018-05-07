package dao.Collection;

import dao.TweetDao;
import domain.Tweet;
import exception.TweetNotFoundException;
import exception.UserNotFoundException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
@Default
public class TweetDaoColl implements TweetDao {

    List<Tweet> tweets = new ArrayList<>();


    @Override
    public Tweet addTweet(Tweet tweet) {
        tweets.add(tweet);
        return tweet;
    }

    @Override
    public void removeTweet(Tweet tweet) {
        tweets.remove(tweet);
    }

    @Override
    public List<Tweet> getTweets() {
        return tweets;
    }

    @Override
    public List<Tweet> getRecentTweets(String username, int count) throws UserNotFoundException {

        List<Tweet> reverseList = new ArrayList<>(findTweetsByUsername(username));
        Collections.reverse(reverseList);
        if (count == -1) {
            count = reverseList.size();
        }

        return reverseList.subList(0, Math.min(reverseList.size(), count));
    }

    @Override
    public Tweet findTweetById(Long id) throws TweetNotFoundException {
        for (Tweet t : tweets) {
            if (t.getId() == id) {
                return t;
            }
        }

        return null;
    }

    @Override
    public List<Tweet> findTweetsByUsername(String name) throws UserNotFoundException {
        ArrayList<Tweet> userTweets = new ArrayList<>();
        for (Tweet t : tweets) {
            if (t.getUser().getUsername().equals(name)) {
                userTweets.add(t);
            }
        }

        if (userTweets.size() <= 0) {
            throw new UserNotFoundException(name);
        }

        return userTweets;
    }

    @Override
    public long getTweetCount(String username) throws UserNotFoundException {
        return findTweetsByUsername(username).size();
    }

    @Override
    public List<Tweet> getFeed(String username) throws UserNotFoundException {
        return getTweets();
    }

    @Override
    public void Like(Tweet tweet) {
        tweet.like();
    }

    @Override
    public void Unlike(Tweet tweet) {
        tweet.unlike();
    }


}
