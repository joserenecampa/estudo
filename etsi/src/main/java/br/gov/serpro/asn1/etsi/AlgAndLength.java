package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

/**
 * AlgAndLength ::= SEQUENCE {
 *   algID OBJECT IDENTIFIER,
 *   minKeyLength INTEGER OPTIONAL, -- Minimum key length in bits
 *   other SignPolExtensions OPTIONAL
 * }
 * @author 09275643784
 *
 */
public class AlgAndLength extends ASN1Object {
	
	private ObjectIdentifier algID;
	private Integer minKeyLength;
	private SignPolExtensions other;
	
	public ObjectIdentifier getAlgID() {
		return algID;
	}
	public void setAlgID(ObjectIdentifier algID) {
		this.algID = algID;
	}
	public Integer getMinKeyLength() {
		return minKeyLength;
	}
	public void setMinKeyLength(Integer minKeyLength) {
		this.minKeyLength = minKeyLength;
	}
	public SignPolExtensions getOther() {
		return other;
	}
	public void setOther(SignPolExtensions other) {
		this.other = other;
	}
	
	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		this.algID = new ObjectIdentifier();
		this.algID.parse(derSequence.getObjectAt(0).getDERObject());
		if (derSequence.size() >= 2) {
			DERInteger derInteger = (DERInteger)derSequence.getObjectAt(1).getDERObject();
			this.setMinKeyLength(derInteger.getValue().intValue());
		}
		if (derSequence.size() == 3) {
			this.other = new SignPolExtensions();
			this.other.parse(derSequence.getObjectAt(2).getDERObject());
		}
	}

}
