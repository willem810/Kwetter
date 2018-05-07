package service;

import domain.Credential;
import domain.User;
import exception.UserNotFoundException;
import interceptor.Logging;
import util.AuthController;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;

@Stateless
@Logging
public class AuthService {

    @Inject
    public UserService userService;

    @Inject
    AuthController authController;

    public void authenticate(Credential cred) throws LoginException {
        userService.authenticate(cred);
    }

    public String generateToken(String username) throws Exception {
        User u = userService.findByUsername(username);
        return authController.generateJWT(u);
    }

    public User getLoggedInUser(String bearer) throws UserNotFoundException, Exception {
        String username = authController.getUserFromJWT(bearer);
        return userService.findByUsername(username);
    }
}
