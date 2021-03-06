package ru.svetozarov.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.svetozarov.common.exception.DriverDAOException;
import org.apache.log4j.Logger;
import ru.svetozarov.services.IDriverService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Шмыга on 26.02.2017.
 */
public class DeleteDriverServlet extends HttpServlet{
    private static Logger logger = Logger.getLogger(DeleteDriverServlet.class);

    private IDriverService IDriverService;
    @Autowired
    @Qualifier("driverService")
    public void setDriverService(IDriverService IDriverService) {
        this.IDriverService = IDriverService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = (!req.getParameter("id").equals("") ) ? Integer.valueOf(req.getParameter("id")) : 0;
        if(id != 0){
            try {
                IDriverService.deleteDriverById(id);
                logger.trace("delete driver by id="+id+" successfull");
                resp.sendRedirect("/taxi/admin/list_driver");
            } catch (DriverDAOException e) {
                logger.error(e);
                resp.sendRedirect("/taxi/error.jsp");
            }

        }else {
            logger.trace("Id is null");
            resp.sendRedirect("/taxi/admin/list_driver");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
