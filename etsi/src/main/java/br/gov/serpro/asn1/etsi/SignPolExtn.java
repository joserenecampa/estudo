package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class SignPolExtn extends ASN1Object {
	
	private ObjectIdentifier extnID;
	private OctetString extnValue;
	
	public ObjectIdentifier getExtnID() {
		return extnID;
	}

	public void setExtnID(ObjectIdentifier extnID) {
		this.extnID = extnID;
	}

	public OctetString getExtnValue() {
		return extnValue;
	}

	public void setExtnValue(OctetString extnValue) {
		this.extnValue = extnValue;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject); 
		
		this.extnID = new ObjectIdentifier();
		this.extnID.parse(derSequence.getObjectAt(0).getDERObject());

		this.extnValue = new OctetString();
		this.extnValue.parse(derSequence.getObjectAt(1).getDERObject());
	}

}
