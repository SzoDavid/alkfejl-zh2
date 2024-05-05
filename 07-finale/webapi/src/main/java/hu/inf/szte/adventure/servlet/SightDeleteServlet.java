package hu.inf.szte.adventure.servlet;

import com.google.gson.Gson;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.data.SightJooqDao;
import hu.inf.szte.adventure.model.*;
import hu.inf.szte.adventure.model.Error;
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
import java.math.BigDecimal;

@WebServlet(urlPatterns = {"/api/sight/remove", "/api/sight/delete"})
public class SightDeleteServlet extends HttpServlet {

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
        var reqModel = gson.fromJson(req.getReader(), DeleteSightReq.class);
        if (reqModel == null) {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(new Error("request body should not be empty")));
            return;
        }

        var affectedRows = dao.deleteById(reqModel.id());
        resp.getWriter().println(gson.toJson(new ModifiedRows(affectedRows)));
    }
}
