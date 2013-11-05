package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERUTF8String;

import br.gov.serpro.asn1.ASN1Object;

public class FieldOfApplication extends ASN1Object {
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void parse(DERObject derObject) {
		if (derObject instanceof DERUTF8String) {
			DERUTF8String derUTF8String = (DERUTF8String)derObject;
			this.setValue(derUTF8String.getString());
		} else {
			this.setValue(derObject.toString());			
		}
	}

}
