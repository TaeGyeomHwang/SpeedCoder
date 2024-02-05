package view;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.BlockDAO;
import model.BlockDTO;

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
		// x 클릭시 메인으로 이동
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	dispose();
				Main main = new Main();
				main.setVisible(true);
            }
        });
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
			btnReset.addActionListener(e->{
				refreshTextArea();
	        });
		}
		return btnReset;
	}

	// 단어 추가 버튼 생성
	private JButton getBtnAdd() {
		if (btnAdd == null) {
	        btnAdd = new JButton();
	        btnAdd.setText("블록 추가");
	        btnAdd.addActionListener(e->{
	        	BlockDialogAdd dialog = new BlockDialogAdd(this);
	            dialog.setVisible(true);
	        });
	    }
	    return btnAdd;
	}

	// 단어 삭제 버튼 생성
	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new JButton();
			btnDelete.setText("블록 삭제");
			btnDelete.addActionListener(e->{
	        	BlockDialogDelete dialog = new BlockDialogDelete(this);
	            dialog.setVisible(true);
	        });
		}
		return btnDelete;
	}

	// 중앙 패널 생성
	private JPanel getPanelCenter() {
		// 패널에 텍스트필드, 텍스트에리어 부착
		if (panelCenter == null) {
			panelCenter = new JPanel();
			panelCenter.setLayout(new BorderLayout());
	        JPanel northPanel = new JPanel(new GridLayout(2, 1));
	        northPanel.add(new JLabel("블록 문제를 연습하려면 시작하기를 누른 후, 문장을 입력해주세요:"));
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
			txtCenter = new JTextArea(15, 100); // 크기 설정
            txtCenter.setEditable(false); // 편집 불가능 설정
            
            BlockDAO blockDAO = BlockDAO.getInstance();
            List<BlockDTO> blocks = blockDAO.getBlocks();
            StringBuilder sb = new StringBuilder();
            for (BlockDTO board : blocks) {
                sb.append(board.getBlockTitle()).append("\n");
            }
            txtCenter.setText(sb.toString());
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
	
	// 텍스트에리어 초기화
	void refreshTextArea() {
	    BlockDAO blockDAO = BlockDAO.getInstance();
	    List<BlockDTO> blocks = blockDAO.getBlocks();
	    StringBuilder sb = new StringBuilder();
	    for (BlockDTO board : blocks) {
	        sb.append(board.getBlockTitle()).append("\n");
	    }
	    txtCenter.setText(sb.toString());
	}
}
