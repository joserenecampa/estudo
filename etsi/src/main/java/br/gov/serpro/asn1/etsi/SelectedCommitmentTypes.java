package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class SelectedCommitmentTypes extends ASN1Object {
	
	private CommitmentType recognizedCommitmentType;
	
	public CommitmentType getRecognizedCommitmentType() {
		return recognizedCommitmentType;
	}
	public void setRecognizedCommitmentType(CommitmentType recognizedCommitmentType) {
		this.recognizedCommitmentType = recognizedCommitmentType;
	}
	
	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		DERObject object = derSequence.getObjectAt(0).getDERObject();
		if (object instanceof DERNull) {
			this.recognizedCommitmentType = null;
		} else if (object instanceof DERSequence) {
			this.recognizedCommitmentType = new CommitmentType();
			this.recognizedCommitmentType.parse(object);
		}
	}

}
