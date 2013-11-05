package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;

import br.gov.serpro.asn1.ASN1Object;

public class ObjectIdentifier extends ASN1Object {
	
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void parse(DERObject derObject) {
		DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier)(derObject);
		this.setValue(derObjectIdentifier.getId());
	}
	
	@Override
	public String toString() {
		return this.value;
	}

}
