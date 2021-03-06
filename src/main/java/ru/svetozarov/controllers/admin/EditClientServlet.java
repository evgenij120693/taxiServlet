package ru.svetozarov.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.svetozarov.common.exception.ClientDAOException;
import ru.svetozarov.common.exception.UserDAOException;
import ru.svetozarov.models.pojo.Client;
import org.apache.log4j.Logger;
import ru.svetozarov.services.IClientService;
import ru.svetozarov.services.IUserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Шмыга on 25.02.2017.
 */
public class EditClientServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(EditClientServlet.class);

    private IClientService IClientService;
    @Autowired
    @Qualifier("clientService")
    public void setClientService(IClientService IClientService) {
        this.IClientService = IClientService;
    }

    private IUserService IUserService;
    @Autowired
    @Qualifier("userService")
    public void setUserService(IUserService IUserService) {
        this.IUserService = IUserService;
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
                Client client = IClientService.getClientBiId(id);
                if(client != null){
                    req.setAttribute("client", client);
                    req.getRequestDispatcher("/admin/edit_client.jsp").forward(req, resp);
                }else{
                    resp.sendRedirect("/taxi/admin/list_client");
                }
            } catch (ClientDAOException e) {
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
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String role = "client";
        String greetings = "";
        Client client = new Client(
                Integer.valueOf(req.getParameter("id")),
                req.getParameter("name"),
                req.getParameter("sex"),
                req.getParameter("phone"),
                req.getParameter("email"),
                req.getParameter("login"),
                req.getParameter("password")
        );
        try {
            if (IUserService.checkUserByLoginAndId(client.getLogin(), client.getId())) {
                if (IClientService.updateClient(client)) {
                    logger.trace("Client " + client.getName() + " updated");
                    resp.sendRedirect("/taxi/admin/list_client");
                } else {
                    logger.trace("Dublicate client");
                    resp.sendRedirect("/taxi/admin/edit_client?id="+client.getId());
                }
            } else  {
                resp.sendRedirect("/taxi/admin/edit_client?id="+client.getId());
            }
        } catch (UserDAOException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        } catch (ClientDAOException e) {
            logger.error(e);
            resp.sendRedirect("/taxi/error.jsp");
        }
    }
}
