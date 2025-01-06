package com.nourish1709.learning.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "userServlet", value = "/user-servlet")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        resp.getWriter().println("""
                <html>
                    <body>
                        <h1>User Servlet</h1>
                        <p>Date: %s</p>
                        <p>Name: %s</p>
                    </body>
                </html>
                """.formatted(
                LocalDate.now(),
                "User"
        ));
    }
}
