package rest;

import domain.Permissions;
import domain.Tweet;
import domain.User;
import exception.TweetNotFoundException;
import exception.UserNotFoundException;
import interceptor.Authentication;
import interceptor.Secured;
import service.TweetService;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("tweets")
@Stateless
@Authentication(Permissions.tweet_guest)
public class TweetResource {

    @Inject
    private TweetService t;

    @Inject
    private UserService u;

    public void setTweetService(TweetService t) {
        this.t = t;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweets() {
        List<Tweet> tweets = t.getTweets();

        JsonArrayBuilder list = Json.createArrayBuilder();
        tweets.stream().map(Tweet::toJson).forEach(list::add);

        return Response.ok(list.build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweet(@PathParam("id") int id) {
        Tweet tweet = null;
        try {
            tweet = t.findTweetById(new Long(id));
        } catch (TweetNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        return Response.ok(tweet.toJson()).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authentication(Permissions.tweet_basic)
    @Secured({Permissions.tweet_basic})
    public Response addTweet(Tweet tweet) {
        User user = tweet.getUser();
        if (user == null) {
            return Response.status(400).build();
        }

        if (user.getId() == null && user.getUsername() != null) {
            User tweetUser = null;
            try {
                tweetUser = u.findByUsername(user.getUsername());
            } catch (UserNotFoundException e) {
                e.printStackTrace();
                return Response.status(404).build();
            }

            tweet = new Tweet(tweetUser, tweet.getMessage());
        } else if (user.getUsername() == null && user.getId() != null) {
            try {
                return Response.status(501).build();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(404).build();
            }
        }

        t.addTweet(tweet);
        return Response.ok().build();
    }

    @POST
    @Path("/remove/{id}")
    @Authentication(Permissions.tweet_basic)
    @Secured({Permissions.tweet_basic})
    public Response removeTweet(@PathParam("id") int id) {
        Tweet tweet = null;
        try {
            tweet = t.findTweetById(new Long(id));
        } catch (TweetNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        t.removeTweet(tweet);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/like")
    @Authentication(Permissions.tweet_basic)
    @Secured({Permissions.tweet_basic})
    public Response like(@PathParam("id") int id) {
        Tweet tweet = null;
        try {
            tweet = t.findTweetById(new Long(id));
        } catch (TweetNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        t.like(tweet);

        return Response.ok().build();
    }

    @POST
    @Path("/{id}/unlike")
    @Authentication(Permissions.tweet_basic)
    @Secured({Permissions.tweet_basic})
    public Response unlike(@PathParam("id") int id) {
        Tweet tweet = null;
        try {
            tweet = t.findTweetById(new Long(id));
        } catch (TweetNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        t.unlike(tweet);

        return Response.ok().build();
    }
}
