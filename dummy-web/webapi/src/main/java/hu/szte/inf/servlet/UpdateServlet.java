package hu.szte.inf.servlet;

import com.google.gson.Gson;
import hu.szte.inf.core.data.Dao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.common.Instancer;
import hu.szte.inf.model.ErrorResp;
import hu.szte.inf.model.UpdateReq;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@WebServlet(urlPatterns = {"/api/dummy/update", "/api/dummy/refresh"})
public class UpdateServlet extends HttpServlet {

    private final Dao<Long, Dummy> dao = Instancer.defaultDaoWithHikariDs();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Defaults - by design choice, we prepare default values
        // for everything, and only modify the response if necessary

        // Set status anotherString so that we do not need to worry about it later
        resp.setStatus(HttpStatus.SC_NO_CONTENT);
        // Set the default content type of our endpoint
        resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        // Set char encoding if necessary
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var gson = new Gson();

        // Read request body for this request
        var reqModel = gson.fromJson(req.getReader(), UpdateReq.class);

        // No request body? Die immediately!
        if (reqModel == null) {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(new ErrorResp("request body can not be empty")));
            // equivalent to {"err":"request body can not be empty"}
            return;  // Do not continue
        }
        // If - by any miraculous event - this requirement is not satisfied,
        // we get Internal Server Error (500) by default.
        Objects.requireNonNull(reqModel);

        // ID should not be null! Die!
        if (reqModel.id() == null) {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(new ErrorResp("id should not be null")));
            return;  // Do not continue
        }

        // Convert request model to db model
        // You already know the story ...
        var dbModel = new Dummy();
        dbModel.setId(reqModel.id());
        dbModel.setSomeString(reqModel.someString());
        dbModel.setAnotherString(reqModel.anotherString());
        dbModel.setSomeBool(reqModel.someBool());
        dbModel.setSomeInt(reqModel.someInt());
        dbModel.setOtherInt(reqModel.otherInt());

        // This is a PUT request, so we make it behave like one.
        // If there is no updated row that signals us that the
        // resource is not present. In that case we would like
        // to create that resource.
        int affectedRows = dao.updateById(reqModel.id(), dbModel);
        // Create missing resource
        if (affectedRows == 0) {
            dao.save(dbModel);
            resp.setStatus(HttpStatus.SC_CREATED);
        }
    }
}
