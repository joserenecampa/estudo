package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERObject;

import br.gov.serpro.asn1.ASN1Object;

public enum CertRefReq {
	
	signerOnly(1), fullPath(2);
	
	private int value;
	
	private CertRefReq(int value) {
		this.value = value;
	}

	public static CertRefReq parse(DERObject derObject) {
		DEREnumerated derEnumerated = ASN1Object.getDEREnumerated(derObject);
		int value = derEnumerated.getValue().intValue();
		for (CertRefReq certRefReq : CertRefReq.values())
			if (certRefReq.value == value)
				return certRefReq;
		return null;
	}

}
