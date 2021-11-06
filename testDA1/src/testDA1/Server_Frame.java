package testDA1;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.naming.OperationNotSupportedException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.awt.event.ActionEvent;

public class Server_Frame extends JFrame {

	private JPanel contentPane;
	private JButton btnStart;
	public int port=8888;
	public static AudioFormat getAudioFormat()
	{
	float sapleRate=8000.0F;
	int sampleSizeInbits=16;
	int chanel=2;
	boolean signed=true;
	boolean bigEndian=false;
	return new AudioFormat(sapleRate, sampleSizeInbits, chanel, signed, bigEndian);
     }
	public SourceDataLine audio_out;
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server_Frame frame = new Server_Frame();
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
	public Server_Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try {
					init_audio();
				} catch (SocketException | LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnStart.setBounds(160, 128, 85, 21);
		contentPane.add(btnStart);
	}
	public void init_audio() throws LineUnavailableException, SocketException
	{
		AudioFormat format=getAudioFormat();
		DataLine.Info info_out=new DataLine.Info(SourceDataLine.class,format);
		if(!AudioSystem.isLineSupported(info_out))
		{
			System.out.println("not suport");
			System.exit(0);
			
		}
		audio_out=(SourceDataLine) AudioSystem.getLine(info_out);
		audio_out.open(format);
		audio_out.start();
		player_thread p=new  player_thread();
		p.din=new DatagramSocket(port);
		p.audio_out=audio_out;
		Server_voice.calling=true;
		p.start();
		btnStart.setEnabled(false);
		
		
		
		
	}

}
