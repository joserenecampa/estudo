package gov.demoiselle.desktop.command.cert;

import gov.demoiselle.web.Request;

public class ListCertsRequest extends Request {
	
	private String password;
	
	public ListCertsRequest() {
		super.setCommand("listcerts");
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
