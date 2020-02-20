package chat;

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
		//option pane donde se introduce el nick
		nick= JOptionPane.showInputDialog("Nick:   ");
		if(nick==null || nick.equals("")) nick="Anonimo";
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
		//este evento introduce el mensaje en pantalla y llama a la funcion sendMessage.
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message=nick+": "+tfMessage.getText();
				sendMessage(message);
				StringBuilder sb=new StringBuilder(chatArea.getText());
				sb.append("\n").append(message);
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
		conectados=new ArrayList();
		
		//creacion del hilo de escucha
		Thread t= new Thread(new ServerListener());
		t.start();
		
	}
	//Classe del hilo de escucha
	class ServerListener implements Runnable{

		@Override
		public void run() {
			try {
				ServerSocket serverSocket=new ServerSocket(9393);
				while(true) {
					Socket mySocket=serverSocket.accept();
					ObjectInputStream oos=new ObjectInputStream(mySocket.getInputStream());
					Object o= oos.readObject();
					if(o instanceof Login) {
						Login l=(Login) o;
						if(l.isDesconexion()) {
							l.setDesconexion(false);
							for(Login log:conectados) {
								if(l.getNickName().equals(log.getNickName()) && l.getDate().equals(log.getDate())) 
									l=log;
							}
							
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
						sb.append("\n").append(cm.getTexto());
						chatArea.setText(sb.toString());
						
							
					}
					
					mySocket.close();
					
				}
				
			} catch (IOException e) {
			System.out.println("ioe client");
			} catch (ClassNotFoundException ex) {
			
				System.out.println("cnfe client");
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
	
	

	/**
	 * Event Handler
	 * @author user
	 *Esta classe es la responsable de enviar los paquetes Login 
	 *cuando se abre la ventana y se cierra
	 */
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
		
		public void windowClosing(WindowEvent e) {
			try {
				
				Socket miSocket=new Socket("192.168.0.100",9696);
				ObjectOutputStream oos=new ObjectOutputStream(miSocket.getOutputStream());	
				login.setDesconexion(true);
				oos.writeObject(login);
				oos.close();
				miSocket.close();
				
			}catch (Exception ex) {
			 System.out.println("Socket did not work");	
			}
			
		}
	}

}
