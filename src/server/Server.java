package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;


public class Server {
	
	private HashMap<Login,String> conectados;
	
	private Thread serverThread;
	public Server() {
		conectados=new HashMap<Login,String>();
		serverThread=new Thread(new ClientListener());
		serverThread.run();
		
		
	}
	
	public static void main(String[] args) {
		Server server=new Server();
	
	}
	
	
	class ClientListener implements Runnable{

		@Override
		public void run() {
			try {
				ServerSocket servidor = new ServerSocket(9696);
				Login login;
				String ip;
				
				while(true) {
					
					Socket miSocket= servidor.accept();
					
					ObjectInputStream ois=new ObjectInputStream(miSocket.getInputStream());
					
					Object o= ois.readObject();
					ip=miSocket.getInetAddress().getHostAddress();
					
					for(Entry<Login, String> entry:conectados.entrySet()){
						try {
							System.out.println(entry.getValue());
						    if(ip!=entry.getValue()) {
								Socket clientSocket=new Socket(entry.getValue(),9898);
								ObjectOutputStream oos=
										new ObjectOutputStream(clientSocket.getOutputStream());
								oos.writeObject(o);
								clientSocket.close();
							}
						}catch(Exception e) {
							
						}
					}
					 
					if(o instanceof Login) {
						login=(Login) o;
						if(login.isDesconexion()) {
							conectados.remove(login);
						}else {
							conectados.put(login,ip);
						}
					}
				}
			}catch(IOException ioe) {
				
				System.out.println("ioe");
			
			}catch (ClassNotFoundException e1) {
				
				System.out.println("cnfe");
			}

			
		}
		
	
		
	}

}



