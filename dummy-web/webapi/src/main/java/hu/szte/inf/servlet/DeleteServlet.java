package hu.szte.inf.servlet;

import com.google.gson.Gson;
import hu.szte.inf.core.data.Dao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.common.Instancer;
import hu.szte.inf.model.ErrorResp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;

import java.io.IOException;

@WebServlet(urlPatterns = {"/api/dummy/delete", "/api/dummy/remove"})
public class DeleteServlet extends HttpServlet {

    private final Dao<Long, Dummy> dao = Instancer.defaultDaoWithHikariDs();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set status anotherString so that we do not need to worry about it later
        resp.setStatus(HttpStatus.SC_NO_CONTENT);
        // Set the default content type of our endpoint
        resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        var gson = new Gson();

        // Read request parameters and handle errors
        long id;
        try {
            // Trying to parse that request param
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            // Did not go well? Die!
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(new ErrorResp("id param could not be parsed as an integer")));
            return;
        }

        // Delete that bastard! Leave no trace!
        dao.deleteById(id);
    }
}
