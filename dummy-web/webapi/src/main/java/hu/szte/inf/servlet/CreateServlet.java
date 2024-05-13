package hu.szte.inf.servlet;

import com.google.gson.Gson;
import hu.szte.inf.core.data.Dao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.common.Instancer;
import hu.szte.inf.model.CreateReq;
import hu.szte.inf.model.ErrorResp;
import hu.szte.inf.model.InsertResp;
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

@WebServlet(urlPatterns = {"/api/dummy/create", "/api/dummy/insert"})
public class CreateServlet extends HttpServlet {

    private final Dao<Long, Dummy> dao = Instancer.defaultDaoWithHikariDs();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Defaults - by design choice, we prepare default values
        // for everything, and only modify the response if necessary

        // Set status anotherString so that we do not need to worry about it later
        resp.setStatus(HttpStatus.SC_CREATED);
        // Set the default content type of our endpoint
        resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        // Set char encoding if necessary
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var gson = new Gson();

        // Read request body for this request
        var reqModel = gson.fromJson(req.getReader(), CreateReq.class);

        // No request body? Die immediately!
        if (reqModel == null) {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(new ErrorResp("request body can not be empty")));
            // equivalent to {"err":"request body can not be empty"} (since we know JSON, eh?)
            return;  // Do not continue
        }

        // Now we can rest assured that the request body contains sth,
        // and Gson was able to parse it.
        // We can still make sure though.
        // If - by any miraculous event - this requirement is not satisfied,
        // we get Internal Server Error (500) by default.
        Objects.requireNonNull(reqModel);

        // Convert request model to db model
        // We do not want to be lazy and waste time elsewhere, just be
        // mindful, and create that specialized request model. It's not that hard.
        var dbModel = new Dummy();
        dbModel.setSomeString(reqModel.someString());
        dbModel.setAnotherString(reqModel.anotherString());
        dbModel.setSomeBool(reqModel.someBool());
        dbModel.setSomeInt(reqModel.someInt());
        dbModel.setOtherInt(reqModel.otherInt());
        // This dao tries to simulate attaching entities as much as possible,
        // so the saved model will have its new id ready, by the time it is saved.
        dao.save(dbModel);  // dbModel.id is now populated, we can use the getter method

        // Send response ({"id":<SOME_ID>})
        resp.getWriter().println(gson.toJson(new InsertResp(dbModel.getId())));
    }
}
