package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.WordDAO;

public class WordExercise extends JFrame {
	private JPanel topPanel, middlePanel, bottomPanel;
	private JButton btnStart, btnReset, btnAddWord, btnDeleteWord;
	private JTextArea txtContent;
	private JTextField textEnter;
	private JLabel txtlbl, lblWordList;
	
	public WordExercise() {
		setTitle("단어 연습");
		setSize(500, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		add(getTopPanel(), BorderLayout.NORTH);
		add(getMiddlePanel(), BorderLayout.CENTER);
		add(getBottomPanel(), BorderLayout.SOUTH);

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

			topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
			topPanel.setPreferredSize(new Dimension(500, 60));
			// 시작버튼
			btnStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
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

					// 시작 버튼을 누른 후에만 텍스트 필드에서 Enter를 눌렀을 때 동작하도록 설정
					textEnter.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// 입력된 단어 가져오기
							String enteredWord = textEnter.getText().trim();

							// 텍스트 영역에서 해당 단어 삭제
							deleteEnteredWord(enteredWord);

							// 입력란 초기화
							textEnter.setText("");
						}
					});

					// 데이터베이스에서 삭제하지 않고 텍스트 에리아에서만 삭제
					textEnter.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String enteredWord = textEnter.getText().trim();
							if (!enteredWord.isEmpty()) {
								// 텍스트 에리아에서 입력된 단어 삭제
								deleteEnteredWord(enteredWord);
								textEnter.setText(""); // 입력란 초기화
							}
						}
					});
					
					
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
					// 텍스트 영역 초기화
					txtContent.setText("");

					// "단어 목록 : " 라벨 보이도록 설정
					lblWordList.setVisible(true);

					// 단어 목록 갱신
					refreshWordList();

					// 시작, 추가, 삭제 버튼 활성화
					btnAddWord.setEnabled(true);
					btnDeleteWord.setEnabled(true);
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
		// 텍스트 영역에서 입력된 찾아서 삭제
		String content = txtContent.getText();
		content = content.replace(enteredWord, "");
		txtContent.setText(content);

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
			int maxSpaces = Math.min(120, (int) (Math.random() * 121)); // 최대 40개의 공백
			StringBuilder wordWithSpaces = new StringBuilder();
			for (int j = 0; j < maxSpaces; j++) {
				wordWithSpaces.append(" ");
			}
			wordWithSpaces.append(words.get(i));

			txtContent.append(wordWithSpaces.toString() + "\n");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			WordExercise wordExercise = new WordExercise();
			wordExercise.setVisible(true);
		});
	}
}
