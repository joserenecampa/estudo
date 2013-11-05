package br.gov.serpro.asn1.etsi;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;

import br.gov.serpro.asn1.ASN1Object;

public class OctetString extends ASN1Object {

	private String value;
	private DEROctetString derOctetString;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueUTF8() {
		String result = null;
		try {
			result = new String(this.derOctetString.getOctets(), "UTF8");
		} catch (UnsupportedEncodingException error) {
			throw new RuntimeException("Erro ao tentar converter OctetString em String", error);
		}
		return result;
	}

	@Override
	public void parse(DERObject derObject) {
		if (derObject instanceof DEROctetString) {
			this.derOctetString = (DEROctetString) derObject;
			String octetString = derOctetString.toString();
			octetString = octetString.substring(1);
			this.setValue(octetString);
		}
	}
	
}
