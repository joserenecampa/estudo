package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

import br.gov.serpro.asn1.ASN1Object;

public class CommonRules extends ASN1Object {
	
	enum TAG {
		signerAndVeriferRules(0), signingCertTrustCondition(1), timeStampTrustCondition(2),
		attributeTrustCondition(3), algorithmConstraintSet(4), signPolExtensions(5);
		int value;
		private TAG(int value) { this.value = value; }
		public static TAG getTag(int value) {
			for (TAG tag : TAG.values()) if (tag.value == value) return tag; return null;
		}
	}
	
	private SignerAndVerifierRules signerAndVeriferRules;
	private SigningCertTrustCondition signingCertTrustCondition;
	private TimestampTrustCondition timeStampTrustCondition;
	private AttributeTrustCondition attributeTrustCondition;
	private AlgorithmConstraintSet algorithmConstraintSet;
	private SignPolExtensions signPolExtensions;

	public SignerAndVerifierRules getSignerAndVeriferRules() {
		return signerAndVeriferRules;
	}

	public void setSignerAndVeriferRules(
			SignerAndVerifierRules signerAndVeriferRules) {
		this.signerAndVeriferRules = signerAndVeriferRules;
	}

	public SigningCertTrustCondition getSigningCertTrustCondition() {
		return signingCertTrustCondition;
	}

	public void setSigningCertTrustCondition(
			SigningCertTrustCondition signingCertTrustCondition) {
		this.signingCertTrustCondition = signingCertTrustCondition;
	}

	public TimestampTrustCondition getTimeStampTrustCondition() {
		return timeStampTrustCondition;
	}

	public void setTimeStampTrustCondition(
			TimestampTrustCondition timeStampTrustCondition) {
		this.timeStampTrustCondition = timeStampTrustCondition;
	}

	public AttributeTrustCondition getAttributeTrustCondition() {
		return attributeTrustCondition;
	}

	public void setAttributeTrustCondition(
			AttributeTrustCondition attributeTrustCondition) {
		this.attributeTrustCondition = attributeTrustCondition;
	}

	public AlgorithmConstraintSet getAlgorithmConstraintSet() {
		return algorithmConstraintSet;
	}

	public void setAlgorithmConstraintSet(
			AlgorithmConstraintSet algorithmConstraintSet) {
		this.algorithmConstraintSet = algorithmConstraintSet;
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
		int total = derSequence.size();
		
		if (total > 0) {
			for (int i = 0; i < total; i++) {
				DERObject object = derSequence.getObjectAt(i).getDERObject();
				if (object instanceof DERTaggedObject) {
					DERTaggedObject derTaggedObject = (DERTaggedObject)object;
					TAG tag = TAG.getTag(derTaggedObject.getTagNo());
					switch (tag) {
					case signerAndVeriferRules:
						this.signerAndVeriferRules = new SignerAndVerifierRules();
						this.signerAndVeriferRules.parse(object);
						break;
					case signingCertTrustCondition:
						this.signingCertTrustCondition = new SigningCertTrustCondition();
						this.signingCertTrustCondition.parse(object);
						break;
					case timeStampTrustCondition:
						this.timeStampTrustCondition = new TimestampTrustCondition();
						this.timeStampTrustCondition.parse(object);
						break;
					case attributeTrustCondition:
						this.attributeTrustCondition = new AttributeTrustCondition();
						this.attributeTrustCondition.parse(object);
						break;
					case algorithmConstraintSet:
						this.algorithmConstraintSet = new AlgorithmConstraintSet();
						this.algorithmConstraintSet.parse(object);
						break;
					case signPolExtensions:
						this.signPolExtensions = new SignPolExtensions();
						this.signPolExtensions.parse(object);
						break;
					default:
						break;
					}
				}
			}
		}
	}

}
