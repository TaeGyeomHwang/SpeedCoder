package view;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.UserDAO;
import model.UserDTO;

public class Login extends JFrame implements KeyListener {
	private static String enteredText;
	private JLabel lblTitle;
	private JLabel lblId;
	private JLabel lblPw;
	private Font fontTitle;
	private Font fontSignup;
	private JTextField txtFieldId;
	private JPasswordField pwFieldPw;
	private JButton btnLogin;
	private JButton btnSignup;

	private static String loginedId;

	public Login() {
		this.setTitle("SPEED C( )DER - Login");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		ImageIcon backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("logo.png"));
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, 500, 500);
		this.add(backgroundLabel);
		
		backgroundLabel.setLayout(null);
		backgroundLabel.add(getTitleLabel());
		backgroundLabel.add(getIdTextField());
		backgroundLabel.add(getPwTextField());
		backgroundLabel.add(getLoginBtn());
		backgroundLabel.add(getSignupBtn());
		backgroundLabel.add(getIdLabel());
		backgroundLabel.add(getPwLabel());

		this.locationCenter();
		txtFieldId.addKeyListener(this);
		pwFieldPw.addKeyListener(this);
	}

	/* 라벨 */
	// 제목 라벨 설정
	private JLabel getTitleLabel() {
		if (lblTitle == null) {
			lblTitle = new JLabel();
			lblTitle.setBounds(100, 80, 300, 50);
			lblTitle.setFont(getTitleFont());
		}
		return lblTitle;
	}

	// ID 라벨 설정
	private JLabel getIdLabel() {
		if (lblId == null) {
			lblId = new JLabel();
			lblId.setText("ID  : ");
			lblId.setBounds(65, 200, 100, 40);
			lblId.setFont(getSignupFont());
		}
		return lblId;
	}

	// PW 라벨 설정
	private JLabel getPwLabel() {
		if (lblPw == null) {
			lblPw = new JLabel();
			lblPw.setText("PW : ");
			lblPw.setBounds(60, 250, 100, 40);
			lblPw.setFont(getSignupFont());
		}
		return lblPw;
	}

	/* 폰트 */
	// 제목 폰트 설정
	private Font getTitleFont() {
		if (fontTitle == null) {
			fontTitle = new Font("Malgun Gothic", Font.PLAIN, 40);
		}
		return fontTitle;
	}

	// 제목 폰트 설정
	private Font getSignupFont() {
		if (fontSignup == null) {
			fontSignup = new Font("Malgun Gothic", Font.PLAIN, 25);
		}
		return fontSignup;
	}

	// 아이디 텍스트 필드
	private JTextField getIdTextField() {
		if (txtFieldId == null) {
			txtFieldId = new JTextField();
		}
		txtFieldId.setBounds(125, 200, 250, 40);
		return txtFieldId;
	}

	// 비밀먼호 패스워드 필드
	private JPasswordField getPwTextField() {
		if (pwFieldPw == null) {
			pwFieldPw = new JPasswordField();
		}
		pwFieldPw.setBounds(125, 250, 250, 40);
		return pwFieldPw;
	}

	/* 버튼 */
	// 로그인 버튼
	private JButton getLoginBtn() {
		if (btnLogin == null) {
			btnLogin = new JButton();
			btnLogin.setText("로그인");
			btnLogin.setBounds(130, 300, 110, 40);
			btnLogin.addActionListener(e -> {
				LoginFn();
			});
		}
		return btnLogin;
	}

	// 로그인 버튼 기능
	private void LoginFn() {

		boolean accountExist = false;
		String id = txtFieldId.getText();
		char[] pw = pwFieldPw.getPassword();
		String strPw = new String(pw);

		UserDAO userDAO = UserDAO.getInstance();
		List<UserDTO> users = userDAO.getSignups(); // 전체 회원 정보 가져오기 메소드

		// 전체 회원 정보에서 입력받은 아이디 및 비밀번호가 일치하는 경우 accountExist 계정 존재 여부 true
		for (UserDTO userDTO : users) {
			if (id.equals(userDTO.getId()) && strPw.equals(userDTO.getPw())) {
				accountExist = true;
				break;
			}
		}

		// if -> 계정이 있는 경우, else -> 계정이 없는 경우
		if (accountExist) {
			Login.loginedId = id;
			JOptionPane.showMessageDialog(Login.this, loginedId + "님 안녕하세요!");
			dispose();
			Main main = new Main();
			main.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(Login.this, "아이디 또는 비밀번호를 잘못 입력하셨습니다.");
			pwFieldPw.setText("");
		}
	}

	// 회원가입 버튼
	private JButton getSignupBtn() {
		if (btnSignup == null) {
			btnSignup = new JButton();
			btnSignup.setText("회원가입");
			btnSignup.setBounds(260, 300, 110, 40);
			btnSignup.addActionListener(e -> {
				dispose();
				Signup signup = new Signup();
				signup.setVisible(true);
			});
		}
		return btnSignup;
	}

	// 타 클래스에서 로그인 된 id 접근을 위한 getter
	public static String getLoginedId() {
		return loginedId;
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
			Login login = new Login();
			login.setVisible(true);
		});
	}
	
	// 엔터 입력 시 로그인 실행
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			LoginFn();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
