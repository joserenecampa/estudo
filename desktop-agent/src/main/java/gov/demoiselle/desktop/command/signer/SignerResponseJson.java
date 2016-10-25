package gov.demoiselle.desktop.command.signer;

import gov.demoiselle.desktop.command.json.CertificateJson;

public class SignerResponseJson {
	
	private String signed;
	private CertificateJson by;
	private String publicKey;
	
	public String getSigned() {
		return signed;
	}
	public void setSigned(String signed) {
		this.signed = signed;
	}
	public CertificateJson getBy() {
		return by;
	}
	public void setBy(CertificateJson by) {
		this.by = by;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	

}
