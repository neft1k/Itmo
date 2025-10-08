package org.example.web2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AreaCheckServlet", urlPatterns = {"/areaCheck"})
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        double x = Double.parseDouble(request.getParameter("x"));
        double y = Double.parseDouble(request.getParameter("y"));
        double r = Double.parseDouble(request.getParameter("r"));

        boolean isInside = (x >= 0 && y >= 0 && x * x + y * y <= r * r) ||
                (x <= 0 && y >= 0 && y <= r && x <= r) ||
                (x <= 0 && y <= 0 && y >= -x / 2 - r / 2);

        response.setContentType("text/html");
        // Store the current result in the session
        HttpSession session = request.getSession();
        List<String[]> results = (List<String[]>) session.getAttribute("results");

        if (results == null) {
            results = new ArrayList<>();
        }

        // Add the current result to the list
        results.add(new String[]{String.valueOf(x), String.valueOf(y), String.valueOf(r), isInside ? "inside" : "outside"});

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Update session attribute
        session.setAttribute("results", results);
        PrintWriter out = response.getWriter();
        request.setAttribute("x", x);
        request.setAttribute("y", y);
        request.setAttribute("r", r);
        request.setAttribute("isInside", isInside);
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
