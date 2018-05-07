package rest;

import domain.Credential;
import domain.Group;
import domain.Permissions;
import domain.User;
import exception.UserNotFoundException;
import interceptor.Logging;
import interceptor.Secured;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import service.AuthService;
import service.UserService;
import util.JWTStore;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("auth")
@Stateless
public class AuthResource {

    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    // Generating a random key. This should be a static key found in this application
//    private final Key key = JWTStore.getKey();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credential credentials) {
        try {

            // Authenticate the user using the credentials provided
            authService.authenticate(credentials);

            // Issue a token for the user
            String token = authService.generateToken(credentials.getUsername());

            // Return the token on the response
            return Response.ok()
                    .header(AUTHORIZATION, "Bearer " + token)
                    .build();

        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

    @POST
    @Secured({Permissions.group_admin, Permissions.group_moderator})
    @Path("/isauthenticated")
    public Response isAuthenticated() {
        return Response.ok().build();
    }

    @GET
    @Path("/loggedinuser")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response loggedinuser(@Context HttpHeaders headers) {
        try {
            User u = authService.getLoggedInUser(headers.getHeaderString("Authorization"));

            return Response.ok(u.toJson()).build();
        } catch (UserNotFoundException e) {
            return Response.status(404).build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

}
