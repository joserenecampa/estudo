package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;


public class CommitmentRule extends CommonRules {
	
	private SelectedCommitmentTypes selCommitmentTypes;
	
	public SelectedCommitmentTypes getSelCommitmentTypes() {
		return selCommitmentTypes;
	}
	public void setSelCommitmentTypes(SelectedCommitmentTypes selCommitmentTypes) {
		this.selCommitmentTypes = selCommitmentTypes;
	}
	
	@Override
	public void parse(DERObject derObject) {
		super.parse(derObject);
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		this.selCommitmentTypes = new SelectedCommitmentTypes();
		this.selCommitmentTypes.parse(derSequence.getObjectAt(0).getDERObject());
	}

}
