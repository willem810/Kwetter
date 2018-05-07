package rest;

import domain.Tweet;
import domain.User;
import exception.TweetNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import service.TweetService;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TweetResourceTest {
    TweetResource tResource = new TweetResource();

    @Mock
    TweetService tService;


    @Before
    public void setUp() {
        tResource.setTweetService(tService);
    }


    @Test
    public void getTweets() {
        Response res = tResource.getTweets();

        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(tService, times(1)).getTweets();
    }

    @Test
    public void getTweet() throws TweetNotFoundException {
        Tweet t1 = new Tweet(1L, new User("user", "user" ,"", ""), "");
        when(tService.findTweetById(t1.getId())).thenReturn(t1);
        when(tService.findTweetById(-1L)).thenThrow(TweetNotFoundException.class);

        Response res = tResource.getTweet(t1.getId().intValue());
        String json = res.getEntity().toString();
        Assert.assertEquals(t1.toJson().toString(), json);
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(tService, times(1)).findTweetById(t1.getId());

        res = tResource.getTweet(-1);
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(tService, times(2)).findTweetById(any(Long.class));
    }

    @Test
    public void addTweet() {
        User t1User = new User("user", "user" ,"", "");
        t1User.setId(1L);
        Tweet t1 = new Tweet(t1User, "");

        Response res = tResource.addTweet(t1);

        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(tService, times(1)).addTweet(t1);
    }

    @Test
    public void removeTweet() throws TweetNotFoundException {
        Tweet t1 = new Tweet(1L, new User("user", "user" ,"", ""), "");

        when(tService.findTweetById(t1.getId())).thenReturn(t1);
        when(tService.findTweetById(-1L)).thenThrow(TweetNotFoundException.class);

        Response res = tResource.removeTweet(t1.getId().intValue());
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(tService, times(1)).findTweetById(t1.getId());
        Mockito.verify(tService, times(1)).removeTweet(t1);

        res = tResource.removeTweet(-1);
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(tService, times(2)).findTweetById(any(Long.class));
        Mockito.verify(tService, times(1)).removeTweet(t1);
    }

    @Test
    public void like() throws TweetNotFoundException {
        Tweet t1 = new Tweet(1L, new User("user", "user" ,"", ""), "");

        when(tService.findTweetById(t1.getId())).thenReturn(t1);
        when(tService.findTweetById(-1L)).thenThrow(TweetNotFoundException.class);

        Response res = tResource.like(1);
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(tService, times(1)).findTweetById(t1.getId());
        Mockito.verify(tService, times(1)).like(t1);

        res = tResource.like(-1);
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(tService, times(2)).findTweetById(any(Long.class));
        Mockito.verify(tService, times(1)).like(t1);
    }

    @Test
    public void unlike() throws TweetNotFoundException {
        Tweet t1 = new Tweet(1L, new User("user", "user" ,"", ""), "");

        when(tService.findTweetById(t1.getId())).thenReturn(t1);
        when(tService.findTweetById(-1L)).thenThrow(TweetNotFoundException.class);

        Response res = tResource.unlike(1);
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(tService, times(1)).findTweetById(t1.getId());
        Mockito.verify(tService, times(1)).unlike(t1);

        res = tResource.unlike(-1);
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(tService, times(2)).findTweetById(any(Long.class));
        Mockito.verify(tService, times(1)).unlike(t1);
    }
}
