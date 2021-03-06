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
import java.util.List;

/**
 * Created by Шмыга on 26.02.2017.
 */
public class ListAutoServlet extends HttpServlet{
    private static Logger logger = Logger.getLogger(ListDriverServlet.class);

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
        try {
            List<Auto> list = IAutoService.getAllAuto();
            logger.trace("get all auto count="+list.size());
            req.setAttribute("list", list);
            req.getRequestDispatcher("/admin/list_auto.jsp").forward(req, resp);
        } catch (AutoDAOException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
