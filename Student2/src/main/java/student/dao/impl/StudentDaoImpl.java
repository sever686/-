package student.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import student.dao.IStudentDao;
import student.entity.Student;
import student.util.DBUtil;

public class StudentDaoImpl implements IStudentDao{
	
	private final String URL = "jdbc:mysql://localhost:3306/student?useSSL=false&serverTimezone=UTC";
	private final String UserName = "root";
	private final String Pwd = "root";

	String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	//查询全部学生信息
	public List<Student> queryAllStudents(){
		PreparedStatement pstmt = null;
		Student student = null;
		List<Student> students = new ArrayList<>();
		ResultSet rs = null;
		try {
			String sql = "select * from student1";
			rs = DBUtil.executeQuery(sql, null);
			while(rs.next()) {
				int sno= rs.getInt("sno");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String dept = rs.getString("dept");
				student = new Student(name, sno, age, dept);
				students.add(student);
			}
			return students;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			DBUtil.closeAll(rs, pstmt, DBUtil.connection);
	}
	}
	
	
	//判断此人是否存在
	public boolean isExist(int sno) {
		return queryStudentBySno(sno) == null? false:true;
	}
	
	//增加学生信息
	public boolean addStudent(Student student) {
		String sql = "insert into student1(name,sno,age,dept) values(?,?,?,?)";
		Object[] params = {student.getName(),student.getSno(),student.getAge(),student.getDept()};
		return DBUtil.executeUpdate(sql, params);
	}
	//删除学生信息
	public boolean deleteStudentBySno(int sno) {
		String sql = "delete from student1 where sno =?";
		Object[] params = {sno};
		return DBUtil.executeUpdate(sql, params);
	}
	
	//根据sno找到要修改的学生，然后再进行修改
	public boolean  updateStudentBySno(int sno,Student student) {
		String sql = "update student1 set name =?,age=?,dept=? where sno=?";
		Object[] params = {student.getName(),student.getAge(),student.getDept(),sno};
		
		return DBUtil.executeUpdate(sql, params);
		
	}
	//根据学号查询学生信息
	public Student queryStudentBySno(int sno){
		PreparedStatement pstmt = null;
		Student student = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(URL,UserName,Pwd);
			String sql = "select * from student1 where sno = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, sno);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int no= rs.getInt("sno");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String dept = rs.getString("dept");
				student = new Student(name, no, age, dept);
			}
			return student;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			DBUtil.closeAll(rs, pstmt, DBUtil.connection);
				}

	}


	@Override
	public int getTotalCount() {//查询总数据数
		String sql = "select count(1) from student1";
		return DBUtil.getTotalCount(sql);
	}


	@Override
	public List<Student> queryStudentByPage(int currentPage, int pageSize) {
		String sql = "select * from student1 order by sno asc limit ?,?";
		Object[] params = {currentPage*pageSize,pageSize};
		
		List<Student> students = new ArrayList<>();
		ResultSet rs = DBUtil.executeQuery(sql, params);
		try {
			while(rs.next()) {
				Student student = new Student(rs.getString("name"),rs.getInt("sno"),rs.getInt("age"),rs.getString("dept"));
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return students;
	}


	@Override
	public boolean checkLoginID(String ID, String pwd){
		int count = 0;
		String sql = "select * from login where ID=? and pwd=?";
		Object[] params = {ID,pwd};
		ResultSet rs = DBUtil.executeQuery(sql, params);
		try {
			while(rs.next()) {
				count++;			}
			if(count>0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			}
				return false;
	}


	@Override
	public boolean addLoginID(String ID, String pwd) {
		// TODO Auto-generated method stub
		String sql = "insert into login(ID,pwd) values(?,?)";
		Object[] params = {ID,pwd};
		return DBUtil.executeUpdate(sql, params);
	}


	@Override
	public boolean updateLoginPwd(String ID, String pwd1) {
		String sql = "update login set pwd =? where ID=?";
		Object[] params = {pwd1,ID};
		return DBUtil.executeUpdate(sql, params);
	}


	@Override
	public boolean deleteLoginID(String ID) {
		String sql = "delete from login where ID =?";
		Object[] params = {ID};
		return DBUtil.executeUpdate(sql, params);
	}


	@Override
	public boolean IDExist(String ID) {
		String sql = "select *from login where ID = ?";
				Object[] params = {ID};
		return DBUtil.executeUpdate(sql, params);
	}


	@Override
	public boolean upLoadWork(int sno, String name) {
		int count = 0;
		String sql = "select *from student1 where sno = ? and name = ?";
		Object[] params = {sno,name};
		ResultSet rs = DBUtil.executeQuery(sql, params);
		try {
			while(rs.next()) {
				count++;
			}
			if(count>0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			}
				return false;
	}
}