package csu.web.mypetstore.web.servlet;

import javax.servlet.ServletException;
import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.Item;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RemoveCartServlet extends HttpServlet {
    private static final String ERROR_FORM = "/WEB-INF/jsp/common/error.jsp";
    private static final String CART_FORM = "/WEB-INF/jsp/cart/cart.jsp";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        String workingItemId = req.getParameter("workingItemId");




        Item item = cart.removeItemById(workingItemId);

        if (item == null) {
            session.setAttribute("errorMsg", "Attempting to remove null CartItem from cart");
            req.getRequestDispatcher(ERROR_FORM).forward(req, resp);
        } else {
            resp.sendRedirect("http://localhost:8080/mypetstore/updateCart");
        }
    }
}
