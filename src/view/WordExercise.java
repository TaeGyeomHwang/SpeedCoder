package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import model.WordDAO;

public class WordExercise extends JFrame {
	private JPanel topPanel, middlePanel, bottomPanel;
	private JButton btnStart, btnReset, btnAddWord, btnDeleteWord;
	private JTextArea txtContent;
	private JTextField textEnter;
	private JLabel txtlbl, lblWordList;
	private Timer timer;
	private long startTime;
	private int enterCount;
	private int enter;
	private JLabel labelMin;
	private JLabel labelSec;
	private boolean terminationFlag;
	private long beforeTime;
	private Thread timerThread;
	private JPanel panelTimer;
	private JComponent colon1;

	public WordExercise() {
		setTitle("단어 연습");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		add(getTopPanel(), BorderLayout.NORTH);
		add(getMiddlePanel(), BorderLayout.CENTER);
		add(getBottomPanel(), BorderLayout.SOUTH);

		// 시작 버튼을 누른 후에만 텍스트 필드에서 Enter를 눌렀을 때 동작하도록 설정
		textEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 입력된 단어 가져오기
				String enteredWord = textEnter.getText().trim();

				// 텍스트 영역에서 해당 단어 삭제
				deleteEnteredWord(enteredWord);

				// Enter 입력 횟수 증가
				enterCount++;
				enter += enteredWord.length();
				// 입력란 초기화
				textEnter.setText("");

				// 게임 종료 확인
				checkGameEnd();
			}
		});
		setTimer();

		setLocationRelativeTo(null);
	}

	// 상단 패널(버튼 추가)
	private JPanel getTopPanel() {
		if (topPanel == null) {
			topPanel = new JPanel();
			btnStart = new JButton("시작하기");
			btnReset = new JButton("초기화");
			btnAddWord = new JButton("단어 추가");
			btnDeleteWord = new JButton("단어 삭제");
			topPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
//			topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
			topPanel.setPreferredSize(new Dimension(500, 100));
			// 시작버튼
			btnStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 시작버튼 눌렀을 때 변수 초기화
					beforeTime = System.currentTimeMillis();
					enter = 0;
					enterCount = 0;
					// 시작하기 버튼을 누르면 밀리세컨드 단위로 타이머 시작
					startTime = System.currentTimeMillis();
					timer.start();
					// 패널 타이머 시작
					startTimer();

					// 데이터베이스에서 단어 목록 가져오기
					WordDAO dao = WordDAO.getInstance();
					List<String> words = dao.getWord();

					// 단어 개수 확인
					int wordCount = words.size();

					// 게임을 시작할 수 있는지 확인
					if (wordCount < 15) {
						JOptionPane.showMessageDialog(WordExercise.this, "단어가 15개 미만입니다. 게임을 시작할 수 없습니다.", "오류",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					// 랜덤하게 단어 배치하기
					placeWordsRandomly();

					// 시작 버튼을 누르면 단어 추가, 삭제 버튼 비활성화
					btnAddWord.setEnabled(false);
					btnDeleteWord.setEnabled(false);
					// 시작 버튼을 누르면 텍스트필드에 포커스
					textEnter.requestFocusInWindow();
				}
			});

			// 초기화 버튼 동작
			btnReset.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 타이머 종료
					timer.stop();
					// 시작 시간 초기화
					startTime = 0;

					// 텍스트 영역 초기화
					txtContent.setText("");

					// "단어 목록 : " 라벨 보이도록 설정
					lblWordList.setVisible(true);

					// 단어 목록 갱신
					refreshWordList();

					// 시작, 추가, 삭제 버튼 활성화
					btnAddWord.setEnabled(true);
					btnDeleteWord.setEnabled(true);

					resetTimer();
				}
			});
			// 단어 추가 버튼 동작
			btnAddWord.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WordDialog dialog = new WordDialog(WordExercise.this);
					dialog.setVisible(true);
				}
			});

			// 단어 삭제 버튼 동작
			btnDeleteWord.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DeleteWordDialog dialog = new DeleteWordDialog(WordExercise.this);
					dialog.setVisible(true);
				}
			});

			topPanel.add(btnStart);
			topPanel.add(btnReset);
			topPanel.add(btnAddWord);
			topPanel.add(btnDeleteWord);
			topPanel.add(getPanelTimer());
		}
		return topPanel;
	}

	// 중앙 패널
	public JPanel getMiddlePanel() {
		if (middlePanel == null) {
			middlePanel = new JPanel();
			middlePanel.setLayout(new BorderLayout());

			// "단어 목록 : " 라벨을 생성하여 텍스트 영역에 추가
			lblWordList = new JLabel("단어 목록 : ");
			middlePanel.add(lblWordList, BorderLayout.NORTH);

			// 단어 목록을 보여줄 텍스트 영역
			txtContent = new JTextArea(15, 100);
			JScrollPane scrollPane = new JScrollPane(txtContent);

			// 텍스트 영역의 글자 색 설정
			txtContent.setForeground(Color.BLACK);

			middlePanel.add(scrollPane, BorderLayout.CENTER);

			// 텍스트 영역의 내용 수정을 방지하기 위해 편집 불가능하게 설정
			txtContent.setEditable(false);

			// 초기화할 때 데이터베이스에서 단어 목록을 가져와 표시
			refreshWordList();
		}
		return middlePanel;
	}

	// 하단 패널
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new BorderLayout());

			txtlbl = new JLabel("단어를 입력하세요:    ");
			textEnter = new JTextField(35);

			JPanel centerPanel = new JPanel();

			centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
			centerPanel.add(txtlbl);
			centerPanel.add(textEnter);

			txtlbl.setFont(txtlbl.getFont().deriveFont(15.0f));

			bottomPanel.add(centerPanel, BorderLayout.CENTER);
		}
		return bottomPanel;
	}

	// 단어 목록을 다시 가져와서 텍스트 영역에 표시함
	public void refreshWordList() {
		WordDAO dao = WordDAO.getInstance();
		List<String> words = dao.getWord();

		// 텍스트 영역을 초기화
		txtContent.setText("");

		// 새로운 단어 목록을 추가
		for (String word : words) {
			txtContent.append(word + "\n");
		}
	}

	// 입력된 단어를 삭제하는 메서드
	private void deleteEnteredWord(String enteredWord) {
		// 텍스트 영역에서 입력된 단어가 포함된 라인을 찾아서 삭제
		String content = txtContent.getText();
		String[] lines = content.split("\n");
		StringBuilder updatedContent = new StringBuilder();

		boolean isFirstLine = true; // 첫 번째 줄인지 여부를 나타내는 변수

		// 각 줄을 확인하면서 입력된 단어와 일치하는 줄을 제외하고 다시 텍스트에 추가
		for (String line : lines) {
			// 첫 번째 줄이 아닌 경우에만 줄 바꿈 추가
			if (!isFirstLine) {
				updatedContent.append("\n");
			} else {
				isFirstLine = false; // 첫 번째 줄이 아니므로 플래그 업데이트
			}

			// 입력된 단어와 현재 줄의 내용을 비교하여 일치하지 않는 경우에만 추가
			if (!line.trim().equals(enteredWord)) {
				updatedContent.append(line);
			}
		}

		// 텍스트 영역 업데이트
		txtContent.setText(updatedContent.toString());

		// 단어가 모두 삭제되면 게임 종료 후 초기 상태로 복귀
		if (txtContent.getText().isEmpty()) {
			// 게임 종료 후 초기 상태로 복귀
			btnAddWord.setEnabled(true);
			btnDeleteWord.setEnabled(true);
		}
	}

	// 랜덤하게 단어 배치하는 메서드
	private void placeWordsRandomly() {
		// 데이터베이스에서 단어 목록 가져오기
		WordDAO dao = WordDAO.getInstance();
		List<String> words = dao.getWord();

		// 텍스트 영역 초기화
		txtContent.setText("");

		// 단어 목록을 섞음
		Collections.shuffle(words);

		// 단어 개수를 최대 15개로 제한
		int maxWords = Math.min(15, words.size());

		// 텍스트 영역에 단어를 한 행에 한 단어씩 배치
		for (int i = 0; i < maxWords; i++) {
			// 랜덤한 공백 추가
			int maxSpaces = Math.min(120, (int) (Math.random() * 121)); // 최대 120개의 공백
			StringBuilder wordWithSpaces = new StringBuilder();
			for (int j = 0; j < maxSpaces; j++) {
				wordWithSpaces.append(" ");
			}
			wordWithSpaces.append(words.get(i));

			txtContent.append(wordWithSpaces.toString() + "\n");
		}
	}

	// 타이머 설정 메서드
	private void setTimer() {
		timer = new Timer(1, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long elapsedTimeInMillis = System.currentTimeMillis() - startTime;
				// 여기에 타이머 작업을 추가할 수 있습니다.
			}
		});
	}

	// 게임 종료 확인 메서드
	private void checkGameEnd() {
		// 텍스트 영역의 내용을 가져옴
		String content = txtContent.getText().trim();

		// 텍스트 영역이 비어있으면 모든 단어가 입력된 것으로 간주하여 게임 종료
		if (content.isEmpty()) {
			// 게임 종료 후 초기 상태로 복귀
			btnAddWord.setEnabled(true);
			btnDeleteWord.setEnabled(true);
			stopTimer();
			// 타수와 정확도 계산 및 팝업 표시
			calculate();

			resetTimer();

		}
	}

	private void calculate() {
		// 현재 시간을 가져옴
		long endTime = System.currentTimeMillis();
		// 게임이 종료된 시간에서 시작 시간을 뺌으로써 총 걸린 시간을 계산
		long elapsedTimeInMillis = endTime - startTime;
		// 밀리초를 초로 변환
		double elapsedSeconds = elapsedTimeInMillis / 1000.0;

		// 타수 계산: (총 입력한 문자 수 * 60) / 걸린 시간(초)
		// 총 입력한 문자 수는 총 입력 횟수(enter를 눌렀을 때 입력한 모든 단어 갯수를 합하고, enterCount를 뺀 것)
		double typingSpeed = (enter * 60.0) / elapsedSeconds;
		int speed = (int) Math.round(typingSpeed);

		// 정확도 계산
		double accuracy = 100 - ((((double) enterCount - 15.0) / 15.0) * 100.0);
		int acc = (int) Math.round(accuracy);

		// 팝업으로 타수와 정확도 표시
		JOptionPane.showMessageDialog(this, "타수 : " + speed + "\n" + "정확도 : " + acc);

		// 단어 목록 갱신
		refreshWordList();

	}

	// 타이머 패널 생성
	private JPanel getPanelTimer() {
		if (panelTimer == null) {
			// 패널, 라벨 생성, 부착
			panelTimer = new JPanel();
			panelTimer.setPreferredSize(new Dimension(450, 70));
			colon1 = new JLabel(" : ");
			labelMin = new JLabel("00");
			labelSec = new JLabel("00");
			panelTimer.add(labelMin);
			panelTimer.add(colon1);
			panelTimer.add(labelSec);
			// 라벨 폰트, 크기 설정
			labelMin.setFont(new Font("courier", Font.BOLD, 30));
			labelSec.setFont(new Font("courier", Font.BOLD, 30));
			colon1.setFont(new Font("courier", Font.BOLD, 30));
		}
		return panelTimer;
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
		// 타이머 라벨 초기화
		labelMin.setText("00");
		labelSec.setText("00");
		// 타이머 중지
		terminationFlag = false;
	}

	// 타이머 중지
	private void stopTimer() {
		// 타이머 중지
		terminationFlag = false;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			WordExercise wordExercise = new WordExercise();
			wordExercise.setVisible(true);
		});
	}
}