package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class BlockDAO extends SpeedCoderDAO {

    private static final BlockDAO instance = new BlockDAO();

    private BlockDAO() {
        
    }

    public static BlockDAO getInstance() {
        return instance;
    }

 // 블록 문제 추가하기
    public void insertBlock(BlockDTO blockDTO) throws SQLException {
        connect();
        try {
            String sql = "INSERT INTO block (id, block_title, block_text) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, blockDTO.getId());
            pstmt.setString(2, blockDTO.getBlockTitle());
            pstmt.setString(3, blockDTO.getBlockText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                throw (SQLIntegrityConstraintViolationException) e;
            } else {
                e.printStackTrace();
            }
        } finally {
            close();
        }
    }

    // id에 따라 블록 가져오기
    public List<BlockDTO> getBlockById(String id) {
    	List<BlockDTO> blockList = new ArrayList<>();
        connect();
        try {
            String sql = "SELECT * FROM block WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BlockDTO block = new BlockDTO();
                block.setId(rs.getString("id"));
                block.setBlockTitle(rs.getString("block_title"));
                block.setBlockText(rs.getString("block_text"));
                blockList.add(block);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return blockList;
    }

    // 전체 블록 가져오기
    public List<BlockDTO> getBlocks() {
        List<BlockDTO> blockList = new ArrayList<>();
        connect();
        try {
            String sql = "SELECT * FROM block";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BlockDTO block = new BlockDTO();
                block.setId(rs.getString("id"));
                block.setBlockTitle(rs.getString("block_title"));
                block.setBlockText(rs.getString("block_text"));
                blockList.add(block);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return blockList;
    }
    
    // 블록 문제 id와 title로 삭제하기
    public void deleteBoard(String id, String title) {
		try {
			connect();
			String sql = "delete from block where id=? and block_title=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, title);
			pstmt.executeUpdate();
		} catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
	}
    
    // 기록 저장하기
    public void insertScore(String id, int acc, int speed) {
    	try {
			connect();
			String sql = "INSERT INTO score (id, acc, speed, kind, regdate) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setInt(2, acc);
			pstmt.setInt(3, speed);
			pstmt.setString(4, "block");
			pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}