package hu.szte.inf.servlet;

import com.google.gson.Gson;
import hu.szte.inf.core.data.Dao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.common.Converter;
import hu.szte.inf.core.util.common.Instancer;
import hu.szte.inf.model.ErrorResp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = {"/api/dummy/read", "/api/dummy/list"})
public class ReadServlet extends HttpServlet {

    private final Dao<Long, Dummy> dao = Instancer.defaultDaoWithHikariDs();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Defaults - by design choice, we prepare default values
        // for everything, and only modify the response if necessary

        // Set status anotherString so that we do not need to worry about it later
        resp.setStatus(HttpStatus.SC_OK);
        // Set the default content type of our endpoint
        resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        // Set char encoding if necessary
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var gson = new Gson();

        // Read request parameters and handle errors
        Long id = null;
        String someString = req.getParameter("someString");
        String anotherString = req.getParameter("anotherString");
        Boolean someBool = null;
        Integer someInt = null;
        Integer otherInt = null;

        // We are patient little workers, and handle every error separately.
        // Just no overexertion.
        // Parse id
        // Is there an id in the first place?
        if (req.getParameter("id") != null) {
            try {
                // If there is, we should try parsing it
                id = Long.parseLong(req.getParameter("id"));
            } catch (NumberFormatException e) {
                // Not happening? DIE!
                resp.setStatus(HttpStatus.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(new ErrorResp("id param could not be parsed as an integer")));
                return;
            }
        }
        // Parse someBool value
        // Can I get a someBool param?
        if (req.getParameter("someBool") != null) {
            // Is it false? Or is it true? If neither --> DIE!
            if (!req.getParameter("someBool").equalsIgnoreCase("true") &&
                    !req.getParameter("someBool").equalsIgnoreCase("false")) {
                resp.setStatus(HttpStatus.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(new ErrorResp("someBool parameter should be either true or false (ignoring case)")));
                return;
            }
            // Parse the validated parameter
            someBool = Boolean.parseBoolean(req.getParameter("someBool"));
        }
        // Parse someInt
        // Do I find someInt somewhere?
        if (req.getParameter("someInt") != null) {
            try {
                // Oh, yes!
                someInt = Integer.parseInt(req.getParameter("someInt"));
            } catch (NumberFormatException e) {
                // Sadly, you will have to DIE now
                resp.setStatus(HttpStatus.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(new ErrorResp("someInt param could not be parsed as an integer")));
                return;
            }
        }
        // Parse otherInt
        // I will do what must be done
        if (req.getParameter("otherInt") != null) {
            try {
                // You will try ...
                otherInt = Integer.parseInt(req.getParameter("otherInt"));
            } catch (NumberFormatException e) {
                // Oh, nooooo
                resp.setStatus(HttpStatus.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(new ErrorResp("someInt param could not be parsed as an integer")));
                return;
            }
        }

        // Query db
        var dbModel = new Dummy(id, someString, anotherString, someBool, someInt, otherInt);
        var query = Converter.iterableToList(dao.findAllByCrit(dbModel));

        // Set cookies containing size information.
        // It only the size that matters ...
        var cookieFull = new Cookie("full-size", String.format("%d", dao.count()));
        var cookieCurr = new Cookie("current-size", String.format("%d", query.size()));

        // Set response cookies, and send response
        resp.addCookie(cookieFull);
        resp.addCookie(cookieCurr);
        resp.getWriter().println(gson.toJson(query));
    }
}
