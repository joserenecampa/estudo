package br.gov.serpro.asn1.etsi;

import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

import br.gov.serpro.asn1.ASN1Object;

public class SignPolExtensions extends ASN1Object {
	
	private Collection<SignPolExtn> extensions;

	public Collection<SignPolExtn> getExtensions() {
		return extensions;
	}

	public void setExtensions(Collection<SignPolExtn> extensions) {
		this.extensions = extensions;
	}

	@Override
	public void parse(DERObject derObject) {
		DERSequence derSequence = ASN1Object.getDERSequence(derObject);
		int total = derSequence.size();
		for (int i = 0; i < total; i++) {
			SignPolExtn signPolExtn = new SignPolExtn();
			signPolExtn.parse(derSequence.getObjectAt(i).getDERObject());
			if (this.extensions == null)
				this.extensions = new ArrayList<SignPolExtn>();
			this.extensions.add(signPolExtn);
		}
	}

}
