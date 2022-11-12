package log;

import java.sql.*;

public class DBUtil
{
	//兼容性说明：USERNAME和PASSWORD要改成本地数据库的用户及密码
	//DRIVER和URL一般不用改（但是要需要确认，手动创建的数据库"logtable"是否存在）
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "9opandf6y";
	private static final String URL = "jdbc:mysql://localhost:3306/logschema";
	
	public static Connection getConnection()
	{
		Connection connection;
		try
		{
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			if(connection==null) System.out.println("数据库连接失败");
			return connection;
		} catch (ClassNotFoundException e)
		{
			System.out.println("驱动创建失败"+e.getMessage());
		} catch (SQLException e)
		{
			System.out.println("SQL错误"+e.getMessage());
		}
		return null;
	}
	
	//随用随关
	public static void closeConnection(Connection connection)
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//Statement、ResultSet直接通过getConnection获得的临时Connection获取即可
	public static void closeStatement(Statement statement)
	{
		if (statement != null)
		{
			try
			{
				statement.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	//带占位符的SQL。在C++中SQL直接提供了这种语法
	public static void closePreparedStatement(PreparedStatement preparedStatement)
	{
		if (preparedStatement != null)
		{
			try
			{
				preparedStatement.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void closeResultSet(ResultSet resultSet)
	{
		if (resultSet != null)
		{
			try
			{
				resultSet.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
