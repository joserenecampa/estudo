package br.gov.serpro.asn1;

import java.text.ParseException;
import java.util.Date;

import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERObject;

public class GeneralizedTime extends ASN1Object {
	
	private Date date;

	@Override
	public void parse(DERObject derObject) {
		if (derObject instanceof DERGeneralizedTime) {
			DERGeneralizedTime derGeneralizedTime = (DERGeneralizedTime)derObject;
			try {
				this.setDate(derGeneralizedTime.getDate());
			} catch (ParseException error) {
				throw new RuntimeException(error);
			}
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
