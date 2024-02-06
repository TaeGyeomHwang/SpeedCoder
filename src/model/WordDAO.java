package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import view.Login;

public class WordDAO extends SpeedCoderDAO {

	private static final WordDAO instance = new WordDAO();

	private WordDAO() {

	}

	public static WordDAO getInstance() {
		return instance;
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

//전체 단어 가져오기
	public List<WordDTO> getWords() {
		List<WordDTO> wordList = new ArrayList<>();
		connect();
		try {
			String sql = "SELECT * FROM word";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				WordDTO word = new WordDTO();
				word.setId(rs.getString("id"));
				word.setWordText(rs.getString("word_text"));
				wordList.add(word);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return wordList;
	}

	public void addWord(String id, String word) {
		connect(); // 데이터베이스 연결
		try {
			String sql = "INSERT INTO word (id ,word_text) VALUES (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, word);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

// 단어 목록 갱신
	public void refreshWordList(List<String> wordList) {
		wordList.clear();
		wordList.addAll(getWord());
	}

// 삭제 다이얼로그
	public void deleteWord(String word) {
		connect(); // 데이터베이스 연결
		try {
			String sql = "DELETE FROM word WHERE word_text = ? and id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, word);
			pstmt.setString(2, Login.getLoginedId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// 게임 종료 후 스코어 저장
	public void insertScore(int acc, int speed) {
		connect(); // 데이터베이스 연결
		try {
			String sql = "INSERT INTO score (id, acc, speed, kind, regdate) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Login.getLoginedId());
			pstmt.setInt(2, acc);
			pstmt.setInt(3, speed);
			pstmt.setString(4, "word");
			pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// 사용자별 스코어 검색
	public List<ScoreDTO> getScores(String id) {
		List<ScoreDTO> scores = new ArrayList<>();
		connect(); // 데이터베이스 연결
		try {
			String sql = "SELECT * FROM score WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ScoreDTO score = new ScoreDTO();
				score.setId(rs.getString("id"));
				score.setKind(rs.getString("kind"));
				score.setSpeed(rs.getInt("speed"));
				score.setAcc(rs.getInt("acc"));
				scores.add(score);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return scores;
	}
}
