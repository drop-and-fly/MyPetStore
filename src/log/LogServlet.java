package log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogServlet extends HttpServlet
{
	static boolean loginState = false;
	private static String username = null;
	//维护同一个session即可
	private static HttpSession session = null;
	
	//以下为外部接口，需要在合适的地方调用
	//-----public-----public-----public-----public-----public-----
	
	//登录或注册，通过一个参数区分
	//TODO：待测试；在合适的位置调用
	//已测试
	public static void setLogin(HttpSession nowSession, String username,boolean IsRegistNotLogin)
	{
		session = nowSession;
		
		loginState = true;
		session.setAttribute("0", new FlagClass(0, username));
		
		attributeNumsChg(1);
		if(IsRegistNotLogin)session.setAttribute("1",new LogClass("0"));
		else session.setAttribute("1",new LogClass("1"));
	}
	
	//TODO:待测试；在合适的位置调用
	//已测试
	public static void setLogout()
	{
		loginState=false;
		attributeNumsChg(1);
		session.setAttribute(Integer.toString(attributeNumsChg(0)),new LogClass("2"));		//当前Session的最后一条日志
		updateDBFromSession();
	}
	
	//TODO:待测试；在合适的位置调用；约定itemInfo
	public static void surfItem(String itemInfo)
	{
		FlagClass flag=getFlag();
		if (flag != null)
		{
			attributeNumsChg(1);
			session.setAttribute(Integer.toString(flag.attributeNums), new LogClass("3", itemInfo));
		}
		else {System.out.println("浏览错误，标志位未设置");}
	}
	
	//TODO:待测试；在合适的位置调用
	public static void addItemToShop(String itemInfo)
	{
		FlagClass flag=getFlag();
		if (flag != null)
		{
			attributeNumsChg(1);
			session.setAttribute(Integer.toString(flag.attributeNums), new LogClass("4", itemInfo));
		}
		else {System.out.println("添加到购物车错误，标志位未设置");}
	}
	
	public static void removeItemFromShop(String itemInfo)
	{
		FlagClass flag=getFlag();
		if (flag != null)
		{
			attributeNumsChg(1);
			session.setAttribute(Integer.toString(flag.attributeNums), new LogClass("5", itemInfo));
		}
		else {System.out.println("移出错误，标志位未设置");}
	}
	
	public static HttpSession getHttpSession()
	{
		if (session == null)
		{
			System.out.println("HttpSession还未初始化");
			return null;
		}
		return session;
	}
	
	//直接向表单插入数据项
	//TODO：待测试；在合适的位置调用；
	//已测试
	public static void updateDBFromSession()
	{
		if (session == null)
		{
			System.out.println("HttpSession还未初始化");
			return;
		}
		
		if (session.getAttribute("0") == null)
		{
			System.out.println("标志位还未创建");
			return;
		}
		
		if (username == "")
		{
			System.out.println("用户还未登录");
			return;
		}
		
		//获取标志位
		FlagClass flag = (FlagClass) session.getAttribute("0");
		String username = flag.username;
		
		if (flag.attributeNums == 0)
		{
			System.out.println("目前没有待插入的条目");
			return;
		}
		//有标志位之外的Attribute，获取并进行插入
		for (int i = 1; i <= flag.attributeNums; i++)
		{
			LogClass log = (LogClass) session.getAttribute(Integer.toString(i));
			LogDAO.directInsertLogEntry(username, log.logType, log.logObject, log.logTime);
		}
	}
	
	//TODO：实现从数据库读入、修改，以及界面
	
	//以下为内部接口
	//-----private-----private-----private-----private-----private-----
	//修改条目数量
	//TODO：待测试；
	//已测试
	private static int attributeNumsChg(int chg)
	{
		FlagClass flag = getFlag();
		
		if (flag != null)
		{
			flag.attributeNums += chg;
			session.setAttribute("0",flag);
			return flag.attributeNums;
		}
		else return 0;
	}
	
	private static FlagClass getFlag()
	{
		FlagClass flag=null;
		if (session != null)
		{
			flag = (FlagClass) session.getAttribute("0");
			return flag;
		}
		else return null;
	}
	
	//TODO：待删除，测试用，获取日志时间信息。已在别处实现
	private String getTimeInfo(HttpSession session)
	{
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",
				Locale.CHINA);
		return format.format(session.getLastAccessedTime());
	}
	
	//-----servlet-----servlet-----servlet-----servlet-----servlet-----
	//以下为servlet程序。TODO：执行时需要检查loginState
	
	//通过静态链接跳转，如访问商品时，需要使用doGet方法
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		session = req.getSession();
		loginState=true;
		username="JMZ";
		//TODO：doGet中的所有代码暂时都是测试代码。待删除
		if(false)
		{
			System.out.println(getTimeInfo(req.getSession()));
			System.out.println(req.getParameter("para"));
		}
		
		if (false)
		{
			//结论：可以用set直接修改Attribute
			session.setAttribute("0", "abc");
			System.out.println(session.getAttribute("0"));
			session.setAttribute("0", "abcd");
			System.out.println(session.getAttribute("0"));
		}
		
		//基本测试通过
		if(false&&req.getParameter("para")!=null)
		{
			if(Integer.parseInt( req.getParameter("para"))==1)
			setLogin(session,username,false);
			removeItemFromShop("item1");
			setLogout();
			req.getRequestDispatcher("../testSession/page2.jsp").forward(req,resp);
			System.out.println("基本测试");
		}
		
		setLogin(session,username,false);
		
		//以上为测试代码
		//TODO：以下为正式代码
		//约定：
		//	link参数用于传递跳转到的网页地址
		//	order参数用于确认请求类型，与logType对应即可
		
		try
		{
			System.out.println(req.getParameter("order")+"号指令");
			int order = Integer.parseInt(req.getParameter("order"));
			
			if(loginState==false)
			{
				System.out.println("由于未登录，"+order+"号指令不成功");
				//TODO：应该有其它的处理
			}
			else
			{
				//注销
				if (order == 2)
				{
					setLogout();
				}
				//浏览，约定itemInfo参数传递物品信息
				else if (order == 3)
				{
					surfItem(req.getParameter("itemInfo"));
				}
				//加入购物车，约定itemInfo参数传递物品信息
				else if (order == 4)
				{
					addItemToShop(req.getParameter("itemInfo"));
				}
				else if (order == 5)
				{
					removeItemFromShop(req.getParameter("itemInfo"));
				}
			}
			
			//TODO：此处的link地址在移植后需要调试
			req.getRequestDispatcher("../testSession/"+req.getParameter("link")).forward(req,resp);
		}
		catch (NumberFormatException e)
		{
			System.out.println(req.getParameter("order"));
			System.out.println("order参数格式错误，或参数未发送");
		}
	}
	
	//TODO：通过表单提交时，用doPost方法
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		//doPost只要在处理表单提交的对应Servlet程序里调用就行了
		//	约定：
		//	order Attribute用于确认请求类型，与logType对应即可
		
		try
		{
			int order = (Integer) req.getAttribute("order");
			//注册，约定user Attribute传递用户名
			if(order==0)
			{
				setLogin(req.getSession(),(String)req.getAttribute("user"),true);
			}
			//登录，约定user Attribute传递用户名
			else if(order==1)
			{
				setLogin(req.getSession(),(String)req.getAttribute("user"),false);
			}
		}
		catch (NumberFormatException e)
		{
			System.out.println(req.getAttribute("order"));
			System.out.println("order参数格式错误");
		}
	}
}