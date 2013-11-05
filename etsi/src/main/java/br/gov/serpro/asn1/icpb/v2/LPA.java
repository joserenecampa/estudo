package br.gov.serpro.asn1.icpb.v2;

import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;
import br.gov.serpro.asn1.GeneralizedTime;

public class LPA extends ASN1Object {
	
	private Version version;
	private Collection<PolicyInfo> policyInfos;
	private GeneralizedTime nextUpdate;

	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	public Collection<PolicyInfo> getPolicyInfos() {
		return policyInfos;
	}
	public void setPolicyInfos(Collection<PolicyInfo> policyInfos) {
		this.policyInfos = policyInfos;
	}
	public GeneralizedTime getNextUpdate() {
		return nextUpdate;
	}
	public void setNextUpdate(GeneralizedTime nextUpdate) {
		this.nextUpdate = nextUpdate;
	}
	
	public void parse(DERObject derObject) {
		DERSequence sequence = ASN1Object.getDERSequence(derObject);
		DERObject firstObject = sequence.getObjectAt(0).getDERObject();
		this.version = new Version();
		int indice = 0;
		if (firstObject instanceof DERInteger) {
			this.version.parse(firstObject);
			indice++;
		}
		DERObject policyInfos = sequence.getObjectAt(indice).getDERObject();
		DERSequence policyInfosSequence = (DERSequence)policyInfos;
		if (policyInfosSequence != null && policyInfosSequence.size() > 0) {
			this.policyInfos = new ArrayList<PolicyInfo>();
			for (int i = 0; i < policyInfosSequence.size(); i++) {
				PolicyInfo policyInfo = new PolicyInfo();
				policyInfo.parse(policyInfosSequence.getObjectAt(i).getDERObject());
				this.policyInfos.add(policyInfo);
			}
		}
		this.nextUpdate = new GeneralizedTime();
		this.nextUpdate.parse(sequence.getObjectAt(indice+1).getDERObject());
	}
}
