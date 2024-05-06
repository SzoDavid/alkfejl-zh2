package hu.inf.szte.servlet;

import com.google.gson.Gson;
import hu.inf.szte.core.data.Dao;
import hu.inf.szte.core.data.StockJooqDao;
import hu.inf.szte.core.model.Stock;
import hu.inf.szte.model.CreateStockReq;
import hu.inf.szte.model.InsertedId;
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
import java.math.BigDecimal;

@WebServlet(urlPatterns = {"/api/stock/insert", "/api/stock/new", "/api/stock/add"})
public class StockCreateServlet extends HttpServlet {
    private Dao<Long, Stock> dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = new StockJooqDao((DataSource) config.getServletContext().getAttribute("ds"), SQLDialect.SQLITE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(201);

        var gson = new Gson();
        var reqModel = gson.fromJson(req.getReader(), CreateStockReq.class);

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

        var stock = new Stock(
                null,
                reqModel.getName(),
                reqModel.getSimpleName(),
                reqModel.getPrice(),
                reqModel.getShares()
        );

        dao.save(stock);

        resp.getWriter().println(gson.toJson(new InsertedId(stock.getId())));
    }
}
