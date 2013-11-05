package br.gov.serpro.asn1.etsi;

import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

import br.gov.serpro.asn1.ASN1Object;

public class PathLenConstraint extends ASN1Object {
	
	private Collection<ObjectIdentifier> pathLenConstraints;

	public Collection<ObjectIdentifier> getPathLenConstraints() {
		return pathLenConstraints;
	}

	public void setPathLenConstraints(
			Collection<ObjectIdentifier> pathLenConstraints) {
		this.pathLenConstraints = pathLenConstraints;
	}

	@Override
	public void parse(DERObject derObject) {
		DERTaggedObject derTaggedObject = (DERTaggedObject)derObject;
		DERSequence derSequence = (DERSequence)derTaggedObject.getObject();
		int total = derSequence.size();
		for (int i = 0; i < total; i++) {
			ObjectIdentifier objectIdentifier = new ObjectIdentifier();
			objectIdentifier.parse(derSequence.getObjectAt(i).getDERObject());
			if (this.pathLenConstraints == null)
				this.pathLenConstraints = new ArrayList<ObjectIdentifier>();
			this.pathLenConstraints.add(objectIdentifier);
		}
	}

}
