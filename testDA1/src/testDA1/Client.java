package testDA1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Client extends JFrame {

	private JPanel contentPane;
	public static JLabel img_client;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Socket s=new Socket("localhost",7788);
		ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
		ImageIcon ic;
		BufferedImage br;
		Webcam cam=Webcam.getDefault();
		//cam.setViewSize(new Dimension(640,480));
	    cam.open();
		while (true) 
		{
			br=cam.getImage();		
			ic=new ImageIcon(br);
			out.writeObject(ic);
			out.flush();    
	        img_client.setIcon(ic);
		}
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 641, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		img_client = new JLabel("");
		img_client.setHorizontalAlignment(SwingConstants.CENTER);
		img_client.setBounds(28, 27, 572, 372);
		contentPane.add(img_client);
	}

}
