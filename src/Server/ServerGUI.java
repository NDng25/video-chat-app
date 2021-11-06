package Server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ServerGUI extends JFrame {
	private final int serverPort = 7050;

	private JPanel contentPane;
	private Server server;
	private ChatServer chatServer;
	private JButton btnStart;
	private JButton btnStop;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
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
	public ServerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 516);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnStart = new JButton("Start Server");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnStart) {
					server = new Server(serverPort);
					server.start();
					chatServer = new ChatServer();
					chatServer.start();
				}
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStart.setBounds(214, 47, 150, 43);
		contentPane.add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStop.setBounds(214, 113, 150, 43);
		contentPane.add(btnStop);
		
		JList list = new JList();
		list.setBounds(10, 190, 616, 279);
		contentPane.add(list);
	}
}
