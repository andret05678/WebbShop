package com.UI;

import com.BO.Services.UserServices;
import com.UI.Info.UserInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserServices userServices;

    @Override
    public void init() throws ServletException {
        this.userServices = new UserServices();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("userInfo") != null) {
            response.sendRedirect("testpage");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(request, response);
        } else if ("register".equals(action)) {
            handleRegistration(request, response);
        } else {
            response.sendRedirect("login");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserInfo userInfo = userServices.login(email, password);

        if (userInfo != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", userInfo);
            session.setAttribute("userId", userInfo.getId());
            session.setAttribute("username", userInfo.getUsername());
            session.setAttribute("role", userInfo.getRole());

            response.sendRedirect("testpage");
        } else {
            request.setAttribute("errorMessage", "Invalid email or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        int roleId = 1;

        UserInfo userInfo = userServices.register(email, password, username, roleId);

        if (userInfo != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", userInfo);
            session.setAttribute("userId", userInfo.getId());
            session.setAttribute("username", userInfo.getUsername());
            session.setAttribute("role", userInfo.getRole());

            response.sendRedirect("testpage");
        } else {
            request.setAttribute("errorMessage", "Email already exists or registration failed");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}