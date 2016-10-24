package gov.demoiselle.desktop.command.json;

public class EncriptResponseJson {
	
	private String encripted;
	private CertificateJson by;
	private String publicKey;
	
	public String getEncripted() {
		return encripted;
	}
	public void setEncripted(String encripted) {
		this.encripted = encripted;
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
