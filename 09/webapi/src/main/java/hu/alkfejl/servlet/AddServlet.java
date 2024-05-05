package hu.alkfejl.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import hu.alkfejl.controller.ZeneController;
import hu.alkfejl.model.Zene;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/api/add", "/alistaadd"})
public class AddServlet extends HttpServlet {

    private final ZeneController ctrl = ZeneController.getInstance(AddServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");

        // Cookie magic
        if (req.getCookies() != null) {
            for (var cookie : req.getCookies()) {
                var myCookie = new Cookie("modded" + cookie.getName(), cookie.getValue());
                resp.addCookie(myCookie);
            }
        }

        var gson = new Gson();
        try {
            var newZene = gson.fromJson(req.getReader(), Zene.class);
            ctrl.add(newZene);
        }
        catch (JsonSyntaxException | JsonIOException | NullPointerException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
