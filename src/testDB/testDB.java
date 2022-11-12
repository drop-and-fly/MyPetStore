package testDB;

import log.DBUtil;

import java.sql.*; //支持数据库编程的包

public class testDB
{
	
	public static void main(String args[])
	{
		Connection connection;
		try
		{
			
			Class.forName("com.mysql.cj.jdbc.Driver"); //装入JDBC驱动软件
			String dbURL = "jdbc:mysql://localhost:3306/logschema"; //数据库本机连接端口
			
			
			//=====本句为重要语法=====
			connection = DriverManager.getConnection( //调用连接数据库方法
					dbURL, "root", "9opandf6y"); //指定数据库、用户名，以及
			// 安装MySQL时建立的密码
			
			/*
			System.out.println("connection is succeeded..."); //如果连接成功，打印信息
			
			System.out.println("dbURL:" + dbURL);
			
			System.out.println("Connection: " + connection);
			 */
			
			System.out.println(connection);
			
			PreparedStatement statement=connection.prepareStatement("INSERT INTO logtable (who,how,time) VALUES(?,?,?)");
			statement.setString(1,"abc");
			statement.setString(2,"fuck");
			statement.setString(3,"15:24:12");
			statement.execute();
			
		} catch (ClassNotFoundException e)
		{ //异常处理
			
			System.out.println("Database driver not found.");
			
		} catch (SQLException e)
		{
			System.out.println("Error opening the db connection: " + e.getMessage());
		}
		
	}
	
}