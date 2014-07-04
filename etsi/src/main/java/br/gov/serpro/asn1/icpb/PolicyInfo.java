package br.gov.serpro.asn1.icpb;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.DirectoryString;

import br.gov.serpro.asn1.ASN1Object;
import br.gov.serpro.asn1.etsi.SigningPeriod;

public class PolicyInfo extends ASN1Object {
	
	private DirectoryString policyName;
	private DirectoryString fieldOfApplication;
	private SigningPeriod signingPeriod;
	private Time revocationDate;
	private PoliciesURI policiesURI;
	private PoliciesDigest policiesDigest;
	
	public DirectoryString getPolicyName() {
		return policyName;
	}
	public void setPolicyName(DirectoryString policyName) {
		this.policyName = policyName;
	}
	public DirectoryString getFieldOfApplication() {
		return fieldOfApplication;
	}
	public void setFieldOfApplication(DirectoryString fieldOfApplication) {
		this.fieldOfApplication = fieldOfApplication;
	}
	public Time getRevocationDate() {
		return revocationDate;
	}
	public void setRevocationDate(Time revocationDate) {
		this.revocationDate = revocationDate;
	}
	public SigningPeriod getSigningPeriod() {
		return signingPeriod;
	}
	public void setSigningPeriod(SigningPeriod signingPeriod) {
		this.signingPeriod = signingPeriod;
	}
	public PoliciesURI getPoliciesURI() {
		return policiesURI;
	}
	public void setPoliciesURI(PoliciesURI policiesURI) {
		this.policiesURI = policiesURI;
	}
	public PoliciesDigest getPoliciesDigest() {
		return policiesDigest;
	}
	public void setPoliciesDigest(PoliciesDigest policiesDigest) {
		this.policiesDigest = policiesDigest;
	}
	
	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		DERObject firstObject = derSequence.getObjectAt(0).getDERObject();
		this.policyName = new DirectoryString(firstObject.toString());
		DERObject secondObject = derSequence.getObjectAt(1).getDERObject();
		String fieldOfApplication = secondObject.toString();
		this.fieldOfApplication = new DirectoryString(fieldOfApplication);
		this.signingPeriod = new SigningPeriod();
		this.signingPeriod.parse(derSequence.getObjectAt(2).getDERObject());
		
		int indice = 3;
		DERObject revocationObject = derSequence.getObjectAt(indice).getDERObject();
		if (!(secondObject instanceof DERTaggedObject))
			indice = 4;
		if (indice == 3) {
			this.revocationDate = new Time();
			this.revocationDate.parse(revocationObject);
		}
	}

}
