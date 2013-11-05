package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;


public class CertRevReq extends ASN1Object {
	
	private RevReq endCertRevReq;
	private RevReq caCerts;
	
	public RevReq getEndCertRevReq() {
		return endCertRevReq;
	}

	public void setEndCertRevReq(RevReq endCertRevReq) {
		this.endCertRevReq = endCertRevReq;
	}

	public RevReq getCaCerts() {
		return caCerts;
	}

	public void setCaCerts(RevReq caCerts) {
		this.caCerts = caCerts;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);

		this.endCertRevReq = new RevReq();
		this.endCertRevReq.parse(derSequence.getObjectAt(0).getDERObject());

		this.caCerts = new RevReq();
		this.caCerts.parse(derSequence.getObjectAt(1).getDERObject());
	}

}
