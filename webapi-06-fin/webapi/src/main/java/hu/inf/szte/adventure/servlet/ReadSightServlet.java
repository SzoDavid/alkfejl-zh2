package hu.inf.szte.adventure.servlet;

import com.google.gson.Gson;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.data.SightJooqDao;
import hu.inf.szte.adventure.model.Sight;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;
import org.jooq.SQLDialect;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.stream.StreamSupport;

@WebServlet(urlPatterns = {"/api/sight/list", "/api/sight/read"})
public class ReadSightServlet extends HttpServlet {

    private Dao<Long, Sight> dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = new SightJooqDao(
                (DataSource) config.getServletContext().getAttribute("ds"),
                SQLDialect.SQLITE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ContentType.APPLICATION_JSON.getMimeType()); // application/json
        resp.setStatus(HttpServletResponse.SC_OK); // 200

        var models = StreamSupport.stream(dao.findAll().spliterator(), false).toList();
        var respBody = new Gson().toJson(models);

        resp.getWriter().println(respBody);
    }
}
