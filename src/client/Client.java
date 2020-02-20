package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField tfMessage;
	private JTextArea chatArea;
	private JTextArea tbConectados;
	private ArrayList<Login> conectados;
	private Login login;
	private String nick;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		nick= JOptionPane.showInputDialog("Nick:   ");
		login=new Login(nick);
		addWindowListener(new EventHandler(login));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl = new JLabel("Nick:");
		lbl.setBounds(22, 27, 56, 16);
		contentPane.add(lbl);
		
		JLabel lblNick = new JLabel(nick);
		lblNick.setBounds(110, 27, 56, 16);
		contentPane.add(lblNick);
		
		chatArea = new JTextArea();
		chatArea.setBounds(22, 58, 307, 122);
		contentPane.add(chatArea);
		chatArea.setEditable(false);

		tfMessage = new JTextField();
		tfMessage.setBounds(22, 193, 307, 22);
		contentPane.add(tfMessage);
		tfMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendMessage(nick+": "+tfMessage.getText());
				StringBuilder sb=new StringBuilder(chatArea.getText());
				sb.append("\n").append(tfMessage.getText());
				chatArea.setText(sb.toString());
				tfMessage.setText("");
			}
		});
		btnSend.setBounds(257, 228, 97, 25);
		contentPane.add(btnSend);
		
		tbConectados = new JTextArea();
		tbConectados.setBounds(341, 58, 79, 122);
		contentPane.add(tbConectados);
		tbConectados.setEditable(false);
		
	}
	
	class serverListener implements Runnable{

		@Override
		public void run() {
			try {
				ServerSocket serverSocket=new ServerSocket(9898);
				while(true) {
					Socket mySocket=serverSocket.accept();
					ObjectInputStream oos=new ObjectInputStream(mySocket.getInputStream());
					Object o= oos.readObject();
					if(o instanceof Login) {
						Login l=(Login) o;
						if(l.isDesconexion()) {
							l.setDesconexion(false);
							conectados.remove(l);
						}else {
							conectados.add(l);
						}
						
						StringBuilder sb=new StringBuilder();
						for(Login conexion:conectados) {
							sb.append(conexion.getNickName()+"\n ");
						}
						tbConectados.setText(sb.toString());
					}else if(o instanceof ChatMessage) {
						ChatMessage cm=(ChatMessage)o;
						StringBuilder sb=new StringBuilder(chatArea.getText());
						sb.append("\n ").append(cm.getNick())
						.append(": ").append(cm.getTexto());
						chatArea.setText(sb.toString());
						
							
					}
					
				}
			} catch (IOException e) {
			
			} catch (ClassNotFoundException ex) {
			
		
			}
			
		}
		
		
	}
	public void sendMessage(String text) {
		try {
			
			Socket miSocket=new Socket("192.168.0.100",9696);
			ObjectOutputStream oos=new ObjectOutputStream(miSocket.getOutputStream());
			ChatMessage message=new ChatMessage(login.getNickName(),text);
			oos.writeObject(message);
			oos.close();
			miSocket.close();
			
		}catch (Exception ex) {
		 System.out.println("Socket did not work");	
		}
	}
	
	

	
	class EventHandler extends WindowAdapter{
		
		private Login login;
		public EventHandler(Login login) {
			this.login=login;
			
		}
		
		public void windowOpened(WindowEvent e) {
			try {
				
				Socket miSocket=new Socket("192.168.0.100",9696);
				ObjectOutputStream oos=new ObjectOutputStream(miSocket.getOutputStream());
				oos.writeObject(login);
				oos.close();
				miSocket.close();
				
			}catch (Exception ex) {
			 System.out.println("Socket did not work");	
			}
		}
		
		public void windowClosed(WindowEvent e) {
			try {
				
				Socket miSocket=new Socket("192.168.0.100",9696);
				ObjectOutputStream oos=new ObjectOutputStream(miSocket.getOutputStream());	
				oos.writeObject(login);
				oos.close();
				miSocket.close();
				
			}catch (Exception ex) {
			 System.out.println("Socket did not work");	
			}
			
		}
	}

}
