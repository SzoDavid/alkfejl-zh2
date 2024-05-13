package hu.szte.inf.api;

import hu.szte.inf.core.data.DummyJooqDao;
import hu.szte.inf.core.util.cfg.ConfigSupport;
import hu.szte.inf.helper.App;
import hu.szte.inf.helper.ReInitDbExtension;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sqlite.SQLiteDataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = {SpringExtension.class, ReInitDbExtension.class})
@SpringBootTest(classes = {App.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        useMainMethod = SpringBootTest.UseMainMethod.WHEN_AVAILABLE)
class DeleteApiTest {

    private final TestRestTemplate testRestTemplate;

    @Autowired
    DeleteApiTest(TestRestTemplate restTemplate) {
        testRestTemplate = restTemplate;
    }

    @Test
    void sendDelete() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();
        Long deleteId = 8L;

        var response = testRestTemplate.exchange("/api/dummy/delete?id={id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                deleteId);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(sizeBefore - 1).isEqualTo(sizeAfter);
        var dbModel = dao.findById(deleteId);
        assertThat(dbModel).isEmpty();
    }

    @Test
    void sendDelete_nonExisting() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();
        Long deleteId = 956L;

        var response = testRestTemplate.exchange("/api/dummy/delete?id={id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                deleteId);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(sizeBefore).isEqualTo(sizeAfter);
        var dbModel = dao.findById(deleteId);
        assertThat(dbModel).isEmpty();
    }

    @Test
    void sendDelete_empty() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();

        var response = testRestTemplate.exchange("/api/dummy/remove",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(sizeBefore).isEqualTo(sizeAfter);
    }

    @Test
    void sendDelete_emptyId() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();
        Long deleteId = null;

        var response = testRestTemplate.exchange("/api/dummy/remove?id={id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                deleteId);

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(sizeBefore).isEqualTo(sizeAfter);
    }
}
