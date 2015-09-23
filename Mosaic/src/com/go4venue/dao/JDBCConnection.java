package com.go4venue.dao;

import java.sql.*;
import java.util.List;
import java.util.Properties;




public class JDBCConnection {
	Properties props1 = new Properties();
	private String host = "";
private static Connection con;
	
	private static JDBCConnection conn = null;
	private ResultSet rs;
	private static int counter = 1;
	private JDBCConnection() {
		try{
			
		props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/go4venue/resources/DBConfig.properties"));
		 String userName = props1.getProperty("DB_USERNAME"); 
		// String password = props1.getProperty("DB_PASSWORD");
		 String password = "";
		 String host = props1.getProperty("DB_SERVER");
		 
			// Load the Driver class. 
		Class.forName("com.mysql.jdbc.Driver");
		// If you are using any other database then load the right driver here.

		//Create the connection using the static getConnection method
		con = DriverManager.getConnection (host,userName,password);

		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static JDBCConnection getInstance(){
		 
				try {
					if(conn == null || con.isClosed()){
						synchronized(JDBCConnection.class){
							if(conn == null || con.isClosed()){
								conn = new JDBCConnection();
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return conn;
				//System.out.println("JDBC Count for "+counter++);
				//return new JDBCConnection();
			}
	public ResultSet executeQuery(String query,List<String> params){
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
		if(params!=null){
			int i = 1;
			for(String str: params){
			
				pstmt.setString(i++, str);
				
			}
			}
		
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean insertData(String query,List<String> params,boolean isUpdate) throws SQLException{
		PreparedStatement pstmt  = null;
		PreparedStatement pstmt1 = null;
		
		boolean flag = false;
		
		try {
			pstmt = con.prepareStatement(query);
		
		
		
		if(params!=null){
			int i = 1;
			for(String str: params){
			
				pstmt.setString(i++, str);
				
			}
		}
		
			//System.out.println(query);
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			flag = false;
			//e.printStackTrace();
			throw e;
		}
		return flag;
	}
	
	
	public boolean upsertData(String query,List<String> params) throws SQLException{
		PreparedStatement pstmt  = null;
		boolean flag = false;
		
		try {
		conn = getInstance();
			pstmt = con.prepareStatement(query);
			if(params!=null){
			int i = 1;
			for(String str: params){
				pstmt.setString(i++, str);
			}
		}
		
			//System.out.println(query);
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
			
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
			throw e;
		}finally{
			/*try {
				//pstmt.close();
				//pstmt1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
		}
		return flag;
	}
	
	public boolean insertBatch(String query,List<String> params){
		boolean flag = false;
		
		
		
		
		return flag;
	}
    
	public PreparedStatement prepareStatement(String query) throws Exception{
		return con.prepareStatement(query);
	}
	
	
	
	public void closeConnection(){
		try {
			if(this.con!=null)
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
