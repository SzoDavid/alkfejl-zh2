package hu.inf.szte.servlet;

import com.google.gson.Gson;
import hu.inf.szte.core.data.Dao;
import hu.inf.szte.core.data.StockJooqDao;
import hu.inf.szte.core.model.Stock;
import hu.inf.szte.model.ModifiedRows;
import hu.inf.szte.model.UpdateStockReq;
import hu.inf.szte.model.Error;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jooq.SQLDialect;

import javax.sql.DataSource;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/stock/update", "/api/stock/refresh"})
public class StockUpdateServlet extends HttpServlet {
    private Dao<Long, Stock> dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = new StockJooqDao((DataSource) config.getServletContext().getAttribute("ds"), SQLDialect.SQLITE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        var gson = new Gson();
        var reqModel = gson.fromJson(req.getReader(), UpdateStockReq.class);

        if (reqModel.getId() == null) {
            resp.setStatus(400);
            resp.getWriter().println(gson.toJson(new Error("attribute id")));
            return;
        }
        if (reqModel.getName() == null || reqModel.getName().isBlank()) {
            resp.setStatus(400);
            resp.getWriter().println(gson.toJson(new Error("attribute name")));
            return;
        }
        if (reqModel.getSimpleName() == null || reqModel.getSimpleName().isBlank()) {
            resp.setStatus(400);
            resp.getWriter().println(gson.toJson(new Error("attribute simple name")));
            return;
        }
        if (reqModel.getPrice() == null || reqModel.getPrice() <= 0) {
            resp.setStatus(400);
            resp.getWriter().println(gson.toJson(new Error("attribute price")));
            return;
        }
        if (reqModel.getShares() == null || reqModel.getShares() <= 0) {
            resp.setStatus(400);
            resp.getWriter().println(gson.toJson(new Error("attribute shares")));
            return;
        }

        var model = new Stock(
                null,
                reqModel.getName(),
                reqModel.getSimpleName(),
                reqModel.getPrice(),
                reqModel.getShares());

        var affectedRows = dao.updateById(reqModel.getId(), model);
        resp.setStatus(200);
        resp.getWriter().println(gson.toJson(new ModifiedRows(affectedRows)));
    }
}
