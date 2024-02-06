package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import model.BlockDAO;
import model.BlockDTO;

public class BlockExercise extends JFrame {
	// 컴포넌트
	private JPanel panelNorth, panelCenter, panelTimer, panelInfo;
	private JButton btnStart, btnReset, btnAdd, btnDelete;
	private JTextField txtNorth;
	private JTextPane txtCenterPane;
	private JLabel labelCenter, labelMin, labelSec, colon1, currentSpeed, currentAcc;
	private String inputText;

	// 랜덤 인덱스 접근 변수
	private String id = Login.getLoginedId();
	private int index = 0;
	private int randomIndex = 0;

	// 타수, 정확도 사용 변수
	private double speed = 0.0;
	private long beforeTime = 0;
	private long inputStartTime = 0;
	private long inputEndTime = 0;
	private long inputTotalTime = 0;
	private double acc = 0.0;
	private double totalChar = 0.0;
	private int inputCount = 0;
	private int enterCount;

	// DAO, DTO
	private BlockDAO blockDAO = BlockDAO.getInstance();
	private List<BlockDTO> blocks = blockDAO.getBlockById(id);

	// 타이머 쓰레드 객체
	private Thread timerThread;
	private boolean terminationFlag = false;

	// 메인 윈도우 출력
	public BlockExercise() {
		this.setTitle("블록 연습");
		this.setSize(500, 650);
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
			panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));
			JPanel btnPanel = new JPanel(new FlowLayout());
			JPanel infoPanel = new JPanel(new FlowLayout());
			JPanel timerPanel = new JPanel(new FlowLayout());
			btnPanel.add(getBtnStart());
			btnPanel.add(getBtnReset());
			btnPanel.add(getBtnAdd());
			btnPanel.add(getBtnDelete());
			timerPanel.add(getPanelTimer());
			infoPanel.add(getPanelInfo());
			panelNorth.add(btnPanel);
			panelNorth.add(timerPanel);
			panelNorth.add(infoPanel);
			panelNorth.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		}
		return panelNorth;
	}

	// 시작하기 버튼 생성
	private JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = new JButton();
			btnStart.setText("시작하기");
			// 블록 연습 시작하기
			btnStart.addActionListener(e -> {
				enterCount = 0;
				try {
					gameStart();
					startTimer(); // 타이머 시작
				} catch (IndexOutOfBoundsException ex) {
					JOptionPane.showMessageDialog(this, "블록 문제가 없습니다");
					resetTimer(); // 타이머 초기화
					refreshTextArea(); // 텍스트 영역 초기화
				}
			});
		}
		return btnStart;
	}

	// 초기화 버튼 생성
	private JButton getBtnReset() {
		if (btnReset == null) {
			btnReset = new JButton();
			btnReset.setText("초기화");
			btnReset.addActionListener(e -> {
				// 타이머 초기화
				stopTimer();
				resetTimer();
				refreshTextArea(); // 텍스트 영역 초기화
			});
		}
		return btnReset;
	}

	// 단어 추가 버튼 생성
	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton();
			btnAdd.setText("블록 추가");
			btnAdd.addActionListener(e -> {
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
			btnDelete.addActionListener(e -> {
				BlockDialogDelete dialog = new BlockDialogDelete(this);
				dialog.setVisible(true);
			});
		}
		return btnDelete;
	}

	// 타이머 패널 생성
	private JPanel getPanelTimer() {
		if (panelTimer == null) {
			// 패널, 라벨 생성, 부착
			panelTimer = new JPanel();
			colon1 = new JLabel(" : ");
			labelMin = new JLabel("00");
			labelSec = new JLabel("00");
			panelTimer.add(labelMin);
			panelTimer.add(colon1);
			panelTimer.add(labelSec);
			// 라벨 폰트 설정
			labelMin.setFont(new Font("courier", Font.BOLD, 30));
			labelSec.setFont(new Font("courier", Font.BOLD, 30));
			colon1.setFont(new Font("courier", Font.BOLD, 30));
		}
		return panelTimer;
	}

	// 현재 정보 라벨 생성
	private JPanel getPanelInfo() {
		if (panelInfo == null) {
			// 패널 생성, 레이아웃 설정
			panelInfo = new JPanel();
			panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
			JLabel label1 = new JLabel("현재 타수 : ");
			currentSpeed = new JLabel("-타/분");
			JLabel label2 = new JLabel(" 현재 정확도 : ");
			currentAcc = new JLabel("-%");
			// 서브 패널 생성, 컴포넌트 부착
			JPanel speedPanel = new JPanel();
			JPanel accPanel = new JPanel();
			speedPanel.add(label1);
			speedPanel.add(currentSpeed);
			accPanel.add(label2);
			accPanel.add(currentAcc);
			// 패널에 서브패널 부착
			panelInfo.add(speedPanel);
			panelInfo.add(accPanel);
			// 라벨 폰트 설정
			currentSpeed.setFont(new Font("courier", Font.BOLD, 15));
			label1.setFont(new Font("courier", Font.BOLD, 15));
			currentAcc.setFont(new Font("courier", Font.BOLD, 15));
			label2.setFont(new Font("courier", Font.BOLD, 15));
		}
		return panelInfo;
	}

	// 중앙 패널 생성
	private JPanel getPanelCenter() {
		// 패널에 텍스트필드, 텍스트에리어 부착
		if (panelCenter == null) {
			panelCenter = new JPanel();
			labelCenter = new JLabel();
			panelCenter.setLayout(new BorderLayout());
			JPanel northPanel = new JPanel(new GridLayout(2, 1));
			labelCenter.setText("블록 문제를 연습하려면 시작하기를 눌러주세요:");
			northPanel.add(labelCenter);
			northPanel.add(getTxtNorth());
			northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 텍스트필드에 여백 설정
			panelCenter.add(northPanel, BorderLayout.NORTH);
			panelCenter.add(new JScrollPane(getTxtCenterPane()), BorderLayout.CENTER); // JTextPane으로 변경
			panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 중앙 패널 여백 설정
		}
		return panelCenter;
	}

	// 텍스트 필드 생성
	private JTextField getTxtNorth() {
		if (txtNorth == null) {
			txtNorth = new JTextField();
			txtNorth.setEditable(false);
			txtNorth.addActionListener(e -> {
				enterCount++;
				inputCount = 0;
				inputEndTime = System.currentTimeMillis();
				inputTotalTime += inputEndTime - inputStartTime;
				inputText = txtNorth.getText();
				txtNorth.setText("");
				validateText(inputText);
			});
			txtNorth.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					inputCount++;
					if (inputCount == 1) {
						inputStartTime = System.currentTimeMillis();
					}
				}
			});
		}
		return txtNorth;
	}

	// 텍스트 에리어 생성
	private JTextPane getTxtCenterPane() {
		if (txtCenterPane == null) {
			txtCenterPane = new JTextPane();
			txtCenterPane.setEditable(false); // 편집 불가능 설정

			// 기본 스타일 지정
			Style defaultStyle = txtCenterPane.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setForeground(defaultStyle, txtCenterPane.getForeground());
			StyleConstants.setBackground(defaultStyle, txtCenterPane.getBackground());
			StyleConstants.setFontFamily(defaultStyle, txtCenterPane.getFont().getFamily());
			StyleConstants.setFontSize(defaultStyle, txtCenterPane.getFont().getSize());

			StringBuilder sb = new StringBuilder();
			sb.append("현재 블록 문제 목록: \n\n");
			for (BlockDTO board : blocks) {
				sb.append(board.getBlockTitle()).append("\n");
			}
			txtCenterPane.setText(sb.toString());
		}
		return txtCenterPane;
	}

	// 창 중앙 정렬
	private void locationCenter() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int leftTopX = centerPoint.x - this.getWidth() / 2;
		int leftTopY = centerPoint.y - this.getHeight() / 2;
		this.setLocation(leftTopX, leftTopY);
	}

	// 화면 초기화
	void refreshTextArea() {
		// 텍스트에리어 초기화
		blocks = blockDAO.getBlockById(id);
		StringBuilder sb = new StringBuilder();
		sb.append("현재 블록 문제 목록: \n\n");
		for (BlockDTO board : blocks) {
			sb.append(board.getBlockTitle()).append("\n");
		}
		txtCenterPane.setText(sb.toString());
		currentSpeed.setText("-타/분");
		currentAcc.setText("-%");

		// 컴포넌트 초기화
		btnAdd.setEnabled(true);
		btnDelete.setEnabled(true);
		txtNorth.setEditable(false);
		labelCenter.setText("블록 문제를 연습하려면 시작하기를 눌러주세요:");

		// 스타일 초기화
		txtCenterPane.setCharacterAttributes(txtCenterPane.getStyle(StyleContext.DEFAULT_STYLE), true);
	}

	// 엔터 입력시 정답인지 검증
	private void validateText(String input) throws IndexOutOfBoundsException {
		// 랜덤 블록 문제 선택
		StringBuilder sb = new StringBuilder();
		sb.append(blocks.get(randomIndex).getBlockText());
		// 가져온 문제를 줄바꿈을 기준으로 파싱
		String lines[] = sb.toString().split("\\r?\\n");
		sb.setLength(0);
		// 입력한 값의 길이가 하이라이트된 문장의 사이즈와 동일할 경우, 다음 문장 출력
		if (input.length() == lines[index].trim().length()) {
			// 맞은 문자 개수
			for (int j = 0; j < lines[index].trim().length(); j++) {
				if (input.charAt(j) == lines[index].trim().charAt(j)) {
					acc++;
				}
				totalChar++;
			}
			// 인덱스 증가, 남은 문장 출력
			index++;
			for (int i = index; i < lines.length; i++) {
				sb.append(lines[i]).append("\n");
			}
			txtCenterPane.setText(sb.toString());
			// 하이라이트 적용
			if (index < lines.length) {
				highlightFirstLine(txtCenterPane);
			}
			// 현재 타수 리프레쉬
//			long diffSec = (inputTotalTime / enterCount) / 1000;
//			double speed = totalChar * 60 / diffSec;
//			currentSpeed.setText(Math.round(speed)+"타/분");
		}
		// 시작하기 버튼 눌렀을 경우
		else if (input.equals("초기 문제 출력")) {
			for (String s : lines) {
				sb.append(s).append("\n");
			}
			txtCenterPane.setText(sb.toString());
			// 하이라이트 적용
			highlightFirstLine(txtCenterPane);
		}
		// 모든 문장을 입력했을 경우
		if (index == lines.length) {
			// 정확도 계산
			acc = (acc / totalChar) * 100;
			// 타수 계산
			long diffSec = (inputTotalTime / enterCount) / 1000;
			speed = totalChar * 60 / diffSec;
			// 기록 DB에 저장
			blockDAO.insertScore(id, (int) acc, (int) Math.round(speed));
			stopTimer();
			// 정확도, 타수 출력
			JOptionPane.showMessageDialog(this, "타수: " + (int) Math.round(speed) + "타/분\n정확도: " + (int) acc + "%");
			// 변수 초기화
			index = 0;
			acc = 0.0;
			totalChar = 0.0;
			speed = 0.0;
			// 화면 초기화
			resetTimer();
			refreshTextArea();
		}
	}

	// 블록 문제 시작하기
	private void gameStart() throws IndexOutOfBoundsException {
		// 타이머 시작
		beforeTime = System.currentTimeMillis();
		// 접근할 인덱스 랜덤으로 설정
		randomIndex = (int) (Math.random() * blocks.size());
		// 추가, 삭제 버튼 비활성화
		btnAdd.setEnabled(false);
		btnDelete.setEnabled(false);
		// 텍스트필드 활성화, 포커스
		txtNorth.setEditable(true);
		txtNorth.requestFocusInWindow();
		labelCenter.setText("문장을 입력한 후 엔터를 눌러주세요:");
		// 블록 문제 출력
		validateText("초기 문제 출력");
	}

	private void highlightFirstLine(JTextPane textPane) {
	    StyledDocument doc = textPane.getStyledDocument();
	    Style style = textPane.addStyle("highlight", null);
	    StyleConstants.setBackground(style, Color.YELLOW);

	    // 이전 강조 지우기
	    doc.setCharacterAttributes(0, doc.getLength(), textPane.getStyle(StyleContext.DEFAULT_STYLE), true);

	    // 첫 번째 문장 찾기
	    int endOfFirstLine = 0;
	    try {
	        endOfFirstLine = doc.getText(0, doc.getLength()).indexOf("\n");
	        if (endOfFirstLine < 0) {
	            endOfFirstLine = doc.getLength(); // 문장이 하나면 끝까지 강조
	        }
	    } catch (BadLocationException e) {
	        e.printStackTrace();
	    }

	    // 첫 번째 문장 강조
	    doc.setCharacterAttributes(0, endOfFirstLine, style, true);
	}

	// 타이머 시작
	private void startTimer() {
		terminationFlag = true;
		// 타이머 쓰레드 실행
		timerThread = new Thread(() -> {
			while (terminationFlag) {
				updateTimer();
				try {
					Thread.sleep(1000); // 1초마다 업데이트
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		timerThread.start();
	}

	// 타이머 업데이트
	private void updateTimer() {
		// 지나간 시간 계산
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - beforeTime;
		long seconds = elapsedTime / 1000;
		long minutes = seconds / 60;
		long remainingSeconds = seconds % 60;

		// 타이머 라벨 업데이트
		SwingUtilities.invokeLater(() -> {
			labelMin.setText(String.format("%02d", minutes));
			labelSec.setText(String.format("%02d", remainingSeconds));
		});
	}

	// 타이머 초기화
	private void resetTimer() {
		labelMin.setText("00");
		labelSec.setText("00");

	}

	// 타이머 정지
	private void stopTimer() {
		terminationFlag = false;
	}
}
