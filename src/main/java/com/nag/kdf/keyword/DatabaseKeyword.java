package com.nag.kdf.keyword;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.WebDriver;

import com.nag.kdf.customizeKeywords.Navigator_Keywords;
import com.nag.kdf.utility.KDFEvent;
import com.nag.kdf.utility.KDFEventResult;
import com.nag.kdf.utility.SharedResource;

public class DatabaseKeyword extends Navigator_Keywords{
	
	public DatabaseKeyword() {}
	public DatabaseKeyword(WebDriver objWebdriver,KDFEvent objAFTEvent)
	{
		super(objWebdriver,objAFTEvent);
	}
	String userName,password;

	private static String url = "jdbc:sqlserver://localhost:1433;databaseName=MYDB;user=UserName;password=pswd"; 
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	public Connection GetConnection(String server,String uname, String pwd, String DBName) {
		if (conn == null) 
			return(createConnection(server,uname,pwd,DBName));
		else
			return conn;
	}
	
	private  Connection createConnection(String server,String uname, String pwd, String DBName) {
		
		try {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			url=url.replace("localhost", server);
			url=url.replace("MYDB", DBName);
			url=url.replace("UserName", uname);
			url=url.replace("pswd", pwd);
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		return conn;
	}
	
	
	public KDFEventResult GetDBCount() {
		try {
				String ServerName=this.strParent;
				String DBName=this.strChild;
				String uname=this.strParam1;
				String pwd=this.strParam2;
				String query =this.strParam3;
				String outputVar=this.strParam4;
				int count=0;
				GetConnection(ServerName,uname,pwd,DBName);
				rs = stmt.executeQuery(query);
				//rs.getRow();
				while (rs.next()){
					count++;
				}
				
				SharedResource.dictRTVarCollection.put(outputVar, Integer.toString(count));
				this.strActualRes = "Total count is " + count;
				this.strStatus = "PASS";
			} catch (Exception e) {
				this.strActualRes = "Exception Occured - " + e.getMessage();
				this.strStatus = "FAIL";
			}		
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	public KDFEventResult CloseConnection() {
		try {
			conn.close();
			conn=null;
			this.strActualRes = "Connection Closed";
			this.strStatus = "PASS";
	
		} catch (Exception e) {
			this.strActualRes = "Exception Occured while closing DB Connection - " + e.getMessage();
			this.strStatus = "FAIL";
		}		
		return new KDFEventResult(this.strActualRes, this.strStatus);
	}
	
	
	public void CloseConnection1()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}
	

}
