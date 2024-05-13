package hu.szte.inf.api;

import hu.szte.inf.core.data.DummyJooqDao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.cfg.ConfigSupport;
import hu.szte.inf.helper.*;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sqlite.SQLiteDataSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = {SpringExtension.class, ReInitDbExtension.class})
@SpringBootTest(classes = {App.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        useMainMethod = SpringBootTest.UseMainMethod.WHEN_AVAILABLE)
class CreateApiTest {

    private final TestRestTemplate testRestTemplate;

    @Autowired
    CreateApiTest(TestRestTemplate restTemplate) {
        testRestTemplate = restTemplate;
    }

    @Test
    void sendCreate() {
        var reqModel = new Dummy(null, "New dummy", "dumdum", false, 3, 9);
        var response = testRestTemplate.exchange("/api/dummy/create",
                HttpMethod.POST,
                new HttpEntity<>(reqModel),
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);

        assertThat(response.getBody()).isNotNull();
        var id = response.getBody().get("id");
        assertThat(id).isInstanceOf(Number.class);

        var dbModel = dao.findById(((Number) id).longValue());
        assertThat(dbModel).isPresent();
        reqModel.setId(dbModel.get().getId());
        assertThat(dbModel.get()).usingRecursiveComparison().isEqualTo(reqModel);
    }

    @Test
    void sendCreate_empty() {
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        int sizeBefore = dao.count();

        var response = testRestTemplate.exchange("/api/dummy/insert",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        int sizeAfter = dao.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(sizeBefore).isEqualTo(sizeAfter);
    }
}
