package rest;

import domain.Group;
import domain.Permissions;
import domain.Tweet;
import domain.User;
import exception.GroupNotFoundException;
import exception.UserNotFoundException;
import interceptor.Authentication;
import interceptor.Secured;
import service.AuthService;
import service.GroupService;
import service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("users")
@Stateless
@Authentication({Permissions.user_guest})
public class UserResource {

    private String loggedInUser;

    @Inject
    private UserService u;

    @Inject
    private GroupService rService;

    public void setUserService(UserService u) {
        this.u = u;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<User> all = this.u.getUsers();
        all.stream().map(User::toJson).forEach(list::add);

        return Response.ok(list.build()).build();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authentication(Permissions.user_moderator)
    public Response getUser(@PathParam("name") String name) {
        User user = null;

        try {
            user = u.findByUsername(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        return Response.ok(user.toJson()).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        u.addUser(user);
        return Response.ok().build();
    }

    @POST
    @Path("/remove/{name}")
    @Authentication(Permissions.user_basic)
    public Response removeUser(@PathParam("name") String name) {
        User user = null;

        try {
            user = u.findByUsername(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        u.removeUser(user);
        return Response.ok().build();
    }

    @GET
    @Path("/{name}/tweets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweets(@PathParam("name") String username, @QueryParam("count") int i) {
        List<Tweet> tweets = new ArrayList<>();

        try {
            if (i > 0) {
                tweets = u.getRecentTweets(username, i);
            } else {
                tweets = u.getTweets(username);
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        JsonArrayBuilder list = Json.createArrayBuilder();
        tweets.stream().map(Tweet::toJson).forEach(list::add);
        return Response.ok(list.build()).build();
    }

    @GET
    @Path("/{name}/tweets/count")
    public Response getTweetCount(@PathParam("name") String username) {
        long count = 0;

        try {
            count = u.getTweetCount(username);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        return Response.ok(count).build();
    }


    @POST
    @Path("/{name}/tweet")
    @Authentication(Permissions.user_basic)
    @Secured({Permissions.user_basic})
    public Response Tweet(@PathParam("name") String name, @QueryParam("message") String message) {
        if (message == null || message.equals("")) {
            return Response.status(400).build();
        }

        User user = null;

        try {
            user = u.findByUsername(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        u.Tweet(user, message);
        return Response.ok().build();
    }

    @POST
    @Path("/{name}/follow")
    @Authentication(Permissions.user_basic)
    @Secured({Permissions.user_basic})
    public Response Follow(@PathParam("name") String name, @QueryParam("u") String userToFollow) {
        User user = null;
        User toFollow = null;

        try {
            user = u.findByUsername(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        try {
            toFollow = u.findByUsername(userToFollow);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        u.followUser(user, toFollow);

        return Response.ok().build();
    }

    @POST
    @Path("/{name}/unfollow")
    @Authentication(Permissions.user_basic)
    @Secured({Permissions.user_basic})
    public Response Unfollow(@PathParam("name") String name, @QueryParam("u") String userToUnfollow) {
        User user = null;
        User toUnfollow = null;

        try {
            user = u.findByUsername(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        try {
            toUnfollow = u.findByUsername(userToUnfollow);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        u.unFollowUser(user, toUnfollow);

        return Response.ok().build();
    }

    @Path("/{name}/addGroup")
    @POST
    public Response addGroup(@PathParam("name") String name, @QueryParam("group") String groupName) {
        User user = null;

        try {
            user = u.findByUsername(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        Group g = null;

        try {
            g = rService.findByName(groupName);
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        u.addGroup(user, g);
        return Response.ok().build();
    }

    @Path("/{name}/removeGroup")
    @POST
    public Response removeGroup(@PathParam("name") String name, @QueryParam("group") String groupName) {
        User user = null;

        try {
            user = u.findByUsername(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        Group g = null;

        try {
            g = rService.findByName(groupName);
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        u.removeGroup(user, g);
        return Response.ok().build();
    }

    @Path("/{name}/feed")
    @GET
    @Secured({Permissions.user_basic})
    public Response getFeed(@PathParam("name") String username) {
        List<Tweet> tweets = new ArrayList<>();

        try {
            tweets = u.getFeed(username);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        JsonArrayBuilder list = Json.createArrayBuilder();
        tweets.stream().map(Tweet::toJson).forEach(list::add);
        return Response.ok(list.build()).build();
    }


    @Path("/search/{query}/{amount}")
    @GET
    public Response searchUsers(@PathParam("query") String query, @PathParam("amount") int amount) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<User> all = this.u.searchUser(query, amount);
        all.stream().map(User::toJson).forEach(list::add);

        return Response.ok(list.build()).build();
    }
}
