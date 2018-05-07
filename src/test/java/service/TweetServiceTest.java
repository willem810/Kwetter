package service;

import dao.TweetDao;
import domain.Hashtag;
import domain.Tweet;
import domain.User;
import exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceTest {
    TweetService tService;

    @Mock
    TweetDao tweetDao;

    @Mock
    HashtagService hashtagService;

    @Before
    public void setUp() {
        tService = new TweetService();
        tService.setTweetDao(tweetDao);
        tService.setHashtagService(hashtagService);
    }

    @Test
    public void addTweet() {
        Tweet t1 = new Tweet(null, "message #1");
        Tweet t2 = new Tweet(null, "message 2");

        tService.addTweet(t1);
        tService.addTweet(t2);

        verify(hashtagService, times(1)).findByName("1");
        verify(hashtagService, times(0)).findByName("2");

        verify(tweetDao, times(1)).addTweet(t1);
        verify(tweetDao, times(1)).addTweet(t2);
    }

    @Test
    public void removeTweet() {
        Tweet t1 = new Tweet(null, "message #1");
        Tweet t2 = new Tweet(null, "message 2");

        tService.removeTweet(t1);
        tService.removeTweet(t2);

        verify(tweetDao, times(1)).removeTweet(t1);
        verify(tweetDao, times(1)).removeTweet(t2);
    }

    @Test
    public void getTweets() {
        Tweet t1 = new Tweet(null, "message #1");
        Tweet t2 = new Tweet(null, "message 2");

        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(t1);
        tweets.add(t2);

        when(tweetDao.getTweets()).thenReturn(tweets);

        Assert.assertEquals(tweets, tService.getTweets());

    }

    @Test
    public void getRecentTweets() throws UserNotFoundException {
        User u1 = new User("user1", "user name 1", "test", "test");
        ArrayList<Tweet> u1Tweets = new ArrayList<>();
        u1Tweets.add(new Tweet(u1, "user 1 message 1"));
        u1Tweets.add(new Tweet(u1, "user 1 message 2"));
        u1Tweets.add(new Tweet(u1, "user 1 message 3"));

        User u2 = new User("user2", "user name 2", "test", "test");
        ArrayList<Tweet> u2Tweets = new ArrayList<>();
        u2Tweets.add(new Tweet(u2, "user 2 message 1"));
        u2Tweets.add(new Tweet(u2, "user 2 message 2"));
        u2Tweets.add(new Tweet(u2, "user 3 message 3"));
        u2Tweets.add(new Tweet(u2, "user 3 message 4"));

        when(tweetDao.getRecentTweets(u1.getUsername(), 2)).thenReturn(u1Tweets.subList(0, 1));
        when(tweetDao.getRecentTweets(u2.getUsername(), 3)).thenReturn(u2Tweets.subList(0, 2));

        Assert.assertEquals(u1Tweets.subList(0, 1), tService.getRecentTweets(u1.getUsername(), 2));
        Assert.assertEquals(u2Tweets.subList(0, 2), tService.getRecentTweets(u2.getUsername(), 3));

        verify(tweetDao, times(1)).getRecentTweets(u1.getUsername(), 2);
        verify(tweetDao, times(1)).getRecentTweets(u2.getUsername(), 3);
    }

    @Test
    public void getTweetsByUsername() throws UserNotFoundException {
        User u1 = new User("user1", "user name 1", "test", "test");
        ArrayList<Tweet> u1Tweets = new ArrayList<>();
        u1Tweets.add(new Tweet(u1, "user 1 message 1"));
        u1Tweets.add(new Tweet(u1, "user 1 message 2"));

        User u2 = new User("user2", "user name 2", "test", "test");
        ArrayList<Tweet> u2Tweets = new ArrayList<>();
        u2Tweets.add(new Tweet(u2, "user 2 message 1"));
        u2Tweets.add(new Tweet(u2, "user 2 message 2"));
        u2Tweets.add(new Tweet(u2, "user 3 message 3"));

        when(tweetDao.findTweetsByUsername(u1.getUsername())).thenReturn(u1Tweets);
        when(tweetDao.findTweetsByUsername(u2.getUsername())).thenReturn(u2Tweets);

        Assert.assertEquals(u1Tweets, tweetDao.findTweetsByUsername(u1.getUsername()));
        Assert.assertEquals(u2Tweets, tweetDao.findTweetsByUsername(u2.getUsername()));

        verify(tweetDao, times(1)).findTweetsByUsername(u1.getUsername());
        verify(tweetDao, times(1)).findTweetsByUsername(u2.getUsername());
    }

    @Test
    public void like() {
        Tweet t1 = new Tweet(null, "message #1");
        Tweet t2 = new Tweet(null, "message 2");

        tService.like(t1);
        tService.like(t2);

        verify(tweetDao, times(1)).Like(t1);
        verify(tweetDao, times(1)).Like(t2);
    }

    @Test
    public void unlike() {
        Tweet t1 = new Tweet(null, "message #1");
        Tweet t2 = new Tweet(null, "message 2");

        tService.unlike(t1);
        tService.unlike(t2);

        verify(tweetDao, times(1)).Unlike(t1);
        verify(tweetDao, times(1)).Unlike(t2);
    }

    @Test
    public void getHashtags() {
        Tweet t1 = new Tweet(null, "message #1 #unittest this @works great");
        Hashtag h1 = new Hashtag("1");
        Hashtag h2 = new Hashtag("unittest");

        ArrayList<Hashtag> expected = new ArrayList<>();
        expected.add(h1);
        expected.add(h2);

        when(hashtagService.findByName("1")).thenReturn(h1);                                // expected
        when(hashtagService.findByName("unittest")).thenReturn(h2);                         // expected

        ArrayList<Hashtag> actual = tService.getHashtags(t1);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getMentions() {
        Tweet t1 = new Tweet(null, "message @user from #creator @willem");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("user");
        expected.add("willem");

        ArrayList<String> actual = tService.getMentions(t1);

        Assert.assertEquals(expected, actual);
    }
}
