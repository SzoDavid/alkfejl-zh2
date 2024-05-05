package hu.inf.szte.adventure.auth;

import hu.inf.szte.adventure.data.UserJooqDao;

import javax.security.auth.Subject;
import java.util.Objects;

public class AuthService implements AuthRealm {

    private final UserJooqDao dao;

    public AuthService(UserJooqDao dao) {
        this.dao = dao;
    }

    @Override
    public Subject authenticate(String username, String password) {
        var userInDb = dao.findByUsername(username);

        if (userInDb.isEmpty() || !Objects.equals(userInDb.get().getPassword(), password)) {
            return null;
        }

        var subject = new Subject();
        // Based on your needs you can always add more principals,
        // or customize this step further.
        subject.getPrincipals().add(new UserAuth(username));
        subject.setReadOnly();
        return subject;
    }
}
