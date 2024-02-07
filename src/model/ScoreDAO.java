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
	
	// id와 연습 종류(kind)에 따라 날짜 최신순으로 (타속, 정확도)데이터를 10개를 리스트에 저장
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
	
	//보여줄 10개의 데이터 기록에 해당하는 평균 타속
	public int getSpeedAvg(String id, String block) {
		int speedAvg = 0;
		connect();
		try {
			String sql = """
					SELECT AVG(speed) as speed_Avg
					FROM score
					WHERE id=? AND kind=?
					ORDER BY regdate DESC
					LIMIT 10;
					""";
					
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, block);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				speedAvg = rs.getInt("speed_Avg");
			}else {
				
			}
		}catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return speedAvg;
	}
	
	//보여줄 10개의 데이터 기록에 해당하는 평균 타속
		public int getAccAvg(String id, String block) {
			int accAvg = 0;
			connect();
			try {
				String sql = """
						SELECT AVG(acc) as acc_Avg
						FROM score
						WHERE id=? AND kind=?
						ORDER BY regdate DESC
						LIMIT 10;
						""";
						
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, block);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					accAvg = rs.getInt("acc_Avg");
				}else {
					
				}
			}catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            close();
	        }
	        return accAvg;
		}
}
