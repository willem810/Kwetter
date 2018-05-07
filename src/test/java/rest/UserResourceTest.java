package rest;

import domain.Tweet;
import domain.User;
import exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import service.UserService;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {
    UserResource uResource = new UserResource();

    @Mock
    UserService uService;


    @Before
    public void setUp() {
        uResource.setUserService(uService);
    }


    @Test
    public void getUsers() {
        Response res = uResource.getUsers();

        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).getUsers();
    }

    @Test
    public void getUser() throws UserNotFoundException {
        User u1 = new User("username", "user name", "", "");

        when(uService.findByUsername(u1.getUsername())).thenReturn(u1);
        when(uService.findByUsername("fake username")).thenThrow(UserNotFoundException.class);

        Response res = uResource.getUser(u1.getUsername());
        String json = res.getEntity().toString();
        Assert.assertEquals(u1.toJson().toString(), json);
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).findByUsername(u1.getUsername());


        res = uResource.getUser("fake username");
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(uService, times(2)).findByUsername(any());
    }

    @Test
    public void addUser() {
        User u1 = new User();
        Response res = uResource.addUser(u1);

        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).addUser(u1);
    }

    @Test
    public void removeUser() throws UserNotFoundException {
        User u1 = new User("username", "user name", "", "");

        when(uService.findByUsername(u1.getUsername())).thenReturn(u1);
        when(uService.findByUsername("fake username")).thenThrow(UserNotFoundException.class);

        Response res = uResource.removeUser(u1.getUsername());
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).findByUsername(u1.getUsername());
        Mockito.verify(uService, times(1)).removeUser(u1);

        res = uResource.removeUser("fake username");
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(uService, times(2)).findByUsername(any());
        Mockito.verify(uService, times(1)).removeUser(u1);
    }

    @Test
    public void getTweets() throws UserNotFoundException {
        User u1 = new User("username", "user name", "", "");
        ArrayList<Tweet> u1Tweets = new ArrayList<>();

        when(uService.getTweets(u1.getUsername())).thenReturn(u1Tweets);

        Response res = uResource.getTweets(u1.getUsername(), 0);
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).getTweets(u1.getUsername());
        Mockito.verify(uService, Mockito.never()).getRecentTweets(anyString(), anyInt());


        res = uResource.getTweets(u1.getUsername(), 1);
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).getTweets(u1.getUsername());
        Mockito.verify(uService, times(1)).getRecentTweets(u1.getUsername(), 1);
    }

    @Test
    public void tweet() throws UserNotFoundException {
        User u1 = new User("username", "user name", "", "");
        String expMessage = "message";

        when(uService.findByUsername(u1.getUsername())).thenReturn(u1);
        when(uService.findByUsername("fake username")).thenThrow(UserNotFoundException.class);

        Response res = uResource.Tweet(u1.getUsername(), expMessage);
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).Tweet(u1, expMessage);

        res = uResource.Tweet(u1.getUsername(), "");
        Assert.assertEquals(400, res.getStatus());
        Mockito.verify(uService, times(1)).Tweet(u1, expMessage);

        res = uResource.Tweet("fake username", expMessage);
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(uService, times(1)).Tweet(u1, expMessage);
    }

    @Test
    public void follow() throws UserNotFoundException {
        User u1 = new User("username 1", "user name 1", "", "");
        User u2 = new User("username 2", "user name 2", "", "");

        when(uService.findByUsername(u1.getUsername())).thenReturn(u1);
        when(uService.findByUsername(u2.getUsername())).thenReturn(u2);
        when(uService.findByUsername("fake username")).thenThrow(UserNotFoundException.class);

        Response res = uResource.Follow(u1.getUsername(), u2.getUsername());
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).followUser(u1, u2);

        res = uResource.Follow("fake username", u2.getUsername());
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(uService, times(1)).followUser(u1, u2);

        res = uResource.Follow(u1.getUsername(), "fake username");
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(uService, times(1)).followUser(u1, u2);
    }

    @Test
    public void unfollow() throws UserNotFoundException {
        User u1 = new User("username 1", "user name 1", "", "");
        User u2 = new User("username 2", "user name 2", "", "");

        when(uService.findByUsername(u1.getUsername())).thenReturn(u1);
        when(uService.findByUsername(u2.getUsername())).thenReturn(u2);
        when(uService.findByUsername("fake username")).thenThrow(UserNotFoundException.class);

        Response res = uResource.Unfollow(u1.getUsername(), u2.getUsername());
        Assert.assertEquals(200, res.getStatus());
        Mockito.verify(uService, times(1)).unFollowUser(u1, u2);

        res = uResource.Unfollow("fake username", u2.getUsername());
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(uService, times(1)).unFollowUser(u1, u2);

        res = uResource.Unfollow(u1.getUsername(), "fake username");
        Assert.assertEquals(404, res.getStatus());
        Mockito.verify(uService, times(1)).unFollowUser(u1, u2);
    }
}
