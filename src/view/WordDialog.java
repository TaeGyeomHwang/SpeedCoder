package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WordDialog extends JDialog {
    private JTextField wordField;
    private JButton saveButton;
    private JButton cancelButton;

    public WordDialog(WordExercise wordex) {
        super(wordex, "단어 추가", true);
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
                // 저장 버튼 동작 추가
                dispose(); // 다이얼로그 닫기
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 취소 버튼 동작 추가
                dispose(); // 다이얼로그 닫기
            }
        });
      
        pack();
        setLocationRelativeTo(wordex);
    }
}
