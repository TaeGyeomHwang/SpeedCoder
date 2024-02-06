package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO extends SpeedCoderDAO{
	
	private static final ScoreDAO instance = new ScoreDAO();
	
	private ScoreDAO() {
		
	}
	
	public static ScoreDAO getInstance() {
		return instance;
	}
	
	public List<ScoreDTO> getScoreByIdDesc(String id, String block){
		List<ScoreDTO> scoreList = new ArrayList<>();
		connect();
		try {
			String sql = """
					SELECT speed, acc 
					FROM score 
					WHERE id=? AND kind=?
					ORDER BY regdate DESC
					LIMIT 10;
					""";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, block);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ScoreDTO score = new ScoreDTO();
				score.setSpeed(rs.getInt("speed"));
				score.setAcc(rs.getInt("acc"));
				scoreList.add(score);
			}
		} catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return scoreList;
	}
}
