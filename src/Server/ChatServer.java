package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import DTO.*;

public class ChatServer extends Thread{

	private final int port = 9999;
	private ServerSocket server;
	private DatagramSocket udp_server;
//	private List<User> users = new ArrayList();
	public static HashMap<String, User> listclient = new HashMap<>();
	private ServerVideo serverVideo;
	
	public ChatServer() {
		try {
			this.server = new ServerSocket(port);
			this.udp_server = new DatagramSocket(6789);
			System.out.println("Chat server....");
			this.serverVideo = new ServerVideo(udp_server);
			new ServerS3().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeSocket(User user) {
		try {
			user.getSocket().close();
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
				//DatagramPacket packet = new DatagramPacket(new byte[1], 1);
				//this.udp_server.receive(packet);
				//int udp_port = packet.getPort();
				User u = new User(socket,username,0);
				System.out.println("Ten: "+ username+", address: "+ socket.getInetAddress().toString()+", port_udp: "+"");
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
			
				//ins = new ObjectInputStream(socket.getInputStream());
				while(!user.getSocket().isClosed()) {
					try {
					ins = new ObjectInputStream(user.getIn());
					Object obj = ins.readObject();
					if(obj instanceof Disconnect) {
						Disconnect d = ((Disconnect)obj);
						System.out.println(d.getName()+" disconnect!");
						listclient.remove(user.getUsername());
						closeSocket(user);
						listclient.forEach((key,value) -> {
							if(key.equals(d.getPeer())) {
								BroadcastDisConn(listclient.get(key),d.getName());
							}
						});
						break;
					}
					else if(obj instanceof Message) {
						String receiver = ((Message)obj).getReceiver();
						System.out.println("receiver: "+receiver);
						User u = listclient.get(receiver);
						if(u == null) {
							//thong bao peer da ngat ket noi
							BroadcastDisConn(user,receiver);
						}
						else {
							ObjectOutputStream out = new ObjectOutputStream(u.getOut());
							out.writeObject(obj);
						}
						
					}
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listclient.remove(user.getUsername());
					System.out.println(user.getUsername()+" disconnect");
					closeSocket(user);
				}
				}
			
		}

		private synchronized void BroadcastDisConn(User user, String msg) {
			// TODO Auto-generated method stub
			try {
				new ObjectOutputStream(user.getOut()).writeObject(new BroadcastMessage(msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
