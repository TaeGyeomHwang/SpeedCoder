package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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