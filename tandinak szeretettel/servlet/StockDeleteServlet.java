package hu.inf.szte.servlet;

import com.google.gson.Gson;
import hu.inf.szte.core.data.Dao;
import hu.inf.szte.core.data.StockJooqDao;
import hu.inf.szte.core.model.Stock;
import hu.inf.szte.model.Error;
import hu.inf.szte.model.DeleteStockReq;
import hu.inf.szte.model.ModifiedRows;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jooq.SQLDialect;

import javax.sql.DataSource;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/stock/delete", "/api/stock/remove"})
public class StockDeleteServlet extends HttpServlet {
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
        var reqModel = gson.fromJson(req.getReader(), DeleteStockReq.class);
        if (reqModel == null || reqModel.id() < 0) {
            resp.setStatus(400);
            resp.getWriter().println(gson.toJson(new Error("req empty")));
            return;
        }

        var affectedRows = dao.deleteById(reqModel.id());
        resp.setStatus(200);
        resp.getWriter().println(gson.toJson(new ModifiedRows(affectedRows)));
    }
}
