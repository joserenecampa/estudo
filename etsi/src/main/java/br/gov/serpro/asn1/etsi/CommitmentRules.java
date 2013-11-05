package br.gov.serpro.asn1.etsi;

import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;


public class CommitmentRules extends ASN1Object {
	
	private Collection<CommitmentRule> commitmentRules;

	public Collection<CommitmentRule> getCommitmentRules() {
		return commitmentRules;
	}

	public void setCommitmentRules(Collection<CommitmentRule> commitmentRules) {
		this.commitmentRules = commitmentRules;
	}
	
	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		int total = derSequence.size();
		for (int i = 0; i < total; i++) {
			CommitmentRule commitmentRule = new CommitmentRule();
			commitmentRule.parse(derSequence.getObjectAt(i).getDERObject());
			if (this.commitmentRules == null)
				this.commitmentRules = new ArrayList<CommitmentRule>();
			this.commitmentRules.add(commitmentRule);
		}
	}

}
