package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.UIManager;

import DTO.Message;
import DTO.Request;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class MainFrame extends JFrame {
	private int idUser;
	private Client client = null;
	private ReadClient read;
	private boolean isOnChat = false;
	
	private JTextField txtName;
	private JScrollPane scrollPane;
	private JButton btnNewButton;
	private JLabel lblNewLabel;
//	private WriteClient write;
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	private String username;
	static DefaultListModel<String> model = new DefaultListModel<>();
	JList<String> listActive ;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String s) {
		this.username = s;
	}
	
	public void welcomeMess(String msg) {
		txtName.setText(msg);
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
		            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		                if ("Nimbus".equals(info.getName())) {
		                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		                    break;
		                }
		            }
		        } catch (ClassNotFoundException ex) {
		            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		        } catch (InstantiationException ex) {
		            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		        } catch (IllegalAccessException ex) {
		            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
		            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		        }
				try {
					MainFrame frame = new MainFrame(1);
					frame.setVisible(true);
					Object result = "";
					while(true) {
						result = JOptionPane.showInputDialog(frame, "Please type your name","Welcome",JOptionPane.QUESTION_MESSAGE);
						if((String)result==null){ System.exit(0);}
						if(((String)result).isEmpty()) {
						continue;
						}
						break;
						}
					frame.setUsername(((String)result));
					frame.welcomeMess("Chao "+ ((String)result)+"!");
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							int result = JOptionPane.showConfirmDialog(frame,
						            "Are you sure you want to close this window?", "Close Window?", 
						            JOptionPane.YES_NO_OPTION,
						            JOptionPane.QUESTION_MESSAGE);
							if ((result == JOptionPane.YES_OPTION)){
									frame.close();
						            System.exit(0);
						    }
						}
					});
					Client client = new Client((String)result);
					frame.setClient(client);
					DataOutputStream dout = new DataOutputStream(client.getOut());
					dout.writeUTF(frame.username);
					frame.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	public BufferedImage Background(String url)
	{
		BufferedImage img = null;
		try {
			String localDir = System.getProperty("user.dir");
		    img = ImageIO.read(new File(localDir+"\\src\\images\\"+url));
		    Image tmp =img.getScaledInstance( 108, 95, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage( 108, 95, BufferedImage.TYPE_INT_ARGB);
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
	/**
	 * Create the frame.
	 */
	public MainFrame(int idUser) {
		initialize();
	}
	
	private void initialize() {
		model.addElement("hihi");
		this.idUser = idUser;
		setBounds(100, 100, 558, 573);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_info = new JPanel();
		panel_info.setBackground(Color.PINK);
		panel_info.setBounds(10, 10, 524, 116);
		panel.add(panel_info);
		panel_info.setLayout(null);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		txtName.setBounds(128, 40, 252, 28);
		panel_info.add(txtName);
		txtName.setColumns(10);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Background("user.png")));
		lblNewLabel.setBounds(10, 10, 108, 95);
		panel_info.add(lblNewLabel);
		
		btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String receiver = listActive.getSelectedValue().trim().replace("\n", "");
				boolean isAccept = false;
				System.out.println(receiver);
				//send message request to server
				read.newChat(receiver);
				System.out.println(isAccept);
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!isOnChat) {
							showConfirmPanel("Tu choi ket noi");
						}
					}
				}).start();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton.setBounds(406, 40, 108, 28);
		panel_info.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Danh s\u00E1ch b\u1EA1n b\u00E8");
		lblNewLabel_1.setBounds(10, 137, 108, 22);
		panel.add(lblNewLabel_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 169, 524, 357);
		panel.add(panel_1);
		panel_1.setLayout(null);
		listActive = new JList<>(model);
		scrollPane = new JScrollPane(listActive);
		scrollPane.setBounds(10, 10, 504, 337);
		panel_1.add(scrollPane);
	}
	
	public boolean showConfirmPanel(String msg) {
		int dialogResult = JOptionPane.showConfirmDialog 
				(this, msg);
		return (dialogResult == JOptionPane.YES_OPTION);
	}
	
	public static void resetFriendList() {
		model.clear();
	}

	public static void addFrList(String str) {
		// TODO Auto-generated method stub
		model.addElement(str);

	}
	
	
	public void execute() throws IOException {
		this.read = new ReadClient(client.getSocket());
		this.read.start();
	}
	
	@SuppressWarnings("deprecation")
	public void close() {
		try {
			Object obj = null;
			new ObjectOutputStream(client.getOut()).writeObject(obj);
			client.getSocket().close();
			this.read.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	class ReadClient extends Thread{
		private Socket socket;
		
		public ReadClient(Socket socket) {
			this.socket = socket;
		}
		
		public synchronized void newChat(String receiver) {
			try {
				System.out.println("New chat");
				ObjectOutputStream out = new ObjectOutputStream(getClient().getOut());
				System.out.println(getClient().getUsername());
				Message m = new Message(username,receiver,"start new chat!");
				out.writeObject(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public synchronized void close() {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			ObjectInputStream ins = null;
			System.out.println("luong doc dang chay");
			try {
				while(!socket.isClosed()) {
					ins = new ObjectInputStream(socket.getInputStream());
					Object o = ins.readObject();
					if(o instanceof Request) {
						System.out.println("Nhan ds active");
						List<String> listUser = ((Request)o).getUsers();
						updateFriend(listUser);
					}
					if(o instanceof Message) {
						System.out.println("Nhan message");
						if(((Message)o).getMessage().equals("start new chat!")) {
							System.out.println("Nhan request new chat");
							String name = ((Message)o).getName();
							boolean option = showConfirmPanel(name+" muon bat dau chat!");
							System.out.println("comfirm here: "+option);
							if(option) {
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										ObjectOutputStream out;
										try {
											out = new ObjectOutputStream(socket.getOutputStream());
											Message m = new Message(username,name,"Accept!");
											out.writeObject(m);
											ChatRoom chatroom = new ChatRoom(client.getUsername(),name);
											chatroom.setVisible(true);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (LineUnavailableException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}).start();
							}
						}
						else if(((Message)o).getMessage().equals("Accept!")) {
							showConfirmPanel("Da ket noi voi: "+((Message)o).getName());
							try {
					            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
					                if ("Nimbus".equals(info.getName())) {
					                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
					                    break;
					                }
					            }
					        } catch (ClassNotFoundException ex) {
					            java.util.logging.Logger.getLogger(ChatRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
					        } catch (InstantiationException ex) {
					            java.util.logging.Logger.getLogger(ChatRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
					        } catch (IllegalAccessException ex) {
					            java.util.logging.Logger.getLogger(ChatRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
					        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
					            java.util.logging.Logger.getLogger(ChatRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
					        }
							ChatRoom c = new ChatRoom(client.getUsername(), ((Message)o).getName());
							c.setVisible(true);
							isOnChat = true;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
		}
		
		public void updateFriend(List<String> listUser) {
			MainFrame.resetFriendList();
			for(String item : listUser) {
				if(item != username) {
					MainFrame.addFrList(item+"\n");
				}
			}
		}
	
	}
}