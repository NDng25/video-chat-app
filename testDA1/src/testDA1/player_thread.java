package testDA1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.sampled.SourceDataLine;

public class player_thread extends Thread{
	public DatagramSocket din;
	public SourceDataLine audio_out;
	byte[] buffer =new byte[512];
	@Override
	public void run()
	{
		DatagramPacket incoming =new DatagramPacket(buffer, buffer.length);
		int i=0;
		while (Server_voice.calling) {
			try {
				din.receive(incoming);
				buffer= incoming.getData();
				audio_out.write(buffer,0,buffer.length);
				System.err.println("#"+i++);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			audio_out.close();
			audio_out.drain();
			System.out.println("Stop"); 
			
		}
	}
}
