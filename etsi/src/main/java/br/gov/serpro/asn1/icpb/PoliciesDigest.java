package br.gov.serpro.asn1.icpb;

import org.bouncycastle.asn1.esf.OtherHashAlgAndValue;

import br.gov.serpro.asn1.ASN1Object;

public class PoliciesDigest extends ASN1Object {
	
	enum TAG {
		textualPolicyDigest(0), asn1PolicyDigest(1), xmlPolicyDigest(2);
		int value;
		private TAG(int value) { this.value = value; }
		public static TAG getTag(int value) {
			for (TAG tag : TAG.values()) if (tag.value == value) return tag; return null;
		}
	}

	private OtherHashAlgAndValue textualPolicyDigest;
	private OtherHashAlgAndValue asn1PolicyDigest;
	private OtherHashAlgAndValue xmlPolicyDigest;	

}
