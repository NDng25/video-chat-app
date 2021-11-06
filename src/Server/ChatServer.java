package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DTO.Message;

public class ChatServer extends Thread{

	private final int port = 9999;
	private ServerSocket server;
	private DatagramSocket udp_server;
//	private List<User> users = new ArrayList();
	private HashMap<String, User> listclient = new HashMap<>();
	
	
	public ChatServer() {
		try {
			this.server = new ServerSocket(port);
			this.udp_server = new DatagramSocket(6969);
			System.out.println("Chat server....");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			while(true) {
				Socket socket = server.accept();
				System.out.println("Chat server da ket noi voi "+socket);
				DataInputStream din = new DataInputStream(socket.getInputStream());
				String username = din.readUTF();
				DatagramPacket packet = new DatagramPacket(new byte[1], 1);
				this.udp_server.receive(packet);
				int udp_port = packet.getPort();
				User u = new User(socket,username,udp_port);
				System.out.println("Ten: "+ username+", address: "+ socket.getInetAddress().toString()+", port: "+udp_port);
				this.listclient.put(u.getUsername(), u);
//				users.add(u);
				new MessageHandler(u).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class MessageHandler extends Thread{
		private User user;
		
		public MessageHandler(User u) {
			this.user = u;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("MessageHandler is running");
			ObjectInputStream ins = null;
			try {
				//ins = new ObjectInputStream(socket.getInputStream());
				while(true) {
					ins = new ObjectInputStream(user.getIn());
					Object obj = ins.readObject();
					if(obj instanceof Message) {
						String receiver = ((Message)obj).getReceiver();
//						for(User item : users) {
//							if(item.getUsername().equals(receiver)) {
//								ObjectOutputStream out = new ObjectOutputStream(item.getOut());
//								out.writeObject(obj);
//							}
//						}
						User u = listclient.get(receiver);
						ObjectOutputStream out = new ObjectOutputStream(u.getOut());
						out.writeObject(obj);
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
