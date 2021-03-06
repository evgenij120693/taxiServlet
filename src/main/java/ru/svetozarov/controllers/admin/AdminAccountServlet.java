package ru.svetozarov.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.svetozarov.common.exception.UserDAOException;
import ru.svetozarov.models.pojo.Admin;
import org.apache.log4j.Logger;
import ru.svetozarov.services.IAdminService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Шмыга on 25.02.2017.
 */
public class AdminAccountServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(AddAutoServlet.class);

    private IAdminService IAdminService;
    @Autowired
    @Qualifier("adminService")
    public void setAdminService(IAdminService IAdminService) {
        this.IAdminService = IAdminService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        int id = (int) session.getAttribute("id");
            try {
                Admin admin = IAdminService.getAdminById(id);
                if (admin != null) {
                    req.setAttribute("admin", admin);
                    req.getRequestDispatcher("/admin/index.jsp").forward(req, resp);
                } else {
                    resp.sendRedirect("/taxi/error.jsp");
                }
            } catch (UserDAOException e) {
                logger.error(e);
                resp.sendRedirect("/taxi/error.jsp");
            }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
