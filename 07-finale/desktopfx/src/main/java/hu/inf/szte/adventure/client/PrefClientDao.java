package hu.inf.szte.adventure.client;

import com.google.gson.Gson;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.Preferences;
import hu.inf.szte.adventure.util.cfg.ConfigSupport;
import lombok.NonNull;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class PrefClientDao implements Dao<Object, Preferences> {

    private final HttpClient client;

    public PrefClientDao(HttpClient client) {
        this.client = client;
    }

    @Override
    public void save(@NonNull Preferences model) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.PREFERENCES_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(model)));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (!(resp.statusCode() >= 200 && resp.statusCode() <= 300)) {
                throw new RuntimeException("Status: %d".formatted(resp.statusCode()) + " " + resp.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Preferences> findById(@NonNull Object o) {
        return Optional.empty();
    }

    @Override
    public Iterable<Preferences> findAll() {
        return null;
    }

    @Override
    public Iterable<Preferences> findAllByIds(@NonNull Iterable<Object> objects) {
        return null;
    }

    @Override
    public Iterable<Preferences> findAllByCrit(@NonNull Preferences model) {
        return null;
    }

    @Override
    public int deleteById(@NonNull Object o) {
        return 0;
    }

    @Override
    public int deleteAllByIds(@NonNull Iterable<Object> objects) {
        return 0;
    }

    @Override
    public int updateById(@NonNull Object o, Preferences model) {
        return 0;
    }

    @Override
    public int updateAllByIds(@NonNull Iterable<Object> objects, @NonNull Preferences model) {
        return 0;
    }

    @Override
    public int prune() {
        // It is also possible to construct a whole other
        // backend api endpoint for cookie removal.
        // E.g. /api/preferences/reset
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.PREFERENCES_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Preferences(null, null, null))));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (!(resp.statusCode() >= 200 && resp.statusCode() <= 300)) {
                throw new RuntimeException("Status: %d".formatted(resp.statusCode()) + " " + resp.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Override
    public int count() {
        return 0;
    }
}
