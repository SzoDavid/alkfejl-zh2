package hu.inf.szte.adventure.servlet;

import com.google.gson.Gson;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.data.SightJooqDao;
import hu.inf.szte.adventure.model.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.jooq.SQLDialect;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.Error;
import java.math.BigDecimal;

@WebServlet(urlPatterns = {"/api/sight/refresh", "/api/sight/update"})
public class SightUpdateServlet extends HttpServlet {

    private Dao<Long, Sight> dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = new SightJooqDao(
                (DataSource) config.getServletContext().getAttribute("ds"),
                SQLDialect.SQLITE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var gson = new Gson();
        var reqModel = gson.fromJson(req.getReader(), UpdateSightReq.class);
        // Do some gruntwork... Boring validations

        if (reqModel.getName() == null || reqModel.getName().isBlank()) {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(new Error("attribute `name` should not be null, empty, or blank")));
            return;
        }
        if (reqModel.getPrice() == null || reqModel.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(new Error("attribute `price` should not be null, or less than zero")));
            return;
        }

        var model = new Sight(
                null,
                reqModel.getName(),
                reqModel.getPrice(),
                reqModel.getOpeningHour(),
                reqModel.getClosingHour(),
                reqModel.getDescription(),
                reqModel.getPopularity());
        var affectedRows = dao.updateById(reqModel.getId(), model);
        resp.getWriter().println(gson.toJson(new ModifiedRows(affectedRows)));
    }
}
