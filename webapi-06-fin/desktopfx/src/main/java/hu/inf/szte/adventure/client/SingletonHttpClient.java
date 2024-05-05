package hu.inf.szte.adventure.client;

import java.net.http.HttpClient;

public class SingletonHttpClient {

    private final HttpClient client;

    private SingletonHttpClient() {
        client = HttpClientFactory.create();
    }

    public static SingletonHttpClient getInstance() {
        return Instance.INSTANCE;
    }

    public HttpClient getClient() {
        return client;
    }

    private static final class Instance {

        private static final SingletonHttpClient INSTANCE = new SingletonHttpClient();
    }
}
