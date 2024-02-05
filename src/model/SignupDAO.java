package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SignupDAO extends SpeedCoderDAO{

	private static final SignupDAO instance = new SignupDAO();
	
	private SignupDAO() {
		
	}
	
	public static SignupDAO getInstance() {
		return instance;
	}
	
	// 회원 정보 추가하기
    public void insertSignup(SignupDTO signupDTO) {
        connect();
        try {
            String sql = "INSERT INTO user (id, pw) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, signupDTO.getId());
            pstmt.setString(2, signupDTO.getPw());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
    
    // 전체 회원 정보 가져오기
    public List<SignupDTO> getSignups() {
        List<SignupDTO> signupList = new ArrayList<>();
        connect();
        try {
            String sql = "SELECT * FROM user ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
            	SignupDTO signup = new SignupDTO();
            	signup.setId(rs.getString("id"));
            	signup.setPw(rs.getString("pw"));
            	signupList.add(signup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return signupList;
    }
    
}
