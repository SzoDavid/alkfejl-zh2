package hu.inf.szte.adventure.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.InsertId;
import hu.inf.szte.adventure.model.ModifiedRows;
import hu.inf.szte.adventure.model.Sight;
import hu.inf.szte.adventure.util.cfg.ConfigSupport;
import lombok.NonNull;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class SightClientDao implements Dao<Long, Sight> {

    private final Gson gson;
    private final HttpClient client;

    public SightClientDao(HttpClient client) {
        gson = new Gson();
        this.client = client;
    }

    @Override
    public void save(@NonNull Sight model) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.SIGHT_ADD_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(model)));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_CREATED) {
                throw new RuntimeException(resp.body());
            }
            model.setId(((Number) gson.fromJson(resp.body(), InsertId.class).id()).longValue());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Sight> findById(@NonNull Long id) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.SIGHT_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Sight(id, null, null, null, null, null, null))));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            var maybe = (List<?>) gson.fromJson(resp.body(), new TypeToken<List<Sight>>() {
            }.getType());
            if (maybe.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of((Sight) maybe.get(0));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Sight> findAll() {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.SIGHT_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.noBody());
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return gson.fromJson(resp.body(), new TypeToken<List<Sight>>() {
            }.getType());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Sight> findAllByIds(@NonNull Iterable<Long> ids) {
        // You should provide backend functionality for it. Have at it, if you will!
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Sight> findAllByCrit(@NonNull Sight model) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.SIGHT_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(model)));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return gson.fromJson(resp.body(), new TypeToken<List<Sight>>() {
            }.getType());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(@NonNull Long id) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.SIGHT_DELETE_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Sight(id, null, null, null, null, null, null))));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return gson.fromJson(resp.body(), ModifiedRows.class).affectedRows();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteAllByIds(@NonNull Iterable<Long> ids) {
        // You should provide backend functionality for it. Have at it, if you will!
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateById(@NonNull Long id, Sight model) {
        model.setId(id);
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.SIGHT_UPDATE_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(model)));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return gson.fromJson(resp.body(), ModifiedRows.class).affectedRows();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateAllByIds(@NonNull Iterable<Long> ids, @NonNull Sight model) {
        // You should provide backend functionality for it. Have at it, if you will!
        throw new UnsupportedOperationException();
    }

    @Override
    public int prune() {
        // You should provide backend functionality for it. Have at it, if you will!
        throw new UnsupportedOperationException();
    }

    @Override
    public int count() {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.SIGHT_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.noBody());
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return ((List<?>) gson.fromJson(resp.body(), new TypeToken<List<Sight>>() {
            }.getType())).size();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
