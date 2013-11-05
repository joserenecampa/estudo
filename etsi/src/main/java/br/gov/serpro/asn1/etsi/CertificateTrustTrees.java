package br.gov.serpro.asn1.etsi;

import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class CertificateTrustTrees extends ASN1Object {
	
	private Collection<CertificateTrustPoint> certificateTrustPoints;

	public Collection<CertificateTrustPoint> getCertificateTrustPoints() {
		return certificateTrustPoints;
	}

	public void setCertificateTrustPoints(
			Collection<CertificateTrustPoint> certificateTrustPoints) {
		this.certificateTrustPoints = certificateTrustPoints;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject); 
		int total = derSequence.size();
		for (int i = 0; i < total; i++) {
			CertificateTrustPoint certificateTrustPoint = new CertificateTrustPoint();
			certificateTrustPoint.parse(derSequence.getObjectAt(i).getDERObject());
			if (this.certificateTrustPoints == null)
				this.certificateTrustPoints = new ArrayList<CertificateTrustPoint>();
			this.certificateTrustPoints.add(certificateTrustPoint);
		}
	}

}
