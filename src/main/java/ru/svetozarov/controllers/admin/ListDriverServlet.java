package ru.svetozarov.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.svetozarov.common.exception.DriverDAOException;
import ru.svetozarov.models.pojo.Driver;
import org.apache.log4j.Logger;
import ru.svetozarov.services.IDriverService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Шмыга on 25.02.2017.
 */
public class ListDriverServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(ListDriverServlet.class);

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
        try {
            List<Driver> list = IDriverService.getListDriver();
           /* List<Auto> listAuto = AutoService.getAllAuto();
            List<Status> statusList = StatusService.getAllStatusDriver();*/
            req.setAttribute("list", list);
            /*req.setAttribute("listAuto", listAuto);
            req.setAttribute("listStatus", statusList);*/
            req.getRequestDispatcher("/admin/list_driver.jsp").forward(req, resp);
        } catch (DriverDAOException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        } /*catch (AutoDAOException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        } catch (StatusDAOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
