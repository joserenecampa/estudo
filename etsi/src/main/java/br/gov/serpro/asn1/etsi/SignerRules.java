package br.gov.serpro.asn1.etsi;

import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

import br.gov.serpro.asn1.ASN1Object;

public class SignerRules extends ASN1Object {
	
	enum TAG {
		mandatedCertificateRef(0), mandatedCertificateInfo(1), signPolExtensions(2);
		int value;
		private TAG(int value) { this.value = value; }
		public static TAG getTag(int value) {
			for (TAG tag : TAG.values()) if (tag.value == value) return tag; return null;
		}
	}
	
	/* 
	 * True if signed data is external to CMS structure
	 * False if signed data part of CMS structure
	 * not present if either allowed
	 */
	private Boolean externalSignedData = null;
	
	/* Mandated CMS signed attributes */
	private CMSAttrs mandatedSignedAttr;
	
	/* Mandated CMS unsigned attributed */
	private CMSAttrs mandatedUnsignedAttr;
	
	/* Mandated Certificate Reference */
	private CertRefReq mandatedCertificateRef = CertRefReq.signerOnly;
	
	/* Mandated Certificate Info */
	private CertInfoReq mandatedCertificateInfo = CertInfoReq.none;
	
	private SignPolExtensions signPolExtensions;	

	public Boolean getExternalSignedData() {
		return externalSignedData;
	}
	
	public void setExternalSignedData(Boolean externalSignedData) {
		this.externalSignedData = externalSignedData;
	}

	public CMSAttrs getMandatedSignedAttr() {
		return mandatedSignedAttr;
	}

	public void setMandatedSignedAttr(CMSAttrs mandatedSignedAttr) {
		this.mandatedSignedAttr = mandatedSignedAttr;
	}

	public CMSAttrs getMandatedUnsignedAttr() {
		return mandatedUnsignedAttr;
	}

	public void setMandatedUnsignedAttr(CMSAttrs mandatedUnsignedAttr) {
		this.mandatedUnsignedAttr = mandatedUnsignedAttr;
	}

	public CertRefReq getMandatedCertificateRef() {
		return mandatedCertificateRef;
	}

	public void setMandatedCertificateRef(CertRefReq mandatedCertificateRef) {
		this.mandatedCertificateRef = mandatedCertificateRef;
	}

	public CertInfoReq getMandatedCertificateInfo() {
		return mandatedCertificateInfo;
	}

	public void setMandatedCertificateInfo(CertInfoReq mandatedCertificateInfo) {
		this.mandatedCertificateInfo = mandatedCertificateInfo;
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
					case mandatedCertificateRef:
						this.mandatedCertificateRef = CertRefReq.parse(object);
						break;
					case mandatedCertificateInfo:
						this.mandatedCertificateInfo = CertInfoReq.parse(object);
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
		
		int i = 0;
		DEREncodable object = derSequence.getObjectAt(i);
		if (!(object instanceof DERSequence)) {
			if (object instanceof DERBoolean)
				this.externalSignedData = ((DERBoolean)object).isTrue();
			i++;
		}
		this.mandatedSignedAttr = new CMSAttrs();
		this.mandatedSignedAttr.parse(derSequence.getObjectAt(i).getDERObject());
		i++;
		this.mandatedUnsignedAttr = new CMSAttrs();
		this.mandatedUnsignedAttr.parse(derSequence.getObjectAt(i).getDERObject());
	}

}
