package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERObject;

import br.gov.serpro.asn1.ASN1Object;

public enum CertInfoReq {

	none(0), signerOnly(1), fullPath(2);
	
	private int value;
	
	private CertInfoReq(int value) {
		this.value = value;
	}

	public static CertInfoReq parse(DERObject derObject) {
		DEREnumerated derEnumerated = ASN1Object.getDEREnumerated(derObject);
		int value = derEnumerated.getValue().intValue();
		for (CertInfoReq certInfoReq : CertInfoReq.values())
			if (certInfoReq.value == value)
				return certInfoReq;
		return null;
	}
}
