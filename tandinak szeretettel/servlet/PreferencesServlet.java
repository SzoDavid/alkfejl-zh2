package hu.inf.szte.servlet;

import com.google.gson.Gson;
import hu.inf.szte.model.ReadStockReq;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.Base64;

@WebServlet(urlPatterns = {"/api/pref", "/api/preferences"})
public class PreferencesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var gson = new Gson();
        var pref = gson.fromJson(req.getReader(), ReadStockReq.class);

        byte[] byteEncodedObj = SerializationUtils.serialize(pref);
        String encodedPref = Base64.getEncoder().encodeToString(byteEncodedObj);

        var cookie = new Cookie("preference", encodedPref);

        cookie.setPath("/");
        cookie.setMaxAge(-1);
        resp.addCookie(cookie);
    }
}
