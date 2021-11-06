package testDA1;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class client_frame extends JFrame {

	private JPanel contentPane;
	private JButton btn_Start;
	public int port_server=8888;
	public String add_server="localhost";
public static AudioFormat getAudioFormat()
{
	float sapleRate=8000.0F;
	int sampleSizeInbits=16;
	int chanel=2;
	boolean signed=true;
	boolean bigEndian=false;
	return new AudioFormat(sapleRate, sampleSizeInbits, chanel, signed, bigEndian);
}
TargetDataLine audio_in;
private JButton btn_Stop;
public void init_audio() throws LineUnavailableException, SocketException, UnknownHostException
{
	AudioFormat format=getAudioFormat();
	DataLine.Info info=new DataLine.Info(TargetDataLine.class,format);
	if(!AudioSystem.isLineSupported(info))
	{
		System.out.println("Not support");
		System.exit(0);
	}
	audio_in=(TargetDataLine) AudioSystem.getLine(info);
	audio_in.open(format);
	audio_in.start();
	record_thread r=new record_thread();
	InetAddress inet=InetAddress.getByName(add_server);
	r.audio_in=audio_in;
	r.dout=new DatagramSocket();
	r.server_ip=inet;
	r.server_port=port_server;
    client_voice.calling=true;
    r.start();
    btn_Start.setEnabled(false);
    btn_Stop.setEnabled(true);
}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					client_frame frame = new client_frame();
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
	public client_frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 604, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btn_Start = new JButton("Start");
		btn_Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					init_audio();
				} catch (SocketException | UnknownHostException | LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btn_Start.setBounds(130, 162, 85, 21);
		contentPane.add(btn_Start);
		
		btn_Stop = new JButton("Stop");
		btn_Stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				client_voice.calling=false;
				btn_Start.setEnabled(true);
			    btn_Stop.setEnabled(false);
			}
		});
		btn_Stop.setBounds(300, 162, 85, 21);
		contentPane.add(btn_Stop);
	}
}
