package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERObject;

import br.gov.serpro.asn1.ASN1Object;

public enum EnuRevReq {
	
	clrCheck (0), // Checks shall be made against current CRLs (or authority revocation lists)
	ocspCheck (1), // The revocation status shall be checked using the Online Certificate Status Protocol (RFC 2450)
	bothCheck (2), // Both CRL and OCSP checks shall be carried out
	eitherCheck (3), // At least one of CRL or OCSP checks shall be carried out
	noCheck (4), // no check is mandated
	other (5); // Other mechanism as defined by signature policy extension
	
	private int value;
	
	private EnuRevReq(int value) {
		this.value = value;
	}

	public static EnuRevReq parse(DERObject derObject) {
		DEREnumerated derEnumerated = ASN1Object.getDEREnumerated(derObject);
		int value = derEnumerated.getValue().intValue();
		for (EnuRevReq enuRevReq : EnuRevReq.values())
			if (enuRevReq.value == value)
				return enuRevReq;
		return null;
	}
	

}
