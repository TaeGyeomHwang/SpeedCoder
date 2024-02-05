package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDAO extends SpeedCoderDAO{
	
	private static final UserInfoDAO instance = new UserInfoDAO();
	
	private UserInfoDAO() {
		
	}
	
	public static UserInfoDAO getInstance() {
		return instance;
	}

	// 등록된 사용자 정보 가져오기
    public UserInfoDTO getLoginInfo() {
    	UserInfoDTO userInfo = new UserInfoDTO();
        connect();
        try {
            String sql = "SELECT * FROM user ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                userInfo.setId(rs.getString("id"));
                userInfo.setPw(rs.getString("pw"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return userInfo;
    }
}
