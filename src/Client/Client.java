package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import DTO.Message;
import DTO.Request;

public class Client {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
//	private ReadClient read;
	private String username;
	public String getUsername() {
		return username;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}

//	public ReadClient getRead() {
//		return read;
//	}
//
//	public void setRead(ReadClient read) {
//		this.read = read;
//	}
//
//	public WriteClient getWrite() {
//		return write;
//	}
//
//	public void setWrite(WriteClient write) {
//		this.write = write;
//	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	private WriteClient write;
	private List<String> userList;
	private int port = 7050;
	private String address = "localhost";
	
	public Client(String username) throws InterruptedException {
		this.username = username;
		this.userList = new ArrayList<String>();
		try {
			this.socket = new Socket(InetAddress.getByName(address),port);
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(socket.getInputStream());
			System.out.println(address + " "+port);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	class WriteClient extends Thread{
		private Socket socket;
		private String username;
		public WriteClient(String username, Socket socket) throws IOException {
			this.socket = socket;
			this.username = username;
		}
		
		@Override
		public void run() {
			ObjectOutputStream outs = null;
			try {
				Scanner sc = new Scanner(System.in);
				while(true) {
					outs = new ObjectOutputStream(socket.getOutputStream());
					String msg = sc.nextLine();
					Message m = new Message(username,"",msg);
					outs.writeObject(m);
					sc.reset();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
				try {
					outs.close();
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
//	public static void main(String args[]) {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Nhap ten");
//		String name = sc.nextLine();
//		try {
//			Client client = new Client(name);
//			client.execute();
//		} catch (InterruptedException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	


}




