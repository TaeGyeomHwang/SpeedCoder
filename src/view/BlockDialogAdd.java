package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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

import exceptions.EmptyTitleContentException;
import exceptions.TitleOverException;
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
		titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 텍스트필드에 패딩 설정

		JPanel contentPanel = new JPanel();
		contentPanel.add(new JLabel("내용:"));
		contentPanel.add(scrollPane);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);

		panel.add(titlePanel);
		panel.add(contentPanel);
		panel.add(buttonPanel);
		titlePanel.setBackground(new Color(170, 207,243));
		contentPanel.setBackground(new Color(170, 207,243));
		buttonPanel.setBackground(new Color(170, 207,243));

		panel.setPreferredSize(new Dimension(800, 350));

		getContentPane().add(panel);
		pack();
		setLocationRelativeTo(parent);

		textFieldTitle.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode== KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		});
		textAreaContent.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode== KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		});
	}

	// DB에 블록 문제 추가
	private void addBlock() {
	    try {
	        String id = Login.getLoginedId();
	        String title = textFieldTitle.getText();
	        String content = textAreaContent.getText();

	        // 공백을 입력할 경우 예외처리
	        if (title.isEmpty()) {
	            throw new EmptyTitleContentException();
	        }
	        if (content.isEmpty()) {
	        	throw new EmptyTitleContentException();
	        }
	        if(title.length()>=50) {
	        	throw new TitleOverException();
	        }

	        BlockDTO blockDTO = new BlockDTO();
	        blockDTO.setId(id);
	        blockDTO.setBlockTitle(title);
	        blockDTO.setBlockText(content);

	        BlockDAO blockDAO = BlockDAO.getInstance();
	        blockDAO.insertBlock(blockDTO);

	        // 필드, 화면 초기화
	        textFieldTitle.setText("");
	        textAreaContent.setText("");
	        ((BlockExercise) getParent()).refreshTextArea();

	        JOptionPane.showMessageDialog(this, "블록 문제가 추가되었습니다.");
	        // 포커스
	        textFieldTitle.requestFocusInWindow();
	    } catch (EmptyTitleContentException e) {
	        JOptionPane.showMessageDialog(this, e.getMessage());
	    } catch (SQLIntegrityConstraintViolationException e) {	// 중복된 제목일 경우 예외처리
	        JOptionPane.showMessageDialog(this, "중복된 제목입니다.");
	    } catch (TitleOverException e) {
	        JOptionPane.showMessageDialog(this, "제목이 올바르지 않습니다.");
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, "SQL에서 오류가 발생했습니다.");
	        e.printStackTrace();
	    }
	}
}