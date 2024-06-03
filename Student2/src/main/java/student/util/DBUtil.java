package student.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import student.entity.Student;

//通用的数据库操作方法
public class DBUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/student?useSSL=false&serverTimezone=UTC";
	private static final String UserName = "root";
	private static final String Pwd = "root";
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static Connection connection = null;
	public static PreparedStatement pstmt = null;
	public static ResultSet rs = null;
	
	//查询总数
	public static int getTotalCount(String sql){
		int count = -1;
		try {
			pstmt = createPreParedStatement(sql,null);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeAll(rs,pstmt,connection);
		}
		return count;
	}
	
	//增删改
	public static boolean executeUpdate(String sql,Object[] params) {
		try {
			pstmt = createPreParedStatement(sql,params);
			int count = pstmt.executeUpdate();
			
			System.out.println(count);
			if(count>0) {
				return true;
			}
			else {
				return false;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			closeAll(null,pstmt,connection);
	}
	}
	public static void closeAll(ResultSet rs,Statement stmt,Connection connection){
		try{
			if(rs!=null)rs.close();
			if(pstmt!=null)pstmt.close();
			if(connection!=null)connection.close();
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static PreparedStatement createPreParedStatement(String sql,Object[] params) throws ClassNotFoundException, SQLException {
			pstmt = getConnection().prepareStatement(sql);
			if(params!=null) {
			for(int i = 0;i<params.length;i++) {
				pstmt.setObject(i+1, params[i]);
			}
			}
			return pstmt;
	}
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(URL,UserName,Pwd);
	}
	//通用的查
	public static ResultSet executeQuery(String sql,Object[] params){
		List<Student> students = new ArrayList<>();
		Student student = null;
		
		try {
			pstmt = createPreParedStatement(sql,params);
			rs = pstmt.executeQuery();
			
			return rs;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	}

