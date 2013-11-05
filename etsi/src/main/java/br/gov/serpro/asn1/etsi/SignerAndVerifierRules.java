package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class SignerAndVerifierRules extends ASN1Object {
	
	private SignerRules signerRules;
	private VerifierRules verifierRules;

	public SignerRules getSignerRules() {
		return signerRules;
	}

	public void setSignerRules(SignerRules signerRules) {
		this.signerRules = signerRules;
	}

	public VerifierRules getVerifierRules() {
		return verifierRules;
	}

	public void setVerifierRules(VerifierRules verifierRules) {
		this.verifierRules = verifierRules;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		
		this.signerRules = new SignerRules();
		this.signerRules.parse(derSequence.getObjectAt(0).getDERObject());

		this.verifierRules = new VerifierRules();
		this.verifierRules.parse(derSequence.getObjectAt(1).getDERObject());
	}

}
