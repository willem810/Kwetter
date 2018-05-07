package dao.coll;

import dao.Collection.TweetDaoColl;
import dao.TweetDao;
import domain.Tweet;
import domain.User;
import exception.UserNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TweetDaoCollTest {

    private TweetDao tweetDao;

    @Before
    public void setUp() {
        tweetDao = new TweetDaoColl();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getRecentTweets() throws UserNotFoundException {

        User willem = new User("Willem", "Willem Toemen", "test", "test");
        User lorenzo = new User("Lorenzo", "Lorenzo van den Weerden", "test", "test");

        Tweet tweetWillem1 = new Tweet(willem, "testMessage 1");
        Tweet tweetWillem2 = new Tweet(willem, "testMessage 2");
        Tweet tweetWillem3 = new Tweet(willem, "testMessage 3");
        Tweet tweetLorenzo1 = new Tweet(lorenzo, "testMessage 4");
        Tweet tweetLorenzo2 = new Tweet(lorenzo, "testMessage 5");

        tweetDao.addTweet(tweetWillem1);
        tweetDao.addTweet(tweetWillem2);
        tweetDao.addTweet(tweetWillem3);
        tweetDao.addTweet(tweetLorenzo1);
        tweetDao.addTweet(tweetLorenzo2);

        ArrayList<Tweet> expected = new ArrayList<>();
        expected.add(tweetWillem3);
        List<Tweet> recentTweets = tweetDao.getRecentTweets("Willem", 1);
        Assert.assertEquals(expected, recentTweets);

        expected.add(tweetWillem2);
        recentTweets = tweetDao.getRecentTweets("Willem", 2);
        Assert.assertEquals(expected, recentTweets);

        expected.clear();
        expected.add(tweetLorenzo2);
        recentTweets = tweetDao.getRecentTweets("Lorenzo", 1);
        Assert.assertEquals(expected, recentTweets);
    }

}
