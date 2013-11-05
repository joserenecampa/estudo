package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class SigningCertTrustCondition extends ASN1Object {
	
	private CertificateTrustTrees signerTrustTrees;
	private CertRevReq signerRevReq;

	public CertificateTrustTrees getSignerTrustTrees() {
		return signerTrustTrees;
	}

	public void setSignerTrustTrees(CertificateTrustTrees signerTrustTrees) {
		this.signerTrustTrees = signerTrustTrees;
	}

	public CertRevReq getSignerRevReq() {
		return signerRevReq;
	}

	public void setSignerRevReq(CertRevReq signerRevReq) {
		this.signerRevReq = signerRevReq;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		this.signerTrustTrees = new CertificateTrustTrees();
		this.signerTrustTrees.parse(derSequence.getObjectAt(0).getDERObject());
		this.signerRevReq = new CertRevReq();
		this.signerRevReq.parse(derSequence.getObjectAt(1).getDERObject());
	}

}
