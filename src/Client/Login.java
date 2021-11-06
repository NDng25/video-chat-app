package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pbl4.dbConnect;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtPassword;
	private JButton btnDangnhap;
	private JButton btnExit;
	private JButton btnDangky;
	private JTextField txtUsername;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 608, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lbText1 = new JLabel("\u0110\u0103ng nh\u1EADp");
		lbText1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbText1.setBounds(232, 30, 126, 40);
		panel.add(lbText1);
		
		JLabel lblNewLabel = new JLabel("T\u00EAn \u0111\u0103ng nh\u1EADp");
		lblNewLabel.setBounds(68, 146, 71, 24);
		panel.add(lblNewLabel);
		
		JLabel lblMtKhu = new JLabel("M\u1EADt kh\u1EA9u");
		lblMtKhu.setBounds(68, 232, 71, 24);
		panel.add(lblMtKhu);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(202, 143, 247, 33);
		panel.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(202, 229, 247, 33);
		panel.add(txtPassword);
		
		btnDangnhap = new JButton("\u0110\u0103ng nh\u1EADp");
		btnDangnhap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText().trim();
				String password = txtPassword.getText().trim();
				if(username.isEmpty() && password.isEmpty())
				{
					JOptionPane.showMessageDialog(new JFrame(),"Phải điền đủ username và password");
				}
				else 
				{
					dbConnect db = new dbConnect();
					if(db.Login(username,password))
					{
						int idUser = 123;
						dispose();
						MainFrame f = new MainFrame(idUser);
						f.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(new JFrame(),"username hoặc password sai");
					}
				}
			}
		});
		btnDangnhap.setBounds(153, 315, 90, 33);
		panel.add(btnDangnhap);
		
		btnExit = new JButton("Thoát");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		btnExit.setBounds(330, 315, 95, 33);
		panel.add(btnExit);
		
		btnDangky = new JButton("\u0110\u0103ng k\u00FD");
		btnDangky.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText().trim();
				String password = txtPassword.getText().trim();
				if(username.isEmpty() && password.isEmpty())
				{
					JOptionPane.showMessageDialog(new JFrame(),"Phải điền đủ username và password");
				}
				else 
				{
					dbConnect db = new dbConnect();
					if(db.Dangky(username,password))
					{
						dispose();
						MainFrame f = new MainFrame(123);
						f.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(new JFrame(),"username đã tồn tại");
					}
				}
			}
		});
		btnDangky.setBounds(254, 365, 71, 27);
		panel.add(btnDangky);
	}
}
