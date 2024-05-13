package hu.szte.inf.api;

import hu.szte.inf.core.data.DummyJooqDao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.cfg.ConfigSupport;
import hu.szte.inf.helper.App;
import hu.szte.inf.helper.ReInitDbExtension;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sqlite.SQLiteDataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = {SpringExtension.class, ReInitDbExtension.class})
@SpringBootTest(classes = {App.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        useMainMethod = SpringBootTest.UseMainMethod.WHEN_AVAILABLE)
class UpdateApiTest {

    private final TestRestTemplate testRestTemplate;

    @Autowired
    UpdateApiTest(TestRestTemplate restTemplate) {
        testRestTemplate = restTemplate;
    }

    @Test
    void sendUpdate() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var reqModel = new Dummy(8L, "New dummy", "DUMDUM", true, 10, 52);
        var response = testRestTemplate.exchange("/api/dummy/update",
                HttpMethod.PUT,
                new HttpEntity<>(reqModel, headers),
                Void.class);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(sizeBefore).isEqualTo(sizeAfter);
        var dbModel = dao.findById(reqModel.getId());
        assertThat(dbModel).isPresent();
        assertThat(dbModel.get()).usingRecursiveComparison().isEqualTo(reqModel);
    }

    @Test
    void sendUpsert() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var reqModel = new Dummy(129L, "My new dummy", "DUM", true, 14, 234);
        var response = testRestTemplate.exchange("/api/dummy/update",
                HttpMethod.PUT,
                new HttpEntity<>(reqModel, headers),
                Void.class);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(sizeBefore + 1).isEqualTo(sizeAfter);
        var dbModel = dao.findById(reqModel.getId());
        assertThat(dbModel).isPresent();
        assertThat(dbModel.get()).usingRecursiveComparison().isEqualTo(reqModel);
    }

    @Test
    void sendUpdate_empty() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();

        var response = testRestTemplate.exchange("/api/dummy/refresh",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Void.class);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(sizeBefore).isEqualTo(sizeAfter);
    }

    @Test
    void sendUpdate_emptyId() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var reqModel = new Dummy(null, "Again?", "TEST", false, 54, 11);
        var response = testRestTemplate.exchange("/api/dummy/refresh",
                HttpMethod.PUT,
                new HttpEntity<>(reqModel, headers),
                Void.class);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(sizeBefore).isEqualTo(sizeAfter);
    }
}
