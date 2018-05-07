package dao.JPA;

import dao.TweetDao;
import domain.Tweet;
import domain.User;
import exception.TweetNotFoundException;
import exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TweetDaoJPATest {
    private TweetDao tweetDao;

    @Mock
    private EntityManager em;

    @Before
    public void setUp() {
        TweetDaoJPA jpa = new TweetDaoJPA();
        jpa.setEntityManager(em);
        tweetDao = jpa;
    }



    @Test
    public void getTweets() {
    }

    @Test
    public void getRecentTweets() throws UserNotFoundException {
        User willem = new User("willem", "willem test", "", "" );
        ArrayList<Tweet> expected = new ArrayList<>();
        expected.add(new Tweet(willem, "test 1"));
        expected.add(new Tweet(willem, "test 2"));

        TypedQuery<Tweet> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("tweet.findRecentTweets", Tweet.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(expected);

        List<Tweet> actual = tweetDao.getRecentTweets(willem.getUsername(), 11);

        verify(mockedQuery, times(1)).setParameter("username", willem.getUsername());
        verify(mockedQuery, times(1)).setMaxResults(11);

        Assert.assertEquals(expected, actual);
    }

    @Test (expected = UserNotFoundException.class)
    public void getRecentTweetsUserNotFound() throws UserNotFoundException {
        TypedQuery<Tweet> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("tweet.findRecentTweets", Tweet.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(new ArrayList<>());

        tweetDao.getRecentTweets("fakeUsername", 1);
    }

    @Test
    public void findTweetById() throws TweetNotFoundException {
        Tweet tweet = new Tweet();
        tweet.setId(3L);

        ArrayList<Tweet> returnList = new ArrayList<>();
        returnList.add(tweet);

        TypedQuery<Tweet> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("tweet.findById", Tweet.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(returnList);

        Tweet actual = tweetDao.findTweetById(tweet.getId());

        verify(mockedQuery, times(1)).setParameter("id", tweet.getId());

        Assert.assertEquals(tweet, actual);
    }

    @Test (expected = TweetNotFoundException.class)
    public void findTweetByIdTweetNotFound() throws TweetNotFoundException {


        TypedQuery<Tweet> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("tweet.findById", Tweet.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(new ArrayList<>());

        tweetDao.findTweetById(-1L);
    }

    @Test
    public void findTweetsByUsername() throws UserNotFoundException {
        User willem = new User("willem", "willem test", "", "" );
        ArrayList<Tweet> expected = new ArrayList<>();
        expected.add(new Tweet(willem, "test 1"));
        expected.add(new Tweet(willem, "test 2"));

        TypedQuery<Tweet> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("tweet.findByUsername", Tweet.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(expected);

        List<Tweet> actual = tweetDao.findTweetsByUsername(willem.getUsername());

        verify(mockedQuery, times(1)).setParameter("username", willem.getUsername());

        Assert.assertEquals(expected, actual);
    }

    @Test (expected = UserNotFoundException.class)
    public void findTweetsByUsernameUserNotFound() throws UserNotFoundException {
        TypedQuery<Tweet> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("tweet.findByUsername", Tweet.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(new ArrayList<>());

        tweetDao.findTweetsByUsername("fakeUser");
    }
}
