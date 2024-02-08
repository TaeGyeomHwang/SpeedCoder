
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import model.WordDAO;
import model.WordDTO;

public class WordDialog extends JDialog {
	private JTextField wordField;
	private JButton addButton;
	private JButton cancelButton;

	private String id = Login.getLoginedId();
	private WordDAO dao = WordDAO.getInstance();
	private List<WordDTO> words = dao.getWords(id);

	public WordDialog(WordExercise wordExercise) {
		super(wordExercise, "단어 추가", true);
		setLayout(new BorderLayout());

		wordField = new JTextField(20);
		addButton = new JButton("추가하기");
		cancelButton = new JButton("취소");

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		inputPanel.add(new JLabel("단어:"));
		inputPanel.add(wordField);
		inputPanel.add(addButton);
		inputPanel.add(cancelButton);

		inputPanel.setBackground(new Color(250, 231, 198));
		add(inputPanel, BorderLayout.CENTER);
		
		
        
     
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText().trim();

				// 단어가 비어 있는지 확인
				if (word.isEmpty()) {
					JOptionPane.showMessageDialog(WordDialog.this, "추가할 단어가 올바르지 않습니다.", "오류",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// 단어 길이가 50자 이하인지 확인
				if (word.length() > 50) {
					JOptionPane.showMessageDialog(WordDialog.this, "추가할 단어가 올바르지 않습니다.", "오류",
							JOptionPane.ERROR_MESSAGE);
					wordField.setText("");
					return;
				}

				try {
					dao.addWord(id, word);
				} catch (SQLIntegrityConstraintViolationException ex) { // 중복된 단어일 경우 예외처리
					JOptionPane.showMessageDialog(WordDialog.this, "추가할 단어가 올바르지 않습니다.", "오류",
							JOptionPane.ERROR_MESSAGE);
					wordField.setText("");
					return;
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(WordDialog.this, "SQL에서 오류가 발생했습니다.", "오류",
							JOptionPane.ERROR_MESSAGE);
					return;
			    }

				JOptionPane.showMessageDialog(WordDialog.this, "단어를 추가했습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);

				// 단어 추가 후 텍스트필드 초기화
				wordField.setText("");
				//단어 추가 후 텍스트필드 포커스
				wordField.requestFocusInWindow();

				// 메인 화면 새로고침
				wordExercise.refreshWordList();
			}
		});
		// 엔터 키 이벤트 처리
        wordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButton.doClick(); // 추가 버튼 클릭 이벤트 실행
            }
        });
     // Esc 키 입력 시 취소 버튼 클릭 이벤트 처리
        cancelButton.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButton.doClick(); // 취소 버튼 클릭
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		

		pack();
		setLocationRelativeTo(wordExercise);
	}
}
