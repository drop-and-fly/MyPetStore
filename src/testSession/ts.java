package testSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ts extends HttpServlet
{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		HttpSession hs=req.getSession();
		hs.setAttribute("0",1);
		System.out.println(1);				//服务器测试
		
		{
			//resp.sendRedirect("page2.jsp");
			//page2正确，表示直接跳转时，虽然request改变了，但Session保持不变
			
			req.getRequestDispatcher("page2.jsp").forward(req, resp);
			//仍然正确，表示网页间接跳转时，Session保持不变
			
			//注意特殊情况：重新部署时，Session重新创建了，然而信息并没有立即清除
		}

	}
}