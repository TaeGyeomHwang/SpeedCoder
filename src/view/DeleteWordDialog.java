package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.SpeedCoderDAO;

public class DeleteWordDialog extends JDialog {
    private JTable table;
    private JButton btnDelete;
    private JButton btnCancel; // 취소 버튼 추가

    public DeleteWordDialog(JFrame parent) {
        super(parent, "단어 삭제", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);

        SpeedCoderDAO dao = SpeedCoderDAO.getInstance();
        List<String> words = dao.getWord();

        // 테이블 모델 생성
        String[] columns = {"단어"};
        Object[][] data = new Object[words.size()][1];
        for (int i = 0; i < words.size(); i++) {
            data[i][0] = words.get(i);
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);

        // 테이블 생성
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(250, 70));
        table.setFillsViewportHeight(true);

        // 스크롤 패널 생성
        JScrollPane scrollPane = new JScrollPane(table);

        // 삭제 버튼 생성
        btnDelete = new JButton("단어 삭제");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String word = (String) table.getValueAt(selectedRow, 0);
                    dao.deleteWord(word);
                    // 테이블 모델에서도 삭제
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        // 취소버튼
        btnCancel = new JButton("   취소   ");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        // 다이얼로그에 추가
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnDelete);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, BorderLayout.SOUTH);
        add(panel);

        setLocationRelativeTo(parent);
    }
}
