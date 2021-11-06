package testDA1;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Panel;

import javax.swing.JButton;
import java.awt.CardLayout;

public class Server extends JFrame {

	private JPanel contentPane;
	public static  JLabel img_Server;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		ServerSocket server=new ServerSocket(7788);
		System.out.print("wait.....");
		Socket s=server.accept();
		System.out.print("Connect !!");
		ObjectInputStream in=new ObjectInputStream(s.getInputStream());
		ImageIcon icon;
		while ( true) {
			icon=(ImageIcon) in.readObject();
			img_Server.setIcon(icon);
			
		     
			
		}
	} 

	/**
	 * Create the frame.
	 */
	public Server() {
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 542, 373);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		img_Server = new JLabel("");
		img_Server.setHorizontalAlignment(SwingConstants.CENTER);
		img_Server.setBounds(10, 10, 495, 326);
		contentPane.add(img_Server);
	}
}
