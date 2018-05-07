package service;

import dao.UserDao;
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
public class UserServiceTest {
    UserService uService;

    @Mock
    UserDao userDao;

    @Mock
    TweetService tweetService;

    @Before
    public void setUp() {
        uService = new UserService();
        uService.setUserDao(userDao);
        uService.setTweetService(tweetService);
    }

    @Test
    public void addUser() {
        User u1 = new User();
        User u2 = new User();

        uService.addUser(u1);
        uService.addUser(u2);

        verify(userDao, times(1)).addUser(u1);
        verify(userDao, times(1)).addUser(u2);
    }

    @Test
    public void removeUser() {
        User u1 = new User();
        User u2 = new User();

        uService.removeUser(u1);
        uService.removeUser(u2);

        verify(userDao, times(1)).removeUser(u1);
        verify(userDao, times(1)).removeUser(u2);
    }

    @Test
    public void findByUsername() throws UserNotFoundException {
        String u1 = "user1";
        String u2 = "user2";

        uService.findByUsername(u1);
        uService.findByUsername(u2);

        verify(userDao, times(1)).findByUsername(u1);
        verify(userDao, times(1)).findByUsername(u2);
    }

    @Test
    public void getTweets() throws UserNotFoundException {
        String u1 = "user1";
        ArrayList<Tweet> u1Tweets = new ArrayList<>();
        u1Tweets.add(new Tweet());
        u1Tweets.add(new Tweet());

        String u2 = "user2";
        ArrayList<Tweet> u2Tweets = new ArrayList<>();
        u2Tweets.add(new Tweet());
        u2Tweets.add(new Tweet());
        u2Tweets.add(new Tweet());

        when(tweetService.getTweetsByUsername(u1)).thenReturn(u1Tweets);
        when(tweetService.getTweetsByUsername(u2)).thenReturn(u2Tweets);

        Assert.assertEquals(u1Tweets, uService.getTweets(u1));
        Assert.assertEquals(u2Tweets, uService.getTweets(u2));

        verify(tweetService, times(1)).getTweetsByUsername(u1);
        verify(tweetService, times(1)).getTweetsByUsername(u2);
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

        when(tweetService.getRecentTweets(u1.getUsername(), 2)).thenReturn(u1Tweets.subList(0, 1));
        when(tweetService.getRecentTweets(u2.getUsername(), 3)).thenReturn(u2Tweets.subList(0, 2));

        Assert.assertEquals(u1Tweets.subList(0, 1), uService.getRecentTweets(u1.getUsername(), 2));
        Assert.assertEquals(u2Tweets.subList(0, 2), uService.getRecentTweets(u2.getUsername(), 3));

        verify(tweetService, times(1)).getRecentTweets(u1.getUsername(),2);
        verify(tweetService, times(1)).getRecentTweets(u2.getUsername(),3);
    }

    @Test
    public void tweet() {
        User u1 = new User();
        String u1Message = "user 1 message 1";
        User u2 = new User();
        String u2Message = "user 2 message 1";

        uService.Tweet(u1, u1Message);
        uService.Tweet(u2, u2Message);

        verify(tweetService, times(2)).addTweet(any());
    }
}
