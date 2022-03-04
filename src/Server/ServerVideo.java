package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.LineUnavailableException;

import DTO.Convert;
import DTO.MessageUDP;

public class ServerVideo {
	DatagramSocket soc_UDP;

	public ServerVideo(DatagramSocket soc_UDP) throws SocketException {
		this.soc_UDP = soc_UDP;
		ReceiveVideoServer serverVoice=new ReceiveVideoServer(soc_UDP);
		serverVoice.start();
	}

}
class ReceiveVideoServer extends Thread
{
	DatagramSocket serverSocket;
	byte[] receiveData=new byte[44000];
	InetAddress ipAddress;
	int port=-1;
	public static boolean check=true;
	public ReceiveVideoServer(DatagramSocket x) 
	{
		serverSocket=x;
	}
	@Override
	public void run()
	{
		//113.174.223.249
		
		while(check)
		{
		try {
			DatagramPacket receive = new DatagramPacket(receiveData,receiveData.length );
			serverSocket.receive(receive);
			ipAddress =receive.getAddress();
			port=receive.getPort();
			System.out.println(receive.getData().length);
			Object obj=Convert.deserialize(receive.getData());
			if(obj instanceof MessageUDP) {
				MessageUDP msg = ((MessageUDP)obj);
				ChatServer.listclient.get(msg.getName()).setPort_UDP(port);
				User receiver = ChatServer.listclient.get(msg.getReceiver());
				System.out.println("Ten: "+ msg.getName()+" port :"+ChatServer.listclient.get(msg.getName()).getPort_UDP());
				if(msg.getReceiver().length()>0)
				{
				System.out.println("from "+msg.getName()+" to "+receiver.getUsername()+" port: "+receiver.getPort_UDP());
				DatagramPacket sendPacket=new DatagramPacket(receive.getData(),receive.getData().length,receiver.getSocket().getInetAddress(),receiver.getPort_UDP());
				serverSocket.send(sendPacket);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		}
		
	}
}
