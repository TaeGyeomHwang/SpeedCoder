package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpeedCoderDAO {
	static final String url = "jdbc:mysql://222.119.100.89:3382/speedcoder";
	static final String user = "speedcoder";
	static final String password = "codehows213";
	private static final SpeedCoderDAO instance = new SpeedCoderDAO();
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private String sql;
	
	public static SpeedCoderDAO getInstance() {
		return instance;
	}
	
	private static void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			instance.conn = DriverManager.getConnection(url, user, password);
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void close() throws SQLException { // 리소스 할당 해제
		if ( instance.rs != null ) instance.rs.close();
		if ( instance.pstmt != null ) instance.pstmt.close();
		if ( instance.conn != null ) instance.conn.close();
	}
}