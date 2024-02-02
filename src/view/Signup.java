package view;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Signup extends JFrame{
	
	private JLabel lblTitle;
	private JLabel lblId;
	private JLabel lblPw;
	private JLabel lblPwVerify;
	private Font ftTitle;
	private Font ftSignup;
	private JTextField txtFieldId;
	private JPasswordField pwFieldPw; 
	private JPasswordField pwFieldPwVerify; 
	private JButton btnSignup;
	private JButton btnCancel;
	
	
	public Signup() {
		this.setTitle("SPEED C( )DER - Signup");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		this.getContentPane().setLayout(null);
		this.getContentPane().add(getTitleLabel());

		this.getContentPane().add(getIdLabel());
		this.getContentPane().add(getIdTextField());
		
		this.getContentPane().add(getPwLabel());
		this.getContentPane().add(getPwField());
		
		this.getContentPane().add(getPwVerifyLabel());
		this.getContentPane().add(getPwVerifyField());
		
		this.getContentPane().add(getSignupBtn());
		this.getContentPane().add(getCancelBtn());
		
		this.locationCenter();
	}
	
	/* 라벨 */
	// 제목 문구 설정
	private JLabel getTitleLabel() {
		if(lblTitle == null	) {
			lblTitle = new JLabel();
			lblTitle.setText("SPEED C( )DER");
			lblTitle.setBounds(100, 80, 300, 50);
			lblTitle.setFont(getTitleFont());
		}
		return lblTitle;
	}
	
	// ID 라벨 설정
		private JLabel getIdLabel() {
			if(lblId == null) {
				lblId = new JLabel();
				lblId.setText("ID    : ");
				lblId.setBounds(85, 170, 100, 40);
				lblId.setFont(getSignupFont());
			}
			return lblId;
		}
		
		// PW 라벨 설정
		private JLabel getPwLabel() {
			if(lblPw == null) {
				lblPw = new JLabel();
				lblPw.setText("PW    : ");
				lblPw.setBounds(68, 220, 100, 40);
				lblPw.setFont(getSignupFont());
			}
			return lblPw;
		}
		
		// PW 검사 라벨 설정
		private JLabel getPwVerifyLabel() {
			if(lblPwVerify == null) {
				lblPwVerify = new JLabel();
				lblPwVerify.setText("PW 확인 : ");
				lblPwVerify.setBounds(50, 270, 100, 40);
				lblPwVerify.setFont(getSignupFont());
			}
			return lblPwVerify;
		}
	
	/* 폰트 */
	// 제목 폰트 설정
		private Font getTitleFont() {
			if(ftTitle == null) {
				ftTitle = new Font("Malgun Gothic", Font.PLAIN, 40);			
			}
			return ftTitle;
		}
	
	// 제목 폰트 설정
	private Font getSignupFont() {
		if(ftSignup == null) {
			ftSignup = new Font("Malgun Gothic", Font.PLAIN, 20);			
		}
		return ftSignup;
	}
	
	// 아이디 텍스트 필드
		private JTextField getIdTextField() {
			if(txtFieldId == null) {
				txtFieldId = new JTextField();
			}
			txtFieldId.setBounds(150, 170, 250, 40);
			return txtFieldId;
		}
		
		// 비밀먼호 패스워드 필드
		private JPasswordField getPwField() {
			if(pwFieldPw == null) {
				pwFieldPw = new JPasswordField();
			}
			pwFieldPw.setBounds(150, 220, 250, 40);
			return pwFieldPw;
		}
		
		// 비밀먼호 검사 패스워드 필드
		private JPasswordField getPwVerifyField() {
			if(pwFieldPwVerify == null) {
				pwFieldPwVerify = new JPasswordField();
			}
			pwFieldPwVerify.setBounds(150, 270, 250, 40);
			return pwFieldPwVerify;
		}
	
	/* 버튼 */
	// 로그인 버튼
	private JButton getSignupBtn() {
		if (btnSignup == null) {
			btnSignup = new JButton();
			btnSignup.setText("회원가입");
			btnSignup.setBounds(155, 320, 110, 40);
			btnSignup.addActionListener(e -> {
				JOptionPane.showMessageDialog(Signup.this, "회원가입이 완료되었습니다.");
				dispose();
			});
		}
		return btnSignup;
	}

	// 블록 연습 화면 출력
	private JButton getCancelBtn() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText("취소");
			btnCancel.setBounds(285, 320, 110, 40);
			btnCancel.addActionListener(e -> {
				dispose();
				// 회원가입 버튼을 클릭하는 경우 회원가입 화면을 보여줌(다이얼로그 형식)
				// 회원가입 화면은 아이디를 입력받는 텍스트 필드, 패스워드 필드, 패스워드 확인 필드, 회원가입 버튼, 취소 버튼으로 구성
				// 회원 가입 성공의 경우 화면에 "회원가입이 완료되었습니다." 를 팝업을 띄우고 닫으면 로그인 화면을 출력한다.
				// 취소 버튼을 누를 경우 회원가입 화면을 닫고 로그인 화면을 출력한다.
			});
		}
		return btnCancel;
	}
	
	//창 중앙 정렬
	private void locationCenter() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int leftTopX = centerPoint.x - this.getWidth()/2;
		int leftTopY = centerPoint.y - this.getHeight()/2;
		this.setLocation(leftTopX, leftTopY);
	}

}
