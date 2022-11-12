package csu.web.mypetstore.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CartFormServlet extends HttpServlet {
    private static final String ERROR_FORM = "/WEB-INF/jsp/common/error.jsp";
    private static final String CART_FORM = "/WEB-INF/jsp/cart/cart.jsp";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // todo
        try {
            req.getRequestDispatcher(CART_FORM).forward(req, resp);
        } catch (Exception e) {
            req.getRequestDispatcher(ERROR_FORM).forward(req, resp);
        }

    }
}
