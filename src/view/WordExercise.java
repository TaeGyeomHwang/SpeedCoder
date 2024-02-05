package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.SpeedCoderDAO;

public class WordExercise extends JFrame {
    private JPanel topPanel, middlePanel, bottomPanel;
    private JButton btnStart, btnReset, btnAddWord, btnDeleteWord;
    private JTextArea txtContent;
    private JTextField textEnter;
    private JLabel txtlbl;

    public WordExercise() {
        setTitle("단어 연습");
        setSize(500, 400);
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

            // 시작하기 버튼 동작
            btnStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 데이터베이스에서 단어 목록을 다시 가져옴
                    SpeedCoderDAO dao = SpeedCoderDAO.getInstance();
                    List<String> words = dao.getWord();
                    // 텍스트 영역을 초기화
                    txtContent.setText("");

                    // 텍스트 영역에 단어 배치
                    Random random = new Random();
                    for (int i = 0; i < 15; i++) {
                        // 좌우 랜덤으로 배치하도록 함
                        String word = words.get(random.nextInt(words.size()));
                        if (random.nextBoolean()) {
                            txtContent.append(word + "\t");
                        } else {
                            txtContent.append("\t" + word);
                        }
                        txtContent.append("\n");
                    }
                }
            });

            // 초기화 버튼 동작
            btnReset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
            txtContent = new JTextArea(15, 100);
            JScrollPane scrollPane = new JScrollPane(txtContent);
            middlePanel.add(scrollPane);

            SpeedCoderDAO dao = SpeedCoderDAO.getInstance();
            List<String> words = dao.getWord();
            for (String word : words) {
                txtContent.append(word + "\n");
            }
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

    public JTextArea getTxtContent() {
        return txtContent;
    }

    private void WordDialog() {
        WordDialog dialog = new WordDialog(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WordExercise wordExercise = new WordExercise();
            wordExercise.setVisible(true);
        });
    }
}
