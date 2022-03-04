package Client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import DTO.*;


import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class ChatRoom extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPanel chatbox_panel;
	private static JTextPane textPane;
	private ChatClient chat;
	private Thread chatThread = null;
	private String username;
	private String peer;
	public static JLabel img_server;
	public static JLabel img_client;
	private boolean Send=false;
	private JButton btnReceive;
	private JButton btnShareScreen;
	private Socket soc;
	/**
	 * Create the frame.
	 * @throws LineUnavailableException 
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public ChatRoom(String username, String peer) throws SocketException, UnknownHostException, LineUnavailableException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				close();
			}
		});
		this.username = username;
		this.peer = peer;
		
		initialize();
		this.setTitle(username);
		this.chat = new ChatClient(username,peer);
		chatThread = new Thread(chat);
		chatThread.start();
		new ClientVideo(chat.udp_socket, username, peer).StartReceiveVideo(true);
	}
	public BufferedImage Background(String url)
	{
		BufferedImage img = null;
		try {
			String localDir = System.getProperty("user.dir");
		    img = ImageIO.read(new File(localDir+"\\src\\images\\"+url));
		    Image tmp =img.getScaledInstance( 40,40, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage( 40, 40, BufferedImage.TYPE_INT_ARGB);
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
	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1197, 740);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel video_panel = new JPanel();
		video_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		video_panel.setBounds(10, 165, 805, 518);
		panel.add(video_panel);
		video_panel.setLayout(null);
		
		img_server = new JLabel("");
		img_server.setBounds(10, 10, 785, 498);
		video_panel.add(img_server);
		
		JPanel user_panel = new JPanel();
		user_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		user_panel.setBounds(10, 10, 550, 145);
		panel.add(user_panel);
		user_panel.setLayout(null);
		
		JButton btn_Video_voice = new JButton("Video Call");
		btn_Video_voice.setBounds(43, 23, 108, 21);
		user_panel.add(btn_Video_voice);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setEnabled(false);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientVideo.checkSend=false;
                btnCancel.setEnabled(false);
				btn_Video_voice.setEnabled(true);
			}
		});
		btnCancel.setBounds(43, 79, 108, 21);
		user_panel.add(btnCancel);
		
		btnShareScreen = new JButton("Share Screen");
		btnShareScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Send!=true)
				{
					Send=true;
					btnShareScreen.setText("Stop Screen");
					//						Home h1=new Home();
					  new Thread(new Runnable() {
					        @Override
					        public void run() {
					          try {
					                Robot rob = new Robot();
					                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
									soc = new Socket(NameServer.getServerAddress(), 7749);	
									DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
							    	
							    	dout.writeBoolean(true);
							    	dout.writeUTF(username); 
					                while (Send) {
					                	
					                	System.out.print("1111");
					                	 try {

					                        BufferedImage img = rob.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));

					                        ByteArrayOutputStream ous = new ByteArrayOutputStream();
					                        ImageIO.write(img, "png", ous);
					                        if(img==null){
					                        	System.out.print("khon");
					                        }
					            			System.out.print("Started!!!");
					            			new Thread(new Runnable() {
					            				@Override
										        public void run() {
					            			try {
												DataOutputStream dOut = new DataOutputStream(soc.getOutputStream());
												dOut.writeInt(ous.toByteArray().length);
												Thread.sleep(10);
												dOut.write(ous.toByteArray());
												dOut.flush();
											} catch (IOException | InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
					            				}
					            			}).start();
					            			System.out.println("Started222!!!");
					                        Thread.sleep(500);
			
					                        }
					                	 catch (Exception e) {
					                		 e.printStackTrace();
					                	 }
					                	 System.out.print("Started4444!!!");
					                }
					                
					            } catch (Exception e) { 
					            	e.printStackTrace();
					           
//				                        JOptionPane.showMessageDialog(null, e);
					            }
					            Send = false;

					        }
					    }).start();
					
				}
				else {
					btnShareScreen.setText("Share Screen");
					Send=false;
				}
			}
		});
		btnShareScreen.setBounds(280, 23, 117, 21);
		user_panel.add(btnShareScreen);
		
		btnReceive = new JButton("Receive");
		btnReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
			            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			                if ("Nimbus".equals(info.getName())) {
			                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
			                    break;
			                }
			            }
			        } catch (ClassNotFoundException ex) {
			            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			        } catch (InstantiationException ex) {
			            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			        } catch (IllegalAccessException ex) {
			            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
			            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			        }
				 
				java.awt.EventQueue.invokeLater(new Runnable() {
		            public void run() {
		                try {
							new Home(false,peer).setVisible(true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        });
				
			}
		});
		btnReceive.setBounds(280, 79, 117, 21);
		user_panel.add(btnReceive);
		btn_Video_voice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new ClientVideo(chat.udp_socket, username, peer).StartSendVideo(true);
					btnCancel.setEnabled(true);
					btn_Video_voice.setEnabled(false);
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		});
		
		chatbox_panel = new JPanel();
		chatbox_panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		chatbox_panel.setBounds(825, 10, 338, 673);
		panel.add(chatbox_panel);
		chatbox_panel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 618, 243, 44);
		chatbox_panel.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = textField.getText().trim();
				if(!msg.equals("")) {
					Message m = new Message(username,peer, msg);
					try {
						chat.sendMessage(m);
						textField.setText("");
						updateChat_send(msg);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSend.setBackground(Color.WHITE);
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnSend.setBounds(263, 618, 65, 44);
		chatbox_panel.add(btnSend);
		btnSend.setIcon(new ImageIcon(Background("send.png")));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 318, 598);
		chatbox_panel.add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		scrollPane.setViewportView(textPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(570, 10, 239, 145);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		img_client = new JLabel("");
		img_client.setBounds(10, 10, 219, 129);
		panel_1.add(img_client);
	}
	
	public static void updateChat_receive(String sender,String msg) {
		appendToPane(textPane, "<div class='left' style='width: 40%; background-color: #f1f0f0;'>"+"<strong>"+sender+": </strong>" + msg +"</div>");
	}

	public void updateChat_send(String msg) {
		appendToPane(textPane, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%; '></td>"
				+ "<td style='width: 40%; background-color: #0084ff;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}
	
	public void updateChat_notify(String msg) {
		appendToPane(textPane, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%; '></td>"
				+ "<td style='width: 40%; background-color: #f1c40f;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}
	
	@SuppressWarnings("deprecation")
	public void close() {
		try {
			Disconnect obj = new Disconnect(username, peer);
			new ObjectOutputStream(chat.socket.getOutputStream()).writeObject(obj);
			ClientVideo.checkSend=false;
			chat.socket.close();
			chatThread.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// send html to pane
	public static void appendToPane(JTextPane tp, String msg){
		  HTMLDocument doc = (HTMLDocument)tp.getDocument();
		    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
		    try {
		    	
		      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
		      tp.setCaretPosition(doc.getLength());
		      
		    } catch(Exception e){
		      e.printStackTrace();
		    }
	}
	
	public void showConfirmPanel(String msg) {
		int dialogResult = JOptionPane.showConfirmDialog 
				(this, msg);
	}
	
	public void closeWindow() {
		this.setVisible(false);
		this.close();
	}
	
	class ChatClient implements Runnable{
		private final int port = 9999;
		
		String username;
		String peer;
		Socket socket;
		DatagramSocket udp_socket;
		
		public ChatClient(String username, String peer) {
			this.username = username;
			this.peer = peer;
			try {
				this.socket = new Socket(NameServer.getServerAddress(),port);
				this.udp_socket = new DatagramSocket();
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(username);
				MessageUDP sMessage=new MessageUDP(this.username,"",new byte[1],new byte[1]);		
				DatagramPacket packet =  new DatagramPacket( Convert.serialize(sMessage), Convert.serialize(sMessage).length,NameServer.getServerAddress(),6789);
				this.udp_socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			System.out.println("Chat client is running");
			// TODO Auto-generated method stub
			ObjectInputStream read = null;
			while(!socket.isClosed()) {
				try {
					read = new ObjectInputStream(this.socket.getInputStream());
					Object obj = read.readObject();
					if(obj instanceof BroadcastMessage) {
						if(((BroadcastMessage)obj).getName().equals(peer)) {
							System.out.println("get duoc broadcast");
							showConfirmPanel(peer+ " da ngat ket noi!");
							closeWindow();
							close();
							break;
						}
					}
					if(obj instanceof Message) {
						System.out.println("received a message: "+ ((Message)obj).getMessage());
						String msg = ((Message)obj).getMessage();
						String sender = ((Message)obj).getName();
						updateChat_receive(sender, msg);
					}
					//read.reset();
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		
		public void sendMessage(Object obj) throws IOException {
			ObjectOutputStream outPeer = new ObjectOutputStream(this.socket.getOutputStream());
			// only send text
			if (obj instanceof Message) {
				outPeer.writeObject(obj);
				outPeer.flush();
			} 
			// send attach file
//			else if (obj instanceof DataFile) {
//				outPeer.writeObject(obj);
//				outPeer.flush();
//			}
		}
		
	}
}
