package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class VerifierRules extends ASN1Object {

	private MandatedUnsignedAttr mandatedUnsignedAttr;
	private SignPolExtensions signPolExtensions;
	
	public MandatedUnsignedAttr getMandatedUnsignedAttr() {
		return mandatedUnsignedAttr;
	}

	public void setMandatedUnsignedAttr(MandatedUnsignedAttr mandatedUnsignedAttr) {
		this.mandatedUnsignedAttr = mandatedUnsignedAttr;
	}

	public SignPolExtensions getSignPolExtensions() {
		return signPolExtensions;
	}

	public void setSignPolExtensions(SignPolExtensions signPolExtensions) {
		this.signPolExtensions = signPolExtensions;
	}

	@Override
	public void parse(DERObject derObject) {
		
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		
		this.mandatedUnsignedAttr = new MandatedUnsignedAttr();
		this.mandatedUnsignedAttr.parse(derSequence.getObjectAt(0).getDERObject());
		
		if (derSequence.size() == 2) {
			this.signPolExtensions = new SignPolExtensions();
			this.signPolExtensions.parse(derSequence.getObjectAt(1).getDERObject());
		}
	}

}
