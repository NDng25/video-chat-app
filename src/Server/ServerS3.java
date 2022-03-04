
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

public class ServerS3 extends Thread{
	Vector<Xuly3> clients = new Vector<Xuly3>();
	ServerSocket server ;
	public static void main(String args[]) {
//		new ServerS3().start();
		
	}

	public ServerS3() {
	
		
	}
	@Override
	public void run()
	{
		try {
			System.out.print("Started111!!!");
			server = new ServerSocket(7749);
			System.out.println(" Start accept!!");			
			// nháº­n video gá»­i video tÃªn ngÆ°á»�i nháº­n gá»­i
			// soc, name , true 
			// soc. name gá»­i, false 
		while (true) {
			Socket soc1 = server.accept();
			DataInputStream din = new DataInputStream(soc1.getInputStream());
			
			Boolean status = din.readBoolean();
			String username = din.readUTF();
			Xuly3 u = new Xuly3(soc1, username, status, this);
			clients.add(u);
//			if(status == true) {		
//				u.start();
//			}else {
//
//				}
			u.start();
		
		}
	} catch (Exception e) {

	}
	}
}

class Xuly3 extends Thread {

	Socket soc;
	String Uname;
	ServerS3 ser;
	Boolean Status;

	public Xuly3(Socket soc, String username, Boolean Status, ServerS3 ser) {
		this.soc = soc;
		this.ser = ser;
		this.Uname=username;
		this.Status = Status;

	}

	public void run() {
		try {
			if(this.Status) {		
				DataInputStream din = new DataInputStream(soc.getInputStream());
				System.out.println("111!!!");
				int status=0;
				while (status<8000) {
					// Nhan Screen
					System.out.println("222!!!");
					//BufferedImage img = ImageIO.read(din);
					
					int length = din.readInt(); // read length of incoming message
					if (length > 0) {		
						status=0;
						System.out.print("333!!!");
						   byte[] message = new byte[length];
						   din.readFully(message, 0, message.length); // read the message
						   //Thread.sleep(100);
							try {
								if(ser.clients.size()>0) {
								for (Xuly3 u2 : ser.clients) {
									if(this.Uname.equals(u2.Uname)  && u2.Status==false ) {//&& u2.soc.getInputStream().read()!=-1
										new Thread(new Runnable() {
				            				@Override
									        public void run() {
										
										try {
											OutputStream dout = u2.soc.getOutputStream();
											dout.write(message);
											System.out.println("Startednnn!!!");
											dout.flush();
											//Thread.sleep(50);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
		            			}).start();
									}
								}	
								}
								

							} catch (Exception e1) {
								e1.printStackTrace();
							}						
								
					try {
	                  //Thread.sleep(10);
	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
					}else {
						status++;
						System.out.println("Error"+status);
					}
									
				}
				for (Xuly3 u2 : ser.clients) {
					if(this.Uname.equals(u2.Uname)  && u2.Status==false) {
						ser.clients.remove(u2);
						u2.soc.close();
					}
				}	
				ser.clients.remove(this);
				soc.close();
			}else {

//			
//				}
			}

		} catch (Exception e) {
		}
	}
}
