package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.WordDAO;
import model.WordDTO;

public class DeleteWordDialog extends JDialog {
	private JTable table;
	private JButton btnDelete;
	private JButton btnCancel;
	private WordExercise parentFrame;
	
	private String id = Login.getLoginedId();
	private WordDAO dao = WordDAO.getInstance();
	private List<WordDTO> words = dao.getWords(id);

	public DeleteWordDialog(WordExercise parent) {
		super(parent, "단어 삭제", true);
		this.parentFrame = parent;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 200);
	
		
		// 테이블 모델 생성
		String[] columns = { "단어" };
		Object[][] data = new Object[words.size()][1];
		for (int i = 0; i < words.size(); i++) {
			data[i][0] = words.get(i).getWordText();
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
					// 메인 창 새로고침
					parentFrame.refreshWordList();
					// 삭제 성공 메시지 팝업
					JOptionPane.showMessageDialog(DeleteWordDialog.this, "단어를 삭제했습니다.", "성공",
							JOptionPane.INFORMATION_MESSAGE);
					
				} else {
					// 선택된 단어가 없는 경우 메시지 팝업
					JOptionPane.showMessageDialog(DeleteWordDialog.this, "단어를 선택해주세요.", "오류",
							JOptionPane.ERROR_MESSAGE);
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
		panel.setBackground(new Color(250, 231, 198));
		panel.add(scrollPane, BorderLayout.CENTER);
		JPanel btnPanel = new JPanel();
		scrollPane.setBackground(new Color(250, 231, 198));
		btnPanel.setBackground(new Color(250, 231, 198));
		btnPanel.add(btnDelete);
		btnPanel.add(btnCancel);
		panel.add(btnPanel, BorderLayout.SOUTH);
		add(panel);

		setLocationRelativeTo(parent);
		
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        // 배경색 설정
		        if (value != null && "단어".equals(value.toString())) {
		            component.setBackground(new Color(250, 231, 198)); 
		            ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
		        } else {
		            component.setBackground(table.getTableHeader().getBackground());
		        }
		        
		        return component;
		    }
		};
		  // Enter 키 입력 시 btnDelete 버튼 클릭 이벤트 처리
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
             .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickBtnDelete");

        table.getActionMap().put("clickBtnDelete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDelete.doClick();
            }
        });
		 // Esc 키 입력 시 취소 버튼 클릭 이벤트 처리
		btnCancel.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	btnCancel.doClick(); // 취소 버튼 클릭
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		//테이블 클릭은됨. 수정은 x
		table.setDefaultEditor(Object.class, null);

		// 테이블의 헤더에 렌더러를 설정합니다.
		table.getTableHeader().setDefaultRenderer(headerRenderer);
	}
}
