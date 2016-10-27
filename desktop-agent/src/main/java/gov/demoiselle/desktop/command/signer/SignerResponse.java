package gov.demoiselle.desktop.command.signer;

import gov.demoiselle.desktop.command.cert.Certificate;
import gov.demoiselle.web.Response;

public class SignerResponse extends Response {
	
	private String signed;
	private Certificate by;
	private String publicKey;
	
	public SignerResponse() {
		super.setCommand("signer");
	}
	
	public String getSigned() {
		return signed;
	}
	public void setSigned(String signed) {
		this.signed = signed;
	}
	public Certificate getBy() {
		return by;
	}
	public void setBy(Certificate by) {
		this.by = by;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	

}
