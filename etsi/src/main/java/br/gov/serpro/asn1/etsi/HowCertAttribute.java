package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERObject;

import br.gov.serpro.asn1.ASN1Object;

public enum HowCertAttribute {
	
	claimedAttribute (0),
	certifiedAttribtes (1),
	either (2);
	
	private int value;
	
	private HowCertAttribute(int value) {
		this.value = value;
	}

	public static HowCertAttribute parse(DERObject derObject) {
		DEREnumerated derEnumerated = ASN1Object.getDEREnumerated(derObject);
		int value = derEnumerated.getValue().intValue();
		for (HowCertAttribute howCertAttribute : HowCertAttribute.values())
			if (howCertAttribute.value == value)
				return howCertAttribute;
		return null;
	}
	

}
