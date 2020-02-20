package chat;

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
	
	/**
	 * ClientListener
	 * @author user
	 *Esta clase es la responsable de recibir y filtrar todos los paquetes 
	 *que recibe de los clientes
	 */
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
					miSocket.close();
					//Enviamos a todos los clientes el objeto
					for(Entry<Login, String> entry:conectados.entrySet()){
						try {
							
						    if(ip.equals(entry.getValue())) {
								
				
							}else {
								Socket clientSocket=new Socket(entry.getValue(),9393);
								ObjectOutputStream oos=
										new ObjectOutputStream(clientSocket.getOutputStream());
								oos.writeObject(o);
								clientSocket.close();
								System.out.println("Paquete enviado a: "+entry.getValue());
							}
						}catch(IOException ioe) {
							System.out.println("ioe enviando paquete "+entry.getValue());

						}catch(Exception e) {
							System.out.println("tenemos un problema");
						}
					}
					//Si se trata de un objeto login comprobamos si es 
					//para conectarse o desconectarse y procedemos a actualizar
					//todos los registros
					if(o instanceof Login) {
						login=(Login) o;
						
						//Si es desconexión busca por todos los registros 
						//y borra el entry correspondiente
						if(login.isDesconexion()) {
							for(Entry<Login, String> entry:conectados.entrySet()){
								if(entry.getValue().equals(ip)) {
									login=entry.getKey();
								}
							}
						
							conectados.remove(login);
							System.out.println("Desconectado: "+ip);
						//en caso contrario lo anyade a la lista de conectados y envia todos
						//los usuarios conectados a nuestro cliente
						}else {
							conectados.put(login,ip);
							System.out.println("Conectado: "+ip);
							for(Entry<Login, String> entry:conectados.entrySet()){
								try {
									
								    if(ip.equals(entry.getValue())) {
										
						
									}else {
										Socket clientSocket=new Socket(ip,9393);
										ObjectOutputStream oos=
												new ObjectOutputStream(clientSocket.getOutputStream());
										oos.writeObject(entry.getKey());
										clientSocket.close();
										System.out.println("Paquete enviado a: "+entry.getValue());
									}
								}catch(IOException ioe) {
									System.out.println("ioe enviando paquete "+entry.getValue());

								}catch(Exception e) {
									System.out.println("tenemos un problema");
								}
							}
							

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



