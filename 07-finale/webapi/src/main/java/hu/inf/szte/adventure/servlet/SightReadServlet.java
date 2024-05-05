package hu.inf.szte.adventure.servlet;

import com.google.gson.Gson;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.data.SightJooqDao;
import hu.inf.szte.adventure.model.ReadSightReq;
import hu.inf.szte.adventure.model.Sight;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.entity.ContentType;
import org.jooq.SQLDialect;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@WebServlet(urlPatterns = {"/api/sight/list", "/api/sight/read"})
public class SightReadServlet extends HttpServlet {

    private Dao<Long, Sight> dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = new SightJooqDao(
                (DataSource) config.getServletContext().getAttribute("ds"),
                SQLDialect.SQLITE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());  // application/json
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());  // UTF-8
        resp.setStatus(HttpServletResponse.SC_OK);  // 200

        var cookies = req.getCookies();
        var prefCookie = Arrays.stream(cookies)
                .filter(c -> Objects.equals(c.getName(), "preference")).findFirst().orElse(null);

        ReadSightReq pref = null;
        if (prefCookie != null) {

            var decodedCookie = Base64.getDecoder().decode(prefCookie.getValue());
            pref = SerializationUtils.deserialize(decodedCookie);
        }

        Sight dbFilter = null;
        if (req.getReader() != null) {
            var filterModel = new Gson().fromJson(req.getReader(), ReadSightReq.class);
            if (filterModel != null) {
                dbFilter = new Sight(filterModel.getId(), filterModel.getName(), filterModel.getPrice(), filterModel.getOpeningHour(), filterModel.getClosingHour(), filterModel.getDescription(), filterModel.getPopularity());
            }
        }
        List<Sight> models;
        if (dbFilter != null) {
            models = StreamSupport.stream(dao.findAllByCrit(dbFilter).spliterator(), false).toList();
        }
        else {
            models = StreamSupport.stream(dao.findAll().spliterator(), false).toList();
        }

        var json = new Gson().toJson(models);
        resp.getWriter().println(json);
    }
}
