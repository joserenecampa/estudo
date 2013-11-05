package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class RevReq extends ASN1Object {
	
	private EnuRevReq enuRevReq;
	private SignPolExtensions exRevReq;
	
	public EnuRevReq getEnuRevReq() {
		return enuRevReq;
	}
	public void setEnuRevReq(EnuRevReq enuRevReq) {
		this.enuRevReq = enuRevReq;
	}
	public SignPolExtensions getExRevReq() {
		return exRevReq;
	}
	public void setExRevReq(SignPolExtensions exRevReq) {
		this.exRevReq = exRevReq;
	}
	
	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		this.enuRevReq = EnuRevReq.parse(derSequence.getObjectAt(0).getDERObject());
		
		if (derSequence.size() == 2) {
			this.exRevReq = new SignPolExtensions();
			this.exRevReq.parse(derSequence.getObjectAt(1).getDERObject());
		}
		
	}
	
}
