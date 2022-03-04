
package Server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ServerS2 extends Thread{
	Vector<Xuly2> clients = new Vector<Xuly2>();
//	Vector<receive> recei = new Vector<receive>();
	Vector<User2> clientsgui = new Vector<User2>();

	//Socket soc1;
	ServerSocket server ;
	public static void main(String args[]) {
		new ServerS2();
	}

	public ServerS2() {
	
		
	}
	@Override
	public void run()
	{
		try {
			System.out.print("Started111!!!");
			server = new ServerSocket(7749);
			System.out.println(" Start accept!!");			
			// nhận video gửi video tên người nhận gửi
			// soc, name , true 
			// soc. name gửi, false 
		while (true) {
			Socket soc1 = server.accept();
			DataInputStream din = new DataInputStream(soc1.getInputStream());
			
			Boolean status = din.readBoolean();
			String username = din.readUTF();
			User2 u = new User2(soc1, username, status);
			if(status == true) {
				clientsgui.add(u);
			}else {
							
				for (User2 u2 : clientsgui) {
					if(username.equals(u.getUsername())) {
						Xuly2 x = new Xuly2(u, u2, this);
						x.start();
						break;
					}
				}				
			}
		
		}
	} catch (Exception e) {

	}
	}
}

class User2 {
	private Socket socket;
	private String username;
	private InputStream in;
	private OutputStream out;
	private Boolean status;
	public User2(Socket s, String username, Boolean Status) throws IOException {
		this.socket = s;
		this.username = username;
		this.status = Status;
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
	public Boolean getStatus() {
		return status;
	}
}
class Xuly2 extends Thread {

	Socket sock, sockgui;
	String name;
	ServerS2 ser;
	User2 u,u2;
	public Xuly2(User2 u, User2 u2, ServerS2 ser) {
		this.sock = u.getSocket();
		this.ser = ser;
		this.sockgui = u2.getSocket();
		this.u=u;
		this.u2=u2;
	}

	public void run() {
		try {
			

			InputStream din = sockgui.getInputStream();
			System.out.println("111!!!");
			int status=0;
			while (status<600) {
							// Nhan Screen
							System.out.println("222!!!");
							BufferedImage img = ImageIO.read(din);
							
							if(img!=null) {
								
								status=0;
								System.out.print("333!!!");
			
									try {
		//								c.sock = cs.server.accept();
										OutputStream dout = sock.getOutputStream();
										ByteArrayOutputStream ous = new ByteArrayOutputStream();
										ImageIO.write(img, "png", ous);
										dout.write(ous.toByteArray());
										System.out.println("Startednnn!!!");
										dout.flush();
		
									} catch (Exception e1) {
										e1.printStackTrace();
									}						
										
							try {
			                    Thread.sleep(10);
			                } catch (Exception e) {
			                	e.printStackTrace();
			                }
							}else {
								status++;
							}
								
			}
			ser.clientsgui.remove(u2);
			sock.close(); sockgui.close();
		} catch (Exception e) {
		}
	}
}
