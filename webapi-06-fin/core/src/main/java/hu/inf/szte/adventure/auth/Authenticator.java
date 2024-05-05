package hu.inf.szte.adventure.auth;

import hu.inf.szte.adventure.exception.UnauthorizedException;

public interface Authenticator {

    void login(String username, String password);

    void logout();

    void authorize() throws UnauthorizedException;
}
