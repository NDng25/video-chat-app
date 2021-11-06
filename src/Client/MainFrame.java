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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
	private int idUser;
	private Client client = null;
	private ReadClient read;
	private boolean isOnChat = false;
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

	private JTextField txtName;
	private JScrollPane scrollPane;
	private JComboBox cbbStatus;
	private JButton btnNewButton;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame(1);
					frame.setVisible(true);
					Object result = JOptionPane.showInputDialog(frame, "Enter printer name:");
					frame.setUsername((String)result);
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

	/**
	 * Create the frame.
	 */
	public MainFrame(int idUser) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
			}
		});
		initialize();
	}
	
	private void initialize() {
		model.addElement("hihi");
		this.idUser = idUser;
		setBounds(100, 100, 558, 573);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_info = new JPanel();
		panel_info.setBackground(UIManager.getColor("info"));
		panel_info.setBounds(10, 10, 524, 116);
		panel.add(panel_info);
		panel_info.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(133, 10, 314, 28);
		panel_info.add(txtName);
		txtName.setColumns(10);
		
		cbbStatus = new JComboBox();
		cbbStatus.setBounds(133, 62, 115, 28);
		panel_info.add(cbbStatus);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Admin\\Pictures\\t\u1EA3i xu\u1ED1ng.jfif"));
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
		btnNewButton.setBounds(406, 47, 108, 58);
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
		return JOptionPane.showConfirmDialog(this, msg) == JOptionPane.YES_OPTION;
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
//		write = new WriteClient(this.username, client.getSocket());
//		write.start();
	}
	
	public void stop() {
		try {
			this.read.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		read.interrupt();
//		write.interrupt();
		try {
			client.getSocket().close();
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
		
		public void newChat(String receiver) {
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
		
		@Override
		public void run() {
			ObjectInputStream ins = null;
			System.out.println("luong doc dang chay");
			try {
				while(true) {
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
							System.out.println("comfirm here");
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
										}
									}
								}).start();
								
							}
						}
						else if(((Message)o).getMessage().equals("Accept!")) {
							showConfirmPanel("Da ket noi voi: "+((Message)o).getName());
							ChatRoom c = new ChatRoom(client.getUsername(), ((Message)o).getName());
							c.setVisible(true);
							isOnChat = true;
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
//	class WriteClient extends Thread{
//		private Socket socket;
//		private String username;
//		public WriteClient(String username, Socket socket) throws IOException {
//			this.socket = socket;
//			this.username = username;
//		}
//		
//		@Override
//		public void run() {
//			ObjectOutputStream outs = null;
//			try {
//				Scanner sc = new Scanner(System.in);
//				while(true) {
//					outs = new ObjectOutputStream(socket.getOutputStream());
//					String msg = sc.nextLine();
//					Message m = new Message(0,username,msg);
//					outs.writeObject(m);
//					sc.reset();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace(); 
//				try {
//					outs.close();
//					socket.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//			
//		}
//	}
}
