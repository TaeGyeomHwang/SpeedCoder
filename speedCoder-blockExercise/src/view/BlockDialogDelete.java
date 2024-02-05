package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BlockDialogDelete extends JDialog {
    private JTextField textFieldTitle;

    public BlockDialogDelete(Frame parent) {
        super(parent, "블록 삭제하기", true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 0, 5)); // 2 rows, 1 column, and 5 pixels vertical gap

        textFieldTitle = new JTextField(30);

        // 삭제하기 버튼
        JButton deleteButton = new JButton("삭제하기");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBlock();
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

        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("제목:"));
        titlePanel.add(textFieldTitle);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // 텍스트필드에 패딩 설정

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        panel.add(titlePanel);
        panel.add(buttonPanel);
        panel.setPreferredSize(new Dimension(400, 100));

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void addBlock() {
        // 컬렉션에 블록 문제 추가
        String title = textFieldTitle.getText();
        // 팝업 출력
        JOptionPane.showMessageDialog(this, "블록 문제 추가");
        // 필드 클리어
        textFieldTitle.setText("");
        // 다이얼로그 종료
        dispose();
    }
}