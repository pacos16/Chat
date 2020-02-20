package chat;

import java.io.Serializable;
import java.util.Date;

/**
 * Login
 * @author user
 *Esta classe permite dar información sobre el login del usuario
 *tambien se utiliza para desconexiones utilizando la variable
 *booleana desconexion.
 */
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
