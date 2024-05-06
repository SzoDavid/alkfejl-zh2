package hu.inf.szte.servlet;

import com.google.gson.Gson;
import hu.inf.szte.core.data.Dao;
import hu.inf.szte.core.data.StockJooqDao;
import hu.inf.szte.core.model.Stock;
import hu.inf.szte.model.Error;
import hu.inf.szte.model.ReadStockReq;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jooq.SQLDialect;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@WebServlet(urlPatterns = {"/api/stock/read", "/api/stock/list"})
public class StockReadServlet extends HttpServlet {
    private Dao<Long, Stock> dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = new StockJooqDao((DataSource) config.getServletContext().getAttribute("ds"), SQLDialect.SQLITE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        var gson = new Gson();

        //var cookies = req.getCookies();
        //var prefCookie = Arrays.stream(cookies).filter();

        Stock dbFilter = null;
        if (req.getReader() != null) {
            var filterModel = new Gson().fromJson(req.getReader(), ReadStockReq.class);

            if (filterModel != null) {

                if (filterModel.getId() != null && filterModel.getId() <= 0) {
                    resp.setStatus(400);
                    resp.getWriter().println(gson.toJson(new Error("attribute id")));
                    return;
                }
                if (filterModel.getName() != null && filterModel.getName().isBlank()) {
                    resp.setStatus(400);
                    resp.getWriter().println(gson.toJson(new Error("attribute name")));
                    return;
                }
                if (filterModel.getSimpleName() != null && filterModel.getSimpleName().isBlank()) {
                    resp.setStatus(400);
                    resp.getWriter().println(gson.toJson(new Error("attribute simple name")));
                    return;
                }
                if (filterModel.getPrice() != null && filterModel.getPrice() <= 0) {
                    resp.setStatus(400);
                    resp.getWriter().println(gson.toJson(new Error("attribute price")));
                    return;
                }
                if (filterModel.getShares() != null && filterModel.getShares() <= 0) {
                    resp.setStatus(400);
                    resp.getWriter().println(gson.toJson(new Error("attribute shares")));
                    return;
                }

                dbFilter = new Stock(
                        filterModel.getId(),
                        filterModel.getName(),
                        filterModel.getSimpleName(),
                        filterModel.getPrice(),
                        filterModel.getShares()
                );
            }
        }

        List<Stock> models;
        if (dbFilter != null) {
            models = StreamSupport.stream(dao.findAllByCrit(dbFilter).spliterator(), false).toList();
        } else {
            models = StreamSupport.stream(dao.findAll().spliterator(), false).toList();
        }

        var json = new Gson().toJson(models);
        resp.getWriter().println(json);
    }
}
