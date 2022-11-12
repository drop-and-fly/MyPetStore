package log;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Locale;

//日志类
//如果日志数为正，则Attribute从1开始编号
public class LogClass
{
	String logTime;                //日志持续时间，以format("HH:mm:ss")方式表示
	String logType;                //相当于日志类型
	//	注册：0
	//	登录：1
	//	登出：2
	//	浏览商品：3
	//	加入购物车：4
	//	移出购物车：5
	
	String logObject;            //操作对象，TODO：像logType一样约定代号，或者直接通过接口获得字符串？
	
	//TODO：本构造函数在点击时（GET或POST）立刻调用
	public LogClass(String logType, String logObject)
	{
		HttpSession tmpSession = LogServlet.getHttpSession();
		if (tmpSession == null) return;
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",
				Locale.CHINA);
		this.logTime = format.format(tmpSession.getLastAccessedTime());
		
		this.logType = logType;
		this.logObject = logObject;
	}
	
	//部分日志没有操作对象，如注册、登录。为了操作方便，约定logObject的值为"null"字符串
	public LogClass(String logType)
	{
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",
				Locale.CHINA);
		this.logTime =
				format.format(LogServlet.getHttpSession().getLastAccessedTime());
		
		this.logType = logType;
		this.logObject = "null";
	}
}
