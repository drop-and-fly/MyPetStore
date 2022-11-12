package log;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;

//与老师给出的DAO文件可能不太一样。我这个DAO提供了一些数据库操作的接口
public class LogDAO
{
	static ArrayList<String> logList=null;
	
	//直接插入日志条目
	public static void directInsertLogEntry(String username, String logType,
											String logObject, String logTime)
	{
		String preparedString;
		if (logObject == "null")
		{
			preparedString = "INSERT INTO logtable(who,how,time) VALUES(?,?," +
					"?)";
		}
		else
		{
			preparedString = "INSERT INTO logtable(who,how,what,time) VALUES" +
					"(?,?,?,?)";
		}
		
		try
		{
			Connection connection = DBUtil.getConnection();
			PreparedStatement statement =
					connection.prepareStatement(preparedString);
			
			statement.setString(1, username);
			statement.setString(2, logType);
			if (logObject == "null") {statement.setString(3, logTime);}
			else
			{
				statement.setString(3, logObject);
				statement.setString(4, logTime);
			}
			
			System.out.println(statement.execute());
			
			DBUtil.closePreparedStatement(statement);
			DBUtil.closeConnection(connection);
		} catch (SQLException e)
		{
			System.out.println("SQL错误" + e.getMessage());
		}
	}
	
	//获取所有日志条目到logList中
	public static void getAllLogEntry()
	{
		if(logList==null)logList=new ArrayList<>();
		try
		{
			Statement statement = DBUtil.getConnection().createStatement();
			statement.execute("SELECT * FROM logtable ORDER BY time DESC");
			ResultSet set = statement.getResultSet();
			
			/*
			//next，迭代行并检测存在性
			if(set.next())
			{
				//logsNum=set.getInt();
				//但getInt需要对应列，这里不能这么用
			}
			else return;
			 */
			while (set.next())
			{
				String logUser = set.getString("who");
				String logType = set.getString("how");
				String logObj = set.getString("what");
				String logTime = set.getString("time");
				switch (logType)
				{
					//注册
					case "0":
						logList.add("用户" + logUser + "进行了注册，于" + logTime);
						break;
					//登录
					case "1":
						logList.add("用户" + logUser + "进行了登录，于" + logTime);
						break;
					//登出
					case "2":
						logList.add("用户" + logUser + "登出了，于" + logTime);
						break;
					//浏览
					case "3":
						logList.add("用户" + logUser + "浏览了" + logObj + "，于" + logTime);
						break;
					//加入
					case "4":
						logList.add("用户" + logUser + "将" + logObj + "加入了购物车，于" + logTime);
						break;
					//移出
					case "5":
						logList.add("用户" + logUser + "将" + logObj + "移出了购物车，于" + logTime);
						break;
				}
			}
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static ArrayList<String> getLogList()
	{
		return logList;
	}
	
	
	//以下内容用于在JS里访问，值用于传给JSP。类似于ResultSet.
	//注意：这里不满足异步性！完全没有实现遍历时的安全修改！
	public static String trasverseStr=null;
	private static int trasverseCnt=0;
	public static void trasverseInt()
	{
		trasverseCnt=0;
	}
	public static boolean trasverseArrayList()
	{
		if(logList==null)return false;
		if(logList.isEmpty())return false;
		if(trasverseCnt==logList.size())return false;
		trasverseStr=logList.get(trasverseCnt++);
		System.out.println(trasverseStr);
		return true;
	}
}
