package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class AlgorithmIdentifier extends ASN1Object {
	
	private ObjectIdentifier algorithm;
	private Object parameters;
	
	public ObjectIdentifier getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(ObjectIdentifier algorithm) {
		this.algorithm = algorithm;
	}
	public Object getParameters() {
		return parameters;
	}
	public void setParameters(Object parameters) {
		this.parameters = parameters;
	}

	@Override
	public void parse(DERObject derObject) {
		this.algorithm = new ObjectIdentifier();
		DERSequence derSequence = (DERSequence)derObject;
		this.algorithm.parse(derSequence.getObjectAt(0).getDERObject());
	}

}
