package hu.szte.inf.api;

import hu.szte.inf.core.data.DummyJooqDao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.cfg.ConfigSupport;
import hu.szte.inf.helper.App;
import hu.szte.inf.helper.CookieParser;
import hu.szte.inf.helper.ReInitDbExtension;
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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = {SpringExtension.class, ReInitDbExtension.class})
@SpringBootTest(classes = {App.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        useMainMethod = SpringBootTest.UseMainMethod.WHEN_AVAILABLE)
class ReadApiTest {

    private final TestRestTemplate testRestTemplate;

    @Autowired
    ReadApiTest(TestRestTemplate restTemplate) {
        testRestTemplate = restTemplate;
    }

    @Test
    void sendRead_all() {
        var response = testRestTemplate.exchange("/api/dummy/read",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Dummy[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        var dbElements = StreamSupport.stream(dao.findAll().spliterator(), false).toArray(Dummy[]::new);

        assertThat(response.getBody()).hasSameSizeAs(dao.findAll());
        assertThat(response.getBody())
                .allSatisfy(el -> assertThat(el)
                        .usingRecursiveComparison()
                        .isEqualTo(Arrays.stream(dbElements).filter(pred ->
                                Objects.equals(pred.getId(), el.getId())).findFirst().get()));
    }

    @Test
    void sendRead_filterName() {
        var filterModel = new Dummy();
        filterModel.setSomeString("wa");
        var response = testRestTemplate.exchange("/api/dummy/list?someString={someString}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Dummy[].class,
                filterModel.getSomeString());

        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        var dbElements = StreamSupport.stream(dao.findAllByCrit(filterModel).spliterator(), false).toArray(Dummy[]::new);

        assertThat(response.getBody())
                .allSatisfy(el -> assertThat(el)
                        .usingRecursiveComparison()
                        .isEqualTo(Arrays.stream(dbElements).filter(pred ->
                                Objects.equals(pred.getId(), el.getId())).findFirst().get()));
    }

    @Test
    void sendRead_filterFinished() {
        var filterModel = new Dummy();
        filterModel.setSomeBool(false);
        var response = testRestTemplate.exchange("/api/dummy/read?someBool={someBool}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Dummy[].class,
                filterModel.getSomeBool());

        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        var dbElements = StreamSupport.stream(dao.findAllByCrit(filterModel).spliterator(), false).toArray(Dummy[]::new);

        assertThat(response.getBody())
                .allSatisfy(el -> assertThat(el)
                        .usingRecursiveComparison()
                        .isEqualTo(Arrays.stream(dbElements).filter(pred ->
                                Objects.equals(pred.getId(), el.getId())).findFirst().get()));
    }

    @Test
    void sendRead_filterCredits() {
        var filterModel = new Dummy();
        filterModel.setSomeInt(7);
        var response = testRestTemplate.exchange("/api/dummy/list?someInt={someInt}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Dummy[].class,
                filterModel.getSomeInt());

        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        var dbElements = StreamSupport.stream(dao.findAllByCrit(filterModel).spliterator(), false).toArray(Dummy[]::new);

        assertThat(response.getBody())
                .allSatisfy(el -> assertThat(el)
                        .usingRecursiveComparison()
                        .isEqualTo(Arrays.stream(dbElements).filter(pred ->
                                Objects.equals(pred.getId(), el.getId())).findFirst().get()));
    }

    @Test
    void sendRead_filterMultiple() {
        var filterModel = new Dummy();
        filterModel.setSomeString("wa");
        filterModel.setAnotherString("ak");
        filterModel.setSomeInt(3);
        filterModel.setOtherInt(3);
        var response = testRestTemplate.exchange("/api/dummy/read?someString={someString}&anotherString={anotherString}&someInt={someInt}&otherInt={otherInt}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Dummy[].class,
                filterModel.getSomeString(), filterModel.getAnotherString(), filterModel.getSomeInt(), filterModel.getOtherInt());

        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        var dbElements = StreamSupport.stream(dao.findAllByCrit(filterModel).spliterator(), false).toArray(Dummy[]::new);

        assertThat(response.getBody())
                .allSatisfy(el -> assertThat(el)
                        .usingRecursiveComparison()
                        .isEqualTo(Arrays.stream(dbElements).filter(pred ->
                                Objects.equals(pred.getId(), el.getId())).findFirst().get()));
    }

    @Test
    void sendRead_cookieFound() {
        var filterModel = new Dummy();
        filterModel.setSomeString("ak");
        filterModel.setAnotherString("d");
        var response = testRestTemplate.exchange("/api/dummy/list?someString={someString}&anotherString={anotherString}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Dummy[].class,
                filterModel.getSomeString(), filterModel.getAnotherString());

        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        var dao = new DummyJooqDao(ds, SQLDialect.SQLITE);
        var dbElements = StreamSupport.stream(dao.findAllByCrit(filterModel).spliterator(), false).toArray(Dummy[]::new);


        var cookies = Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).stream().map(CookieParser::parseCookie).toList();

        var cookie1 = cookies.stream().filter(c -> Objects.equals(c.getName(), "full-size")).findFirst().get();
        assertThat(cookie1.getValue()).isEqualTo(Integer.toString(dao.count()));
        var cookie2 = cookies.stream().filter(c -> Objects.equals(c.getName(), "current-size")).findFirst().get();
        assertThat(cookie2.getValue()).isEqualTo(Integer.toString(dbElements.length));
    }

    @Test
    void sendRead_badFilterFinished() {
        String finished = "1";
        var response = testRestTemplate.exchange("/api/dummy/read?someBool={someBool}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Map<String, Object>>() {
                },
                finished);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("err");
    }

    @Test
    void sendRead_badFilterCredits() {
        String credits = "not valid someInt";
        var response = testRestTemplate.exchange("/api/dummy/list?someInt={someInt}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Map<String, Object>>() {
                },
                credits);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("err");
    }
}
