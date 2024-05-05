package hu.alkfejl.servlet;

import com.google.gson.Gson;
import hu.alkfejl.Main;
import hu.alkfejl.controller.ZeneController;
import hu.alkfejl.model.Zene;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/api/list", "/alista"})
public class ListServlet extends HttpServlet {

    private final ZeneController ctrl = ZeneController.getInstance(ListServlet.class);
    private static Integer callNum = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ++callNum;

        // Init
        resp.setContentType("application/json");
        var gson = new Gson();

        // Filter
        String cim = req.getParameter("cim");
        String stilus = req.getParameter("stilus");
        String eloado = req.getParameter("eloado");
        String hossz = req.getParameter("hossz");

        var zeneFilter = new Zene();
        zeneFilter.setCim(cim);
        zeneFilter.setStilus(stilus);
        zeneFilter.setEloado(eloado);
        try {
            zeneFilter.setHossz(Integer.parseInt(hossz));
        }
        catch (NumberFormatException e) {
            // Do nothing
        }

        // Cookie
        var cookie = new Cookie("request-number", callNum.toString());
        resp.addCookie(cookie);

        // var jar = req.getCookies();

        resp.getWriter().println(gson.toJson(ctrl.find(zeneFilter)));
    }
}
