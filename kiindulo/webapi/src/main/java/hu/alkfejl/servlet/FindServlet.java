package hu.alkfejl.servlet;

import com.google.gson.Gson;
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
public class FindServlet extends HttpServlet {
    private int requestNumber = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestNumber++;

        String cim = req.getParameter("cim");
        String eloado = req.getParameter("eloado");
        String hosszStr = req.getParameter("hossz");
        int hossz = -1;
        try {
            hossz = Integer.parseInt(hosszStr);
        } catch (NumberFormatException e) {
            //T ODO
        }

        ZeneController zc = ZeneController.getInstance(this.getClass());
        var list = zc.find(new Zene().setCim(cim)
                          .setEloado(eloado)
                          .setHossz(hossz));

        Gson gson = new Gson();
        var jsonList = gson.toJson(list);

        Cookie cookie = new Cookie("request-number", String.valueOf(requestNumber));
        cookie.setMaxAge(5000);
        cookie.setPath("/");
        resp.addCookie(cookie);

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().println(jsonList);
    }
}
