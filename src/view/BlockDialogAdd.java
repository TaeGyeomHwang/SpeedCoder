package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.BlockDAO;
import model.BlockDTO;

public class BlockDialogAdd extends JDialog {
	private JTextField textFieldTitle;
	private JTextArea textAreaContent;

	public BlockDialogAdd(Frame parent) {
		super(parent, "블록 추가하기", true);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		textFieldTitle = new JTextField(80);
		textAreaContent = new JTextArea(15, 80);
		JScrollPane scrollPane = new JScrollPane(textAreaContent);

		// 추가하기 버튼
		JButton addButton = new JButton("추가하기");
		addButton.addActionListener(new ActionListener() {
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
//		titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 텍스트필드에 패딩 설정

		JPanel contentPanel = new JPanel();
		contentPanel.add(new JLabel("내용:"));
		contentPanel.add(scrollPane);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);

		panel.add(titlePanel);
		panel.add(contentPanel);
		panel.add(buttonPanel);

		panel.setPreferredSize(new Dimension(800, 300));

		getContentPane().add(panel);
		pack();
		setLocationRelativeTo(parent);
	}

    // DB에 블록 문제 추가
	private void addBlock() {
		String id = "jihuhw";
	    String title = textFieldTitle.getText();
	    String content = textAreaContent.getText();

	    BlockDTO blockDTO = new BlockDTO();
	    blockDTO.setId(id); 
	    blockDTO.setBlockTitle(title);
	    blockDTO.setBlockText(content);

	    BlockDAO blockDAO = BlockDAO.getInstance();
	    blockDAO.insertBlock(blockDTO);

	    JOptionPane.showMessageDialog(this, "블록 문제가 추가되었습니다.");

	    textFieldTitle.setText("");
	    textAreaContent.setText("");

	    ((BlockExercise) getParent()).refreshTextArea();
	}
}