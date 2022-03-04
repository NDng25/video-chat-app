package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.util.AdaptiveSizeWriter;


import DTO.Convert;
import DTO.MessageUDP;
import DTO.NameServer;
import DTO.Voice;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ClientVideo{
	DatagramSocket clientSocket;
	String username;
	String receiver;
	public static Boolean checkSend=true;
	public static Boolean checkReceiver=true;
	public ClientVideo(DatagramSocket clientSocket, String username, String receiver) throws UnknownHostException, LineUnavailableException, SocketException {
		this.clientSocket = clientSocket;
		this.username = username;
		this.receiver = receiver;
	}
	public void StartSendVideo(Boolean check) throws UnknownHostException, LineUnavailableException {
		checkSend=true;
	   	SendVideoUDP sendVideo=new SendVideoUDP(clientSocket, NameServer.getServerAddress(), this.username, this.receiver);
		 sendVideo.start();
	}
	public void StartReceiveVideo(Boolean check) throws SocketException, LineUnavailableException 
	{
		checkReceiver=true;
		ReceiveVideoUDP receiveVideo=new ReceiveVideoUDP(clientSocket);
        receiveVideo.start();
	}
	
}
class SendVideoUDP extends Thread
{
	private static final Dimension RESOLUTION = WebcamResolution.VGA.getSize();
	DatagramSocket clientSocket;
	InetAddress iPAddress;
	BufferedImage br;
	Webcam cam;
	String sender;
	String receiver;
	public TargetDataLine audio_in =null;
	AdaptiveSizeWriter writer = new AdaptiveSizeWriter(100000);
	public SendVideoUDP(DatagramSocket x,InetAddress y, String sender, String receiver) throws LineUnavailableException {
		clientSocket=x;
		iPAddress=y;
		this.sender = sender;
		this.receiver = receiver;
	    cam=Webcam.getDefault();
		cam.setViewSize(RESOLUTION);
		cam.open();
		DataLine.Info info=new DataLine.Info(TargetDataLine.class,Voice.getAudioFormat());
    	audio_in=(TargetDataLine) AudioSystem.getLine(info);
    	audio_in.open(Voice.getAudioFormat());
    	audio_in.start();
	}
	public BufferedImage Background()
	{
		BufferedImage img = null;
		try {
			String localDir = System.getProperty("user.dir");
		    img = ImageIO.read(new File(localDir+"\\src\\images\\avatar.png"));
		    Image tmp =img.getScaledInstance( 219, 129, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage( 219, 129, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();
		    return dimg;
		    
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void run()
	{
		ChatRoom.img_client.setIcon(new ImageIcon(Background()));
		while (ClientVideo.checkSend) {  
		    try {
		    	//br=cam.getImage();		
		    	byte[] arr=writer.write(cam.getImage());
		    	try (InputStream is = new ByteArrayInputStream(arr)) {
					br = ImageIO.read(is);
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				Image tmp =br.getScaledInstance(219, 129, Image.SCALE_SMOOTH);
			    BufferedImage dimg = new BufferedImage(219, 129, BufferedImage.TYPE_INT_ARGB);
			    Graphics2D g2d = dimg.createGraphics();
			    g2d.drawImage(tmp, 0, 0, null);
			    g2d.dispose();
				ChatRoom.img_client.setIcon(new ImageIcon(dimg));
		    	
		    	//Client.img_server.setIcon(new ImageIcon(br));
				//ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//ImageIO.write(dimg,"JPG", baos);
				//sendData =baos.toByteArray();
				//System.out.println(arr.length);
				byte byte_buff[] =new byte[10000];
				audio_in.read(byte_buff, 0, byte_buff.length);
				MessageUDP sMessage=new MessageUDP(this.sender, this.receiver,arr,byte_buff);		 
				byte[] yourBytes = Convert.serialize(sMessage);
				System.out.println("Lenght"+yourBytes.length);
				DatagramPacket data=new DatagramPacket(yourBytes,yourBytes.length,iPAddress,6789);
				clientSocket.send(data);
				System.out.println("sended obj to "+this.receiver);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ChatRoom.img_client.setIcon(new ImageIcon(Background()));
			}	
		}
		cam.close();
		audio_in.close();
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(Background(), "jpg", baos);
			byte[] bytes = baos.toByteArray();
			MessageUDP sMessage=new MessageUDP(this.sender, this.receiver,bytes,new byte[0]);		 
			byte[] yourBytes = Convert.serialize(sMessage);
			DatagramPacket data=new DatagramPacket(yourBytes,yourBytes.length,iPAddress,6789);
			clientSocket.send(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ChatRoom.img_client.setIcon(new ImageIcon(Background()));
	}
}
class ReceiveVideoUDP extends Thread
{
	DatagramSocket clientSocket;
	byte[] receiveData=new byte[44000];
	private SourceDataLine audio_out;
	public static Boolean check=true;
	BufferedImage br;
	public ReceiveVideoUDP(DatagramSocket x) throws SocketException, LineUnavailableException {
		clientSocket=x;
		DataLine.Info info_out=new DataLine.Info(SourceDataLine.class,Voice.getAudioFormat());
		audio_out=(SourceDataLine) AudioSystem.getLine(info_out);
		audio_out.open(Voice.getAudioFormat());
		audio_out.start();
	}
	public BufferedImage Background()
	{
		BufferedImage img = null;
		try {
			String localDir = System.getProperty("user.dir");
		    img = ImageIO.read(new File(localDir+"\\src\\images\\avatar.png"));
		    Image tmp =img.getScaledInstance( 785, 498, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage( 785, 498, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();
		    return dimg;
		    
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void run()
	{
		ChatRoom.img_server.setIcon(new ImageIcon(Background()));
		while (ClientVideo.checkReceiver) {
			
		try {
			DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length );	
			clientSocket.receive(receivePacket);
			Object obj=Convert.deserialize(receivePacket.getData());
			System.out.println("Lenght"+obj.getClass());
			if(obj instanceof MessageUDP) {
				System.out.println("received obj from: "+((MessageUDP)obj).getName());
			byte[] voice=((MessageUDP)obj).getVoice();
			byte[] video=((MessageUDP)obj).getVideo();
			audio_out.write(voice,0,voice.length);
			AudioFormat format=Voice.getAudioFormat();
			ByteArrayInputStream inputStream=new ByteArrayInputStream(voice);
			AudioInputStream stream=new AudioInputStream(inputStream,format,10000);
			try {
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(stream);
		        clip.start(); 
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			
			try (InputStream is = new ByteArrayInputStream(video)) {
				br = ImageIO.read(is);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			Image tmp =br.getScaledInstance( 785, 498, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage( 785, 498, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();
		    ChatRoom.img_server.setIcon(new ImageIcon(dimg));
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ChatRoom.img_server.setIcon(new ImageIcon(Background()));
		}
		
		}
		ChatRoom.img_server.setIcon(new ImageIcon(Background()));
	}
	
}
