package interceptor;

import domain.Group;
import domain.Permissions;
import io.jsonwebtoken.*;
import service.GroupService;
import util.JWTStore;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String REALM = "realm";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    Logger log = Logger.getLogger(this.getClass().getName());

    @Inject
    GroupService gService;


    @Context
    ResourceInfo resourceInfo;

    // Generating a random key. This should be a static key found in this application
//    private final byte[] key = JWTStore.getKey();


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim();

        try {

            // Validate the token
            Jws<Claims> claims = validateToken(token);
            ArrayList userGroups = getUserGroups(claims);
            Permissions[] perms = getPermissionsNeeded(requestContext);

            authorize(userGroups, perms);

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE,
                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
    }

    private Jws<Claims> validateToken(String token) throws Exception {
        try {
            byte[] key = JWTStore.getKey();
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            log.log(Level.INFO, "AUTHENTICATION: Method: validateToken - valid token : " + token);

            return claims;
        } catch (Exception e) {
            log.log(Level.WARNING, "AUTHENTICATION: Method: validateToken - invalid token : " + token, e);
            throw new Exception("token not valid!", e);
        }
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
    }

    private ArrayList getUserGroups(Jws<Claims> claims) {
        return claims.getBody().get("groups", ArrayList.class);
    }

    private void authorize(ArrayList groups, Permissions[] perms) throws Exception {
        if (perms.length == 1 && perms[0] == Permissions.none) {
            return;
        }

        boolean isAuthorized = true;

        ArrayList<Group> userGroups = new ArrayList<>();

        for (Object g : groups) {
            userGroups.add(gService.findByName((String) g));
        }


        for (Permissions p : perms) {
            boolean permissionFound = false;

            for (Group g : userGroups) {
                if (g.hasPermission(p)) {
                    permissionFound = true;
                    break;
                }
            }

            if (!permissionFound) {
                isAuthorized = false;
                break;
            }
        }

        if (!isAuthorized) {
            throw new Exception("User not authorized for this invocation");
        }
    }

    public Permissions[] getPermissionsNeeded(ContainerRequestContext context) throws Exception {
        // First check if there's a permissions required on method level.
        Secured auth = resourceInfo.getResourceMethod().getAnnotation(Secured.class);

        // If there's no authentication required on method level, check class level.
        if (auth == null) {
            auth = resourceInfo.getResourceClass().getAnnotation(Secured.class);
        }

        // Else, there's no permission required, thus we chan continue;
        if (auth == null) {
            log.log(Level.INFO, "AUTHENTICATION: Method: " + context.getMethod() + ", no permission required");
            return new Permissions[0];
        }

        return auth.value();
    }
}
