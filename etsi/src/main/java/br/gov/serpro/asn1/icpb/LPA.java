package br.gov.serpro.asn1.icpb;

import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class LPA extends ASN1Object {
	
	private Collection<PolicyInfo> policyInfos;
	private Time nextUpdate;
	
	public Collection<PolicyInfo> getPolicyInfos() {
		return policyInfos;
	}
	public void setPolicyInfos(Collection<PolicyInfo> policyInfos) {
		this.policyInfos = policyInfos;
	}
	public Time getNextUpdate() {
		return nextUpdate;
	}
	public void setNextUpdate(Time nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence sequence = ASN1Object.getDERSequence(derObject);
		DERObject policyInfos = sequence.getObjectAt(0).getDERObject();
		DERSequence policyInfosSequence = (DERSequence)policyInfos;
		if (policyInfosSequence != null && policyInfosSequence.size() > 0) {
			this.policyInfos = new ArrayList<PolicyInfo>();
			for (int i = 0; i < policyInfosSequence.size(); i++) {
				PolicyInfo policyInfo = new PolicyInfo();
				policyInfo.parse(policyInfosSequence.getObjectAt(i).getDERObject());
				this.policyInfos.add(policyInfo);
			}
		}
		this.nextUpdate = new Time();
		this.nextUpdate.parse(sequence.getObjectAt(1).getDERObject());
	}
	
}