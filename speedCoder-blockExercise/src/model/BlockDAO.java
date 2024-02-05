package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void insertBlock(BlockDTO blockDTO) {
        connect();
        try {
            String sql = "INSERT INTO block (id, block_title, block_text) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, blockDTO.getId());
            pstmt.setString(2, blockDTO.getBlockTitle());
            pstmt.setString(3, blockDTO.getBlockText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
}