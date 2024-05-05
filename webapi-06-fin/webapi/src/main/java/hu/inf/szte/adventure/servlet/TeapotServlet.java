package hu.inf.szte.adventure.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;

import java.io.IOException;

@WebServlet(urlPatterns = "/teapot")
public class TeapotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ContentType.TEXT_PLAIN.getMimeType()); // text/plain
        resp.setStatus(418);
        resp.getWriter().println("I am a teapot");
    }
}
