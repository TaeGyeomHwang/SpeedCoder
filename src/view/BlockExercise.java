package view;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class BlockExercise extends JFrame {
	private JPanel panelNorth, panelCenter;
	private JButton btnStart, btnReset, btnAdd, btnDelete, btnSave;
	private JTextField txtNorth;
	private JTextArea txtCenter;

	// 메인 윈도우 출력
	public BlockExercise() {
		this.setTitle("블록 연습");
		this.setSize(500, 500);
		this.getContentPane().add(getPanelNorth(), BorderLayout.NORTH);
		this.getContentPane().add(getPanelCenter(), BorderLayout.CENTER);
		locationCenter();
	}

	// 상단 패널 생성
	private JPanel getPanelNorth() {
		if (panelNorth == null) {
			panelNorth = new JPanel();
			// 패널에 버튼 부착
			panelNorth.add(getBtnStart());
			panelNorth.add(getBtnReset());
			panelNorth.add(getBtnAdd());
			panelNorth.add(getBtnDelete());
			panelNorth.add(getBtnSave());
			// 위쪽에 패딩 추가
			panelNorth.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		}
		return panelNorth;
	}

	// 시작하기 버튼 생성
	private JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = new JButton();
			btnStart.setText("시작하기");
		}
		return btnStart;
	}

	// 초기화 버튼 생성
	private JButton getBtnReset() {
		if (btnReset == null) {
			btnReset = new JButton();
			btnReset.setText("초기화");
		}
		return btnReset;
	}

	// 단어 추가 버튼 생성
	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton();
			btnAdd.setText("단어 추가");
		}
		return btnAdd;
	}

	// 단어 삭제 버튼 생성
	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new JButton();
			btnDelete.setText("단어 삭제");
		}
		return btnDelete;
	}

	// 저장 버튼 생성
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton();
			btnSave.setText("저장하기");
		}
		return btnSave;
	}

	// 중앙 패널 생성
	private JPanel getPanelCenter() {
		// 패널에 텍스트필드, 텍스트에리어 부착
		if (panelCenter == null) {
			panelCenter = new JPanel();
			panelCenter.setLayout(new BorderLayout());
	        JPanel northPanel = new JPanel(new GridLayout(2, 1));	
	        northPanel.add(new JLabel("문장을 입력해주세요:"));
	        northPanel.add(getTxtNorth());
	        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 텍스트필드에 여백 설정
	        panelCenter.add(northPanel, BorderLayout.NORTH);
	        panelCenter.add(new JScrollPane(getTxtCenter()), BorderLayout.CENTER);
			panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));	// 중앙 패널 여백 설정
		}
		return panelCenter;
	}

	// 텍스트 필드 생성
	private JTextField getTxtNorth() {
		if (txtNorth == null) {
			txtNorth = new JTextField();
		}
		return txtNorth;
	}

	// 텍스트 에리어 생성
	private JTextArea getTxtCenter() {
		if (txtCenter == null) {
			txtCenter = new JTextArea();
		}
		return txtCenter;
	}

	// 창 중앙 정렬
	private void locationCenter() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int leftTopX = centerPoint.x - this.getWidth() / 2;
		int leftTopY = centerPoint.y - this.getHeight() / 2;
		this.setLocation(leftTopX, leftTopY);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			BlockExercise jFrame = new BlockExercise();
			jFrame.setVisible(true);
		});
	}

}
