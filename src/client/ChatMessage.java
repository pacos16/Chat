package client;

import java.io.Serializable;

public class ChatMessage implements Serializable{
	
	private String nick;
	private String texto;
	
	public ChatMessage() {
		
	}

	public ChatMessage(String nick, String texto) {
		super();
		this.nick = nick;
		this.texto = texto;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
	
	

}
