package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import DTO.Message;
import DTO.Request;

public class Server extends Thread{
	private final int timeOut = 5000;
	
	private ServerSocket server;
	private int port;
	public List<User> clientList;
	Thread updateActive = null;
	
	public Server(int port) {
		this.clientList = new ArrayList<>();
		this.port = port;
//		new ServerS2();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			this.server = new ServerSocket(port);
			System.out.println("Server is listerning...");
			while(true) {
				Socket socket = server.accept();
				System.out.println("Da ket noi voi "+socket);
				DataInputStream din = new DataInputStream(socket.getInputStream());
				String username = din.readUTF();
				User u = new User(socket,username,port);
				System.out.println("Ten: "+ username);
				clientList.add(u);
				sendActiveUser();
				new Handler(u).start();
				//Gui danh sach hoat dong moi khoang thoi gian = timeOut
				updateActive = new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							try {
								Thread.sleep(timeOut);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							sendActiveUser();
						}
					}
				});
				updateActive.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removeUser(User user) {
		for(User i : clientList) {
			if(i.getUsername() == user.getUsername()) {
				clientList.remove(i);
				sendActiveUser();
			}
		}
	}
	
	public void UpdateActiveUser() {
		for(User i : clientList) {
			if(i.getSocket().isClosed()) 
				removeUser(i);
		}
	}
	
	
	public void sendActiveUser() {
		List<String> users = new ArrayList<String>();
		for(User item : clientList)
		{
			users.add(item.getUsername());
		}
		for(User item : clientList) {
			ObjectOutputStream out;
			try {
				List<String> tmp = new ArrayList<String>(users);
				tmp.remove(item.getUsername());
				out = new ObjectOutputStream(item.getSocket().getOutputStream());
				Request rq = new Request(tmp);
				out.writeObject(rq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void closeSocket(User u) {
		try {
			u.getSocket().close();
			updateActive.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class Handler extends Thread{
		private User user;
		
		public Handler(User _user) throws IOException {
			user = _user;
		}
		
		@Override
		public void run() {
			ObjectInputStream ins = null;
			System.out.println("Handler is running!");
			try {
				//ins = new ObjectInputStream(socket.getInputStream());
				while(!user.getSocket().isClosed()) {
					// check for client disconnection
					ins = new ObjectInputStream(user.getIn());
					Object obj = ins.readObject();
					if(obj == null) {
						System.out.println(user.getUsername()+" disconnect!");
						clientList.remove(user);
						closeSocket(user);
						break;
					}
					else if(obj instanceof Message) {
						System.out.println("server nhan: "+((Message)obj).getMessage());
						for(User item : clientList) {
							if(item.getUsername().equals(((Message)obj).getReceiver())) {
								ObjectOutputStream out = new ObjectOutputStream(item.getOut());
								out.writeObject(obj);
								System.out.println("server da gui di");
							}
						}
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

class User {
	private Socket socket;
	private String username;
	private int Port_UDP;
	private InputStream in;
	private OutputStream out;
	public User(Socket s, String username, int port_udp) throws IOException {
		this.socket = s;
		this.username = username;
		this.Port_UDP = port_udp;
		this.in = socket.getInputStream();
		this.out = socket.getOutputStream();
	}
	public InputStream getIn() {
		return in;
	}
	public OutputStream getOut() {
		return out;
	}
	public Socket getSocket() {
		return socket;
	}
	public String getUsername() {
		return username;
	}
	public int getPort_UDP() {
		return Port_UDP;
	}
	public void setPort_UDP(int port_UDP) {
		Port_UDP = port_UDP;
	}
}

//new Thread(new Runnable() {
//	@Override
//	public void run() {
//		while(!user.getSocket().isClosed()) {
//			try {
//				Thread.sleep(timeOut);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				user.getOut().write(1);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				System.out.println(user.getUsername()+" was disconnected!");
//				clientList.remove(user);
//				try {
//					user.getSocket().close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		}
//	}
//}).start();