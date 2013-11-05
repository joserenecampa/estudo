package br.gov.serpro.asn1;

import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

public abstract class ASN1Object {
	
	public void parse(DERObject derObject) {
		System.out.println(this.getClass() + " : não implementado");
	}
	
	public static DERSequence getDERSequence(DERObject derObject) {
		DERSequence derSequence = null;
		if (derObject instanceof DERTaggedObject) {
			DERObject object = ((DERTaggedObject)derObject).getObject();
			if (object instanceof DERSequence)
				derSequence = (DERSequence)object;
		} else if (derObject instanceof DERSequence)
			derSequence = (DERSequence)derObject;
		return derSequence;
	}

	public static DEREnumerated getDEREnumerated(DERObject derObject) {
		DEREnumerated derEnumerated = null;
		if (derObject instanceof DERTaggedObject) {
			DERObject object = ((DERTaggedObject)derObject).getObject();
			if (object instanceof DEREnumerated)
				derEnumerated = (DEREnumerated)object;
		} else if (derObject instanceof DEREnumerated)
			derEnumerated = (DEREnumerated)derObject;
		return derEnumerated;
	}
}
