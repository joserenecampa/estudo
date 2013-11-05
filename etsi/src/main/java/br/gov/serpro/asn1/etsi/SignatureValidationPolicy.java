package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class SignatureValidationPolicy extends ASN1Object {
	
	private SigningPeriod signingPeriod;
	private CommonRules commonRules;
	private CommitmentRules commitmentRules;
	private SignPolExtensions signPolExtensions;

	public SignPolExtensions getSignPolExtensions() {
		return signPolExtensions;
	}

	public void setSignPolExtensions(SignPolExtensions signPolExtensions) {
		this.signPolExtensions = signPolExtensions;
	}

	public SigningPeriod getSigningPeriod() {
		return signingPeriod;
	}

	public void setSigningPeriod(SigningPeriod signingPeriod) {
		this.signingPeriod = signingPeriod;
	}

	public CommonRules getCommonRules() {
		return commonRules;
	}

	public void setCommonRules(CommonRules commonRules) {
		this.commonRules = commonRules;
	}

	public CommitmentRules getCommitmentRules() {
		return commitmentRules;
	}

	public void setCommitmentRules(CommitmentRules commitmentRules) {
		this.commitmentRules = commitmentRules;
	}

	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		
		this.signingPeriod = new SigningPeriod();
		this.signingPeriod.parse(derSequence.getObjectAt(0).getDERObject());
		
		this.commonRules = new CommonRules();
		this.commonRules.parse(derSequence.getObjectAt(1).getDERObject());
		
		this.commitmentRules = new CommitmentRules();
		this.commitmentRules.parse(derSequence.getObjectAt(2).getDERObject());
		
		if (derSequence.size() == 4) {
			this.signPolExtensions = new SignPolExtensions();
			this.signPolExtensions.parse(derSequence.getObjectAt(3).getDERObject());
		}
		
	}

}
