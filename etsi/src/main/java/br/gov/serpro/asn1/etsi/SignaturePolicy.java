package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class SignaturePolicy {
	
	private AlgorithmIdentifier signPolicyHashAlg;
	private SignPolicyInfo signPolicyInfo;
	private SignPolicyHash signPolicyHash;
	
	public AlgorithmIdentifier getSignPolicyHashAlg() {
		return signPolicyHashAlg;
	}
	public void setSignPolicyHashAlg(AlgorithmIdentifier signPolicyHashAlg) {
		this.signPolicyHashAlg = signPolicyHashAlg;
	}
	public SignPolicyInfo getSignPolicyInfo() {
		return signPolicyInfo;
	}
	public void setSignPolicyInfo(SignPolicyInfo signPolicyInfo) {
		this.signPolicyInfo = signPolicyInfo;
	}
	public SignPolicyHash getSignPolicyHash() {
		return signPolicyHash;
	}
	public void setSignPolicyHash(SignPolicyHash signPolicyHash) {
		this.signPolicyHash = signPolicyHash;
	}

	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		this.signPolicyHashAlg = new AlgorithmIdentifier();
		this.signPolicyHashAlg.parse(derSequence.getObjectAt(0).getDERObject());
		this.signPolicyInfo = new SignPolicyInfo();
		this.signPolicyInfo.parse(derSequence.getObjectAt(1).getDERObject());
		if (derSequence.size() == 3) {
			this.signPolicyHash = new SignPolicyHash();
			this.signPolicyHash.parse(derSequence.getObjectAt(2).getDERObject());
		}
	}
	

}
