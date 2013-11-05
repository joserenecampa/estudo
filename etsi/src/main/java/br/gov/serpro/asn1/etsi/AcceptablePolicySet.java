package br.gov.serpro.asn1.etsi;

import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class AcceptablePolicySet extends ASN1Object {
	
	private Collection<CertPolicyId> certPolicyIds;
	
	public Collection<CertPolicyId> getCertPolicyIds() {
		return certPolicyIds;
	}

	public void setCertPolicyIds(Collection<CertPolicyId> certPolicyIds) {
		this.certPolicyIds = certPolicyIds;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		int total = derSequence.size();
		for (int i = 0; i < total; i++) {
			CertPolicyId certPolicyId = new CertPolicyId();
			certPolicyId.parse(derSequence.getObjectAt(i).getDERObject());
			if (this.certPolicyIds == null)
				this.certPolicyIds = new ArrayList<CertPolicyId>();
			this.certPolicyIds.add(certPolicyId);
		}
	}

}
