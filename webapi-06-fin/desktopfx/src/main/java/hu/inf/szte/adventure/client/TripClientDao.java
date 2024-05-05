package hu.inf.szte.adventure.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.InsertId;
import hu.inf.szte.adventure.model.ModifiedRows;
import hu.inf.szte.adventure.model.Trip;
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

public class TripClientDao implements Dao<Long, Trip> {

    private final Gson gson;
    private final HttpClient client;

    public TripClientDao(HttpClient client) {
        gson = new Gson();
        this.client = client;
    }

    @Override
    public void save(@NonNull Trip model) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.TRIP_ADD_ENDPOINT)))
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
    public Optional<Trip> findById(@NonNull Long id) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.TRIP_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Trip(id, null, null, null, null, null))));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            var maybe = (List<?>) gson.fromJson(resp.body(), new TypeToken<List<Trip>>() {
            }.getType());
            if (maybe.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of((Trip) maybe.get(0));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Trip> findAll() {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.TRIP_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.noBody());
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return gson.fromJson(resp.body(), new TypeToken<List<Trip>>() {
            }.getType());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Trip> findAllByIds(@NonNull Iterable<Long> ids) {
        // You should provide backend functionality for it. Have at it, if you will!
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Trip> findAllByCrit(@NonNull Trip model) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.TRIP_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(model)));
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return gson.fromJson(resp.body(), new TypeToken<List<Trip>>() {
            }.getType());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(@NonNull Long id) {
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.TRIP_DELETE_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Trip(id, null, null, null, null, null))));
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
    public int updateById(@NonNull Long id, Trip model) {
        model.setId(id);
        var req = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.TRIP_UPDATE_ENDPOINT)))
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
    public int updateAllByIds(@NonNull Iterable<Long> ids, @NonNull Trip model) {
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
                .uri(URI.create(ConfigSupport.getProperty(ApiEP.TRIP_LIST_ENDPOINT)))
                .setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .POST(HttpRequest.BodyPublishers.noBody());
        var body = HttpResponse.BodyHandlers.ofString();
        try {
            var resp = client.send(req.build(), body);
            // TODO: more robust logging and/or exception handling
            if (resp.statusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException(resp.body());
            }
            return ((List<?>) gson.fromJson(resp.body(), new TypeToken<List<Trip>>() {
            }.getType())).size();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
