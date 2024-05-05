package hu.inf.szte.adventure.auth;

import javax.security.auth.Subject;

public interface AuthRealm {

    Subject authenticate(String username, String password);
}
