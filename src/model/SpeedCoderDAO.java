package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpeedCoderDAO {
    private static final String url = "jdbc:mysql://222.119.100.89:3382/speedcoder";
    private static final String user = "speedcoder";
    private static final String password = "codehows213";
    private static final SpeedCoderDAO instance = new SpeedCoderDAO();

    protected Connection conn;
    protected PreparedStatement pstmt;
    protected ResultSet rs;
    protected String sql;

    protected SpeedCoderDAO() {

    }

    public static SpeedCoderDAO getInstance() {
        return instance;
    }

    protected void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    protected void close() {
    	try {
    		if (rs != null) rs.close();
    		if (pstmt != null) pstmt.close();
    		if (conn != null) conn.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

		public List<String> getWord() {
	        List<String> words = new ArrayList<>();
	        connect(); // 데이터베이스 연결
	        try {
	            String sql = "SELECT word_text FROM word";
	            pstmt = conn.prepareStatement(sql);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                words.add(rs.getString("word_text"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            close();
	        }
	        return words;
	    }
		//삭제 다이얼로그
		public void deleteWord(String word) {
		    connect(); // 데이터베이스 연결
		    try {
		        String sql = "DELETE FROM word WHERE word_text = ?";
		        pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, word);
		        pstmt.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        close();
		    }
		}
	}