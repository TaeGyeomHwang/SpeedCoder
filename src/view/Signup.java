package view;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.UserDAO;
import model.UserDTO;

public class Signup extends JFrame implements KeyListener{

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

        // 배경 이미지 추가
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("../logo2.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 500, 500);
        this.add(backgroundLabel);

        // 컴포넌트 추가
        backgroundLabel.setLayout(null);
        backgroundLabel.add(getTitleLabel());
        backgroundLabel.add(getIdLabel());
        backgroundLabel.add(getIdTextField());
        backgroundLabel.add(getPwLabel());
        backgroundLabel.add(getPwField());
        backgroundLabel.add(getPwVerifyLabel());
        backgroundLabel.add(getPwVerifyField());
        backgroundLabel.add(getSignupBtn());
        backgroundLabel.add(getCancelBtn());

        this.locationCenter();
        
        // 키 입력 리스너 추가
        txtFieldId.addKeyListener(this);
		pwFieldPw.addKeyListener(this);
		pwFieldPwVerify.addKeyListener(this);
		
		this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    Login login = new Login();
                    login.setVisible(true);
                }
            }
        });
    }

	/* 라벨 */
	// 제목 문구 설정
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
			lblId.setText("ID      :  ");
			lblId.setBounds(70, 170, 100, 40);
			lblId.setFont(getSignupFont());
		}
		return lblId;
	}

	// PW 라벨 설정
	private JLabel getPwLabel() {
		if (lblPw == null) {
			lblPw = new JLabel();
			lblPw.setText("PW     :   ");
			lblPw.setBounds(68, 220, 100, 40);
			lblPw.setFont(getSignupFont());
		}
		return lblPw;
	}

	// PW 검사 라벨 설정
	private JLabel getPwVerifyLabel() {
		if (lblPwVerify == null) {
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
		if (ftTitle == null) {
			ftTitle = new Font("Malgun Gothic", Font.PLAIN, 40);
		}
		return ftTitle;
	}

	// 제목 폰트 설정
	private Font getSignupFont() {
		if (ftSignup == null) {
			ftSignup = new Font("Malgun Gothic", Font.PLAIN, 20);
		}
		return ftSignup;
	}

	// 아이디 텍스트 필드
	private JTextField getIdTextField() {
		if (txtFieldId == null) {
			txtFieldId = new JTextField();
		}
		txtFieldId.setBounds(150, 170, 250, 40);
		return txtFieldId;
	}

	// 비밀먼호 패스워드 필드
	private JPasswordField getPwField() {
		if (pwFieldPw == null) {
			pwFieldPw = new JPasswordField();
		}
		pwFieldPw.setBounds(150, 220, 250, 40);
		return pwFieldPw;
	}

	// 비밀먼호 검사 패스워드 필드
	private JPasswordField getPwVerifyField() {
		if (pwFieldPwVerify == null) {
			pwFieldPwVerify = new JPasswordField();
		}
		pwFieldPwVerify.setBounds(150, 270, 250, 40);
		return pwFieldPwVerify;
	}

	/* 버튼 */
	// 회원가입 버튼
	private JButton getSignupBtn() {
		if (btnSignup == null) {
			btnSignup = new JButton();
			btnSignup.setText("회원가입");
			btnSignup.setBounds(155, 320, 110, 40);
			btnSignup.addActionListener(e -> {
				SignupFn();
			});
		}
		return btnSignup;
	}

	// 나가기 버튼
	private JButton getCancelBtn() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText("취소");
			btnCancel.setBounds(285, 320, 110, 40);
			btnCancel.addActionListener(e -> {
				dispose();
				Login login = new Login();
				login.setVisible(true);
			});
		}
		return btnCancel;
	}

	// 회원가입 버튼 기능
	private void SignupFn() {

		boolean idCheck = false; // 아이디가 존재하면 true 값 가짐.
		String id = txtFieldId.getText();
		char[] pw = pwFieldPw.getPassword();
		char[] pwVerify = pwFieldPwVerify.getPassword();

		UserDAO userDAO = UserDAO.getInstance();
		List<UserDTO> users = userDAO.getSignups(); // 전체 회원 정보 가져오기 메소드

		// 입력받은 아이디가 중복이거나 null일 경우 idCehck 변수 true 설정
		for (UserDTO userDTO : users) {
			if (id.equals(userDTO.getId()) || id.equals("")) {
				idCheck = true;
			}
		}
		if (idCheck == true) {
			JOptionPane.showMessageDialog(Signup.this, "해당 아이디는 사용할 수 없습니다.");
		} else { // 아이디 검증 통과한 경우
			// 비밀번호가 입력되었으면서 검증란과 동일한 경우 if 문, 아닌 경우 else
			if (pw.length != 0 && Arrays.equals(pw, pwVerify)) {
				UserDTO userDTO = new UserDTO();
				
				userDTO.setId(id);
				String strPw = new String(pw); 
				userDTO.setPw(strPw);
				
				userDAO.insertSignup(userDTO);
				JOptionPane.showMessageDialog(Signup.this, "회원가입이 완료되었습니다.");
				super.dispose();
				Login login = new Login();
				login.setVisible(true);
			} else if (pw.length == 0) {
				JOptionPane.showMessageDialog(Signup.this, "비밀번호를 공백으로 설정할 수 없습니다.");
			} else {
				JOptionPane.showMessageDialog(Signup.this, "비밀번호와 비밀번호 확인란에 입력된 문자가 동일해야합니다.");
			}
		}
		pwFieldPw.setText("");
		pwFieldPwVerify.setText("");
	}

	// 창 중앙 정렬
	private void locationCenter() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int leftTopX = centerPoint.x - this.getWidth() / 2;
		int leftTopY = centerPoint.y - this.getHeight() / 2;
		this.setLocation(leftTopX, leftTopY);
	}
	
	// 엔터 입력 시 로그인 실행
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			SignupFn();
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			dispose();
			Login login = new Login();
			login.setVisible(true);
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
