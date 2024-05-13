package hu.szte.inf.helper;

import jakarta.servlet.http.Cookie;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;

public class CookieParser {

    public static Cookie parseCookie(String headerCookie) {
        var cookieParts = Arrays.stream(headerCookie.split(";")).map(String::trim).toList();
        var nameValue = cookieParts.get(0).split("=");
        var cookie = new Cookie(nameValue[0], nameValue[1]);
        var attrMap = new HashMap<String, String>();
        for (int i = 1; i < cookieParts.size(); ++i) {
            var nv = cookieParts.get(i).split("=");
            if (nv.length > 1) {
                attrMap.put(nv[0], nv[1]);
            } else {
                attrMap.put(nv[0], null);
            }
        }
        if (attrMap.containsKey("Domain")) {
            cookie.setPath(attrMap.get("Domain"));
        }
        if (attrMap.containsKey("Expires")) {
            var expire = LocalDateTime.parse(attrMap.get("Expires"), DateTimeFormatter.RFC_1123_DATE_TIME);
            var now = LocalDateTime.now(Clock.systemUTC());
            long seconds = ChronoUnit.SECONDS.between(now, expire);
            cookie.setMaxAge((int) seconds);
        }
        if (attrMap.containsKey("HttpOnly")) {
            cookie.setHttpOnly(true);
        }
        if (attrMap.containsKey("Max-Age")) {
            cookie.setPath(attrMap.get("Path"));
        }
        if (attrMap.containsKey("Path")) {
            cookie.setPath(attrMap.get("Path"));
        }
        if (attrMap.containsKey("Secure")) {
            cookie.setSecure(true);
        }
        return cookie;
    }
}
