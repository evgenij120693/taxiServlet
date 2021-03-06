package ru.svetozarov.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.svetozarov.common.exception.AutoDAOException;
import ru.svetozarov.models.pojo.Auto;
import org.apache.log4j.Logger;
import ru.svetozarov.services.IAutoService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Шмыга on 26.02.2017.
 */
public class EditAutoServlet extends HttpServlet{
    private static Logger logger = Logger.getLogger(EditClientServlet.class);

    private IAutoService IAutoService;
    @Autowired
    @Qualifier("autoService")
    public void setAutoService(IAutoService IAutoService) {
        this.IAutoService = IAutoService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = (!req.getParameter("id").equals(""))? Integer.valueOf(req.getParameter("id")):0;
        if(id != 0){
            try {
                Auto auto = IAutoService.getAutoById(id);
                if(auto != null){
                    req.setAttribute("auto", auto);
                    req.getRequestDispatcher("/admin/edit_auto.jsp").forward(req, resp);
                }else{
                    resp.sendRedirect("/taxi/admin/list_auto");
                }
            } catch (AutoDAOException e) {
                logger.error(e);
                resp.sendRedirect("/taxi/error.jsp");
            }
        }else{
            resp.sendRedirect("/taxi/admin/list_client");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = (!req.getParameter("id").equals(""))? Integer.valueOf(req.getParameter("id")):0;
        Auto auto = new Auto(
                id,
                req.getParameter("marka"),
                req.getParameter("model"),
                req.getParameter("regNumber"),
                req.getParameter("color")
        );
        try {
            if(IAutoService.updateAuto(auto)){
                logger.trace("update successful ");
                resp.sendRedirect("/taxi/admin/list_auto");
            }else{
                logger.trace("update failed ");
                resp.sendRedirect("/taxi/admin/list_auto");
            }
        } catch (AutoDAOException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        }

    }
}
