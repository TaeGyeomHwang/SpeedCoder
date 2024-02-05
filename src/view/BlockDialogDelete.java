package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import model.BlockDAO;
import model.BlockDTO;

public class BlockDialogDelete extends JDialog {
	private JTable table;
	private int rowIndex = -1;
	private String id = "jihuhw";

	public BlockDialogDelete(Frame parent) {
		super(parent, "블록 삭제하기", true);

		JPanel panel = new JPanel();

		// 삭제하기 버튼
		JButton deleteButton = new JButton("삭제하기");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteBlock();
			}
		});

		// 취소하기 버튼
		JButton cancelButton = new JButton("취소하기");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// 블록 목록 표시할 테이블 생성
		table = new JTable();
		// DefaultTableModel 생성, 테이블에 값 입력
		final DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.addColumn("제목");
		BlockDAO blockDAO = BlockDAO.getInstance();
		List<BlockDTO> blocks = blockDAO.getBlockById(id);
		StringBuilder sb = new StringBuilder();
		for (BlockDTO block : blocks) {
			Object[] rowData = { block.getBlockTitle() };
			tableModel.addRow(rowData);
		}
		// 사이즈 조절
		resizeColumnWidth(table);
		// 컬럼 선택 시 마우스 이벤트
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				rowIndex = table.getSelectedRow();
				if (rowIndex != -1) {
					String title = (String) table.getValueAt(rowIndex, 0);
					table.setSelectionBackground(Color.yellow);
					table.repaint();
				}
			}
		});

		JPanel titlePanel = new JPanel();
		titlePanel.add(new JLabel("삭제할 블록 문제를 선택해주세요 :"));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // 텍스트필드에 패딩 설정

		JPanel tablePanel = new JPanel();
		tablePanel.add(new JScrollPane(table));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(deleteButton);
		buttonPanel.add(cancelButton);

		panel.add(titlePanel, BorderLayout.NORTH);
		panel.add(tablePanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		panel.setPreferredSize(new Dimension(550, 550));

		getContentPane().add(panel);
		pack();
		setLocationRelativeTo(parent);
	}

	// DB에 블록 문제 삭제
	private void deleteBlock() {
		if (rowIndex != -1) {
			System.out.println(rowIndex);
			String title = (String) table.getValueAt(rowIndex, 0);
			BlockDAO blockDAO = BlockDAO.getInstance();
			blockDAO.deleteBoard(id, title);
			// 팝업 출력
			JOptionPane.showMessageDialog(this, "블록 문제 삭제");
			// 테이블, 텍스트에리어 초기화
			refreshTable();
			((BlockExercise) getParent()).refreshTextArea();
		} else {
			// 팝업 출력
			JOptionPane.showMessageDialog(this, "문제를 선택해주세요.");
		}
	}

	// JTable에서 컬럼 size 내용길이에 맞춰 조절
	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 50; // 최소 width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}
	
	// 텍스트에리어 초기화
	private void refreshTable() {
		final DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		// 기존 행 지우기
		tableModel.setNumRows(0);
		tableModel.addColumn("제목");
		BlockDAO blockDAO = BlockDAO.getInstance();
		List<BlockDTO> blocks = blockDAO.getBlockById(id);
		StringBuilder sb = new StringBuilder();
		for (BlockDTO block : blocks) {
			Object[] rowData = { block.getBlockTitle() };
			tableModel.addRow(rowData);
		}
	}
}