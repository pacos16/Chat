package client;

import java.io.Serializable;
import java.util.Date;

public class Login implements Serializable {
	
	private String nickName;
	private Date date;
	private boolean desconexion;
	public Login() {
		
	}
	public Login(String nickname) {
		this.nickName=nickname;
		date=new Date();
		desconexion=false;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isDesconexion() {
		return desconexion;
	}
	public void setDesconexion(boolean desconexion) {
		this.desconexion = desconexion;
	}
	
	
	
	

}
