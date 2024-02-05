package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private static SpeedCoderDAO getInstance() {
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
}