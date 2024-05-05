package hu.inf.szte.adventure.auth;

import java.io.Serializable;
import java.security.Principal;

public record UserAuth(String name) implements Principal, Serializable {

    @Override
    public String getName() {
        return name;
    }
}
