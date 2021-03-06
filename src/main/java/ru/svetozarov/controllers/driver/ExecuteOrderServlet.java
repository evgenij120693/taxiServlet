package ru.svetozarov.controllers.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.svetozarov.common.exception.OrderDAOException;
import ru.svetozarov.models.pojo.Order;
import org.apache.log4j.Logger;
import ru.svetozarov.services.IOrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by Шмыга on 02.03.2017.
 */
public class ExecuteOrderServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(ExecuteOrderServlet.class);

    private IOrderService IOrderService;
    @Autowired
    @Qualifier("orderService")
    public void setOrderService(IOrderService IOrderService) {
        this.IOrderService = IOrderService;
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
        HttpSession session = req.getSession(false);
        int id_driver = (int) session.getAttribute("id");
        if(id != 0){
            Order order = new Order(
                    id,
                    0,
                    null,
                    null,
                    null,
                    0,
                    id_driver,
                    new Timestamp(System.currentTimeMillis()).toString(),
                    new Timestamp(System.currentTimeMillis()+60*30).toString(),
                    4
            );
            try {
                if (IOrderService.updateOrderOfDriver(order)) {
                    logger.trace("Update successful");
                    resp.sendRedirect("/taxi/driver/list_new_order");
                }else{
                    logger.trace("Update failed");
                    resp.sendRedirect("/taxi/driver/list_new_order");
                }
            }catch (OrderDAOException e){
                logger.error(e);
                resp.sendRedirect("/taxi/error.jsp");
            }
        }else{
            resp.sendRedirect("/taxi/driver/new_list_order");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
