package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.SpeedCoderDAO;

public class WordDialog extends JDialog {
    private JTextField wordField;
    private JButton saveButton;
    private JButton cancelButton;
    private WordExercise wordex;

    public WordDialog(WordExercise wordex) {
        super(wordex, "단어 추가", true);
        this.wordex = wordex;
        setLayout(new BorderLayout());

        wordField = new JTextField(20);
        saveButton = new JButton("단어 추가");
        cancelButton = new JButton("    취소    ");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        inputPanel.add(wordField, BorderLayout.CENTER);
        inputPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText().trim();

                // 공백 체크
                if (word.isEmpty()) {
                    JOptionPane.showMessageDialog(WordDialog.this, "추가할 단어가 올바르지 않습니다", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 길이 체크
                if (word.length() > 50) {
                    JOptionPane.showMessageDialog(WordDialog.this, "추가할 단어가 올바르지 않습니다", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 중복 체크
                SpeedCoderDAO dao = SpeedCoderDAO.getInstance();
                List<String> words = dao.getWord();
                if (words.contains(word)) {
                    JOptionPane.showMessageDialog(WordDialog.this, "추가할 단어가 이미 존재합니다", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 모든 조건 통과 시 단어 추가
                dao.addWord(word);
                wordex.refreshWordList();
                dispose(); // 다이얼로그 닫기
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 다이얼로그 닫기
            }
        });
      
        pack();
        setLocationRelativeTo(wordex);
    }
}
