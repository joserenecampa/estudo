package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;
import br.gov.serpro.asn1.GeneralizedTime;

public class SignPolicyInfo extends ASN1Object {
	
	private SignPolicyId signPolicyIdentifier;
	private GeneralizedTime dateOfIssue;
	private PolicyIssuerName policyIssuerName;
	private FieldOfApplication fieldOfApplication;
	private SignatureValidationPolicy signatureValidationPolicy;
	private SignPolExtensions signPolExtensions;
	
	public SignPolicyId getSignPolicyIdentifier() {
		return signPolicyIdentifier;
	}
	public void setSignPolicyIdentifier(SignPolicyId signPolicyIdentifier) {
		this.signPolicyIdentifier = signPolicyIdentifier;
	}
	public GeneralizedTime getDateOfIssue() {
		return dateOfIssue;
	}
	public void setDateOfIssue(GeneralizedTime dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}
	public PolicyIssuerName getPolicyIssuerName() {
		return policyIssuerName;
	}
	public void setPolicyIssuerName(PolicyIssuerName policyIssuerName) {
		this.policyIssuerName = policyIssuerName;
	}
	public FieldOfApplication getFieldOfApplication() {
		return fieldOfApplication;
	}
	public void setFieldOfApplication(FieldOfApplication fieldOfApplication) {
		this.fieldOfApplication = fieldOfApplication;
	}
	public SignatureValidationPolicy getSignatureValidationPolicy() {
		return signatureValidationPolicy;
	}
	public void setSignatureValidationPolicy(
			SignatureValidationPolicy signatureValidationPolicy) {
		this.signatureValidationPolicy = signatureValidationPolicy;
	}
	public SignPolExtensions getSignPolExtensions() {
		return signPolExtensions;
	}
	public void setSignPolExtensions(SignPolExtensions signPolExtensions) {
		this.signPolExtensions = signPolExtensions;
	}
	
	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		this.signPolicyIdentifier = new SignPolicyId();
		this.signPolicyIdentifier.parse(derSequence.getObjectAt(0).getDERObject());
		
		this.dateOfIssue = new GeneralizedTime();
		this.dateOfIssue.parse(derSequence.getObjectAt(1).getDERObject());
		
		this.policyIssuerName = new PolicyIssuerName();
		this.policyIssuerName.parse(derSequence.getObjectAt(2).getDERObject());
		
		this.fieldOfApplication = new FieldOfApplication();
		this.fieldOfApplication.parse(derSequence.getObjectAt(3).getDERObject());
		
		this.signatureValidationPolicy = new SignatureValidationPolicy();
		this.signatureValidationPolicy.parse(derSequence.getObjectAt(4).getDERObject());
		
		if (derSequence.size() == 6) {
			this.signPolExtensions = new SignPolExtensions();
			this.signPolExtensions.parse(derSequence.getObjectAt(5).getDERObject());
		}
		
	}
	
}
