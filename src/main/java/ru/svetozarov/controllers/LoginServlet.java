package ru.svetozarov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.svetozarov.common.exception.HashPasswordException;
import ru.svetozarov.common.exception.UserDAOException;
import ru.svetozarov.common.util.HashPassword;
import ru.svetozarov.models.pojo.User;
import org.apache.log4j.Logger;
import ru.svetozarov.services.AdminService;
import ru.svetozarov.services.ClientService;
import ru.svetozarov.services.DriverService;
import ru.svetozarov.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Шмыга on 24.02.2017.
 */
public class LoginServlet extends HttpServlet {
    private UserService userService;
    private HashPassword hashPassword;
    @Autowired
    public void setHashPassword(HashPassword hashPassword) {
        this.hashPassword = hashPassword;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        String role = "";
        int id = 0;
        try {
            User user = null;
                user = userService.getUserByLoginAndPassword(login, hashPassword.hashingPassword(password));

            if (user != null) {
                setSession(user.getId(), user.getRole(), user.getName(), req);
                resp.sendRedirect("/taxi/"+user.getRole());
            }  else {
                logger.trace("Authorization failed");
                req.setAttribute("error", "Неверный логин/пароль.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                //resp.sendRedirect("/taxi/login");
            }
        } catch (UserDAOException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        }catch (HashPasswordException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        }
    }
    private static void setSession(int id, String role, String name, HttpServletRequest req){
        HttpSession session = req.getSession();
        session.setAttribute("id", id);
        session.setAttribute("role", role);
        session.setAttribute("name", name);
        session.setMaxInactiveInterval(30 * 60);
        logger.trace("Authorization successfull");
    }

}
