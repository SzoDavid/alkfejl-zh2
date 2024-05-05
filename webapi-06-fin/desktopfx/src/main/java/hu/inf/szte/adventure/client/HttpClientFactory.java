package hu.inf.szte.adventure.client;

import java.net.*;
import java.net.http.HttpClient;

public class HttpClientFactory {

    public static HttpClient create() {
        var builder = HttpClient.newBuilder();
        // default in-memory cookie store
        // default policy: ACCEPT_ORIGINAL_SERVER
        // - meaning that no cookies outside the original host is allowed
        var cookieManager = new CookieManager();
        builder.cookieHandler(cookieManager);

        return builder.build();
    }
}
