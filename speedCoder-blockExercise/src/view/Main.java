package view;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
	private JButton btnWordExcercise, btnBlockExcercise, btnMyInfo;

	// 메인 화면 출력
	public Main() {
		this.setTitle("SPEED C( )DER");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.getContentPane().add(getWordExerciseBtn());
		this.getContentPane().add(getBlockExerciseBtn());
		this.getContentPane().add(getMyInfoBtn());

		this.locationCenter();
	}

	// 단어 연습 화면 출력
	private JButton getWordExerciseBtn() {
		if (btnWordExcercise == null) {
			btnWordExcercise = new JButton();
			btnWordExcercise.setText("단어 연습");
			btnWordExcercise.setBounds(125, 90, 250, 55);
			btnWordExcercise.addActionListener(e -> {
				dispose();
				new WordExercise();
			});
		}
		return btnWordExcercise;
	}

	// 블록 연습 화면 출력
	private JButton getBlockExerciseBtn() {
		if (btnBlockExcercise == null) {
			btnBlockExcercise = new JButton();
			btnBlockExcercise.setText("블록 연습");
			btnBlockExcercise.setBounds(125, 190, 250, 55);
			btnBlockExcercise.addActionListener(e -> {
				dispose();
				BlockExercise block = new BlockExercise();
				block.setVisible(true);
			});
		}
		return btnBlockExcercise;
	}

	// 내 정보 화면 출력
	private JButton getMyInfoBtn() {
		if (btnMyInfo == null) {
			btnMyInfo = new JButton();
			btnMyInfo.setText("내 정보");
			btnMyInfo.setBounds(125, 290, 250, 55);
			btnMyInfo.addActionListener(e -> {
				dispose();
				new MyInfo();
			});
		}
		return btnMyInfo;
	}

	// 창 중앙 정렬
	private void locationCenter() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int leftTopX = centerPoint.x - this.getWidth() / 2;
		int leftTopY = centerPoint.y - this.getHeight() / 2;
		this.setLocation(leftTopX, leftTopY);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Main main = new Main();
			main.setVisible(true);
		});
	}

}
