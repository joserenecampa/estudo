package br.gov.serpro.asn1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;

import br.gov.serpro.asn1.etsi.CMSAttrs;
import br.gov.serpro.asn1.etsi.CommonRules;
import br.gov.serpro.asn1.etsi.ObjectIdentifier;
import br.gov.serpro.asn1.etsi.SignaturePolicy;
import br.gov.serpro.asn1.etsi.SignerRules;
import br.gov.serpro.asn1.icpb.LPA;
import br.gov.serpro.asn1.icpb.Time;
import br.gov.serpro.asn1.icpb.v2.PolicyInfo;

public class Read {
	
	public static final String URL_LPAv2 = "http://politicas.icpbrasil.gov.br/LPAv2.der";
	public static final String URL_LPA   = "http://politicas.icpbrasil.gov.br/LPA.der";
	
	public static DERObject readDERFromURL(URL url) {
		if (url == null)
			return null;
		InputStream is = null;
		DERObject result = null;
		try {
			is = url.openStream();
			result = Read.readDERFromInputStream(is);
			is.close();
		} catch (IOException error) {
			throw new RuntimeException(error);
		}
		return result;
	}

	public static DERObject readDERFromFile(File file) {
		if (file == null)
			return null;
		FileInputStream fis = null;
		DERObject result = null;
		try {
			fis = new FileInputStream(file);
			result = Read.readDERFromInputStream(fis);
			fis.close();
		} catch (IOException error) {
			throw new RuntimeException(error);
		}
		return result;
	}
	
	public static DERObject readDERFromInputStream(InputStream is) {
		if (is == null)
			return null;
		ASN1InputStream asn1is = new ASN1InputStream(is);
		DERObject derObject = null;
		try {
			derObject = asn1is.readObject();
		} catch (IOException error) {
			throw new RuntimeException(error);
		} finally {
			try {
				asn1is.close();
			} catch (IOException error) {
				throw new RuntimeException(error);
			}			
		}
		return derObject;
	}

	private static SignaturePolicy readSignaturePolicy(DERObject derObject) {
		SignaturePolicy signaturePolicy = new SignaturePolicy();
		signaturePolicy.parse(derObject);
		return signaturePolicy;
	}
	
	public static SignaturePolicy readSignaturePolicyFromFile(File file) {
		DERObject derObject = Read.readDERFromFile(file);
		SignaturePolicy signaturePolicy = Read.readSignaturePolicy(derObject);
		return signaturePolicy;
	}
	
	public static SignaturePolicy readSignaturePolicyFromURL(URL url) {
		DERObject derObject = Read.readDERFromURL(url);
		SignaturePolicy signaturePolicy = Read.readSignaturePolicy(derObject);
		return signaturePolicy;
	}

	private static LPA readLPA(DERObject derObject) {
		LPA listaPoliticaAssinatura = new LPA();
		listaPoliticaAssinatura.parse(derObject);
		return listaPoliticaAssinatura;
	}

	public static LPA readLPAFromFile(File file) {
		DERObject derObject = Read.readDERFromFile(file);
		LPA listaPoliticaAssinatura = Read.readLPA(derObject);
		return listaPoliticaAssinatura;
	}
	
	public static LPA readLPAFromURL(URL url) {
		DERObject derObject = Read.readDERFromURL(url);
		LPA listaPoliticaAssinatura = Read.readLPA(derObject);
		return listaPoliticaAssinatura;
	}

	private static br.gov.serpro.asn1.icpb.v2.LPA readLPAv2(DERObject derObject) {
		br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinaturaV2 = new br.gov.serpro.asn1.icpb.v2.LPA();
		listaPoliticaAssinaturaV2.parse(derObject);
		return listaPoliticaAssinaturaV2;
	}

	public static br.gov.serpro.asn1.icpb.v2.LPA readLPAv2FromFile(File file) {
		DERObject derObject = Read.readDERFromFile(file);
		br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinaturaV2 = Read.readLPAv2(derObject);
		return listaPoliticaAssinaturaV2;
	}

	public static br.gov.serpro.asn1.icpb.v2.LPA readLPAv2FromURL(URL url) {
		DERObject derObject = Read.readDERFromURL(url);
		br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinaturaV2 = Read.readLPAv2(derObject);
		return listaPoliticaAssinaturaV2;
	}
	
	public static void printSignaturePolicy(SignaturePolicy signaturePolicy) {
		System.out.println("===================================================");
		System.out.println("Algoritmo Hash da Política.....: " + signaturePolicy.getSignPolicyHashAlg().getAlgorithm().getValue());
		System.out.println("Hash da Política...............: " + signaturePolicy.getSignPolicyHash().getValue());
		System.out.println("OID da Política................: " + signaturePolicy.getSignPolicyInfo().getSignPolicyIdentifier().getValue());
		System.out.println("Data Lancamento da Política....: " + signaturePolicy.getSignPolicyInfo().getDateOfIssue().getDate());
		System.out.println("Emissor da Política............: " + signaturePolicy.getSignPolicyInfo().getPolicyIssuerName());
		System.out.println("Campo de aplicação da Política.: " + signaturePolicy.getSignPolicyInfo().getFieldOfApplication().getValue());
		System.out.println("Politica válida entre..........: " + signaturePolicy.getSignPolicyInfo().getSignatureValidationPolicy().getSigningPeriod());
		System.out.println("Algumas Regras Associadas a esta política:");
		CommonRules commonRules = signaturePolicy.getSignPolicyInfo().getSignatureValidationPolicy().getCommonRules();
		if (commonRules != null) {
			SignerRules signedRules = commonRules.getSignerAndVeriferRules().getSignerRules();
			if (signedRules != null) {
				System.out.println("\tSignerRules:");
				
				Boolean externalSignedData = signedRules.getExternalSignedData();
				CMSAttrs mandatedSignedAttr = signedRules.getMandatedSignedAttr();
				CMSAttrs mandatedUnsignedAttr = signedRules.getMandatedUnsignedAttr();

				if (externalSignedData != null) {
					if (externalSignedData.booleanValue()) {
						System.out.println("\t\tObriga o conteudo original ficar fora da estrutura da assinatura");
					} else {
						System.out.println("\t\tObriga o conteudo original ficar dentro estrutura da assinatura");
					}
				} else {
					System.out.println("\t\tPermite que o conteúdo faça ou não parte da estrutura da assinatura");
				}
				
				if (mandatedSignedAttr != null) {
					System.out.print("\t\tAtributos assinados obrigatórios: ");
					for (ObjectIdentifier oid : mandatedSignedAttr.getObjectIdentifiers())
						System.out.print(oid.getValue() + ", ");
					System.out.println();
				}
				
				if (mandatedUnsignedAttr != null && mandatedUnsignedAttr.getObjectIdentifiers() != null) {
					System.out.print("\t\tAtributos não assinados obrigatórios: ");
					for (ObjectIdentifier oid : mandatedUnsignedAttr.getObjectIdentifiers())
						System.out.print(oid.getValue() + ", ");
					System.out.println();
				}

			}
			
		}
	}
	
	public static void printSignaturePolicyFromFile(File file) {
		SignaturePolicy signaturePolicy = Read.readSignaturePolicyFromFile(file);
		Read.printSignaturePolicy(signaturePolicy);
	}

	public static void printSignaturePolicyURL(URL url) {
		SignaturePolicy signaturePolicy = Read.readSignaturePolicyFromURL(url);
		Read.printSignaturePolicy(signaturePolicy);
	}

	public static void printLPAFromFile(File file) {
		LPA listaPoliticaAssinatura = Read.readLPAFromFile(file);
		Read.printLPA(listaPoliticaAssinatura);
	}
	
	public static void printLPAFromURL(URL url) {
		LPA listaPoliticaAssinatura = Read.readLPAFromURL(url);
		Read.printLPA(listaPoliticaAssinatura);
	}

	public static void printLPA(LPA listaPoliticaAssinatura) {
		System.out.println("===================================================");
		System.out.println("Próxima Atualização.: " + listaPoliticaAssinatura.getNextUpdate().getTime());
		System.out.println("Qtds Políticas......: " + listaPoliticaAssinatura.getPolicyInfos().size());
		System.out.println("===================================================");
		for (br.gov.serpro.asn1.icpb.PolicyInfo policyInfo : listaPoliticaAssinatura.getPolicyInfos()) {
			System.out.println("\tPolítica.............: " + policyInfo.getPolicyName());
			System.out.println("\tAplicação............: " + policyInfo.getFieldOfApplication());
			System.out.println("\tPeríodo de Assinatura: " + policyInfo.getSigningPeriod());
			System.out.print("\tStatus...............: ");
			Time revocationDate = policyInfo.getRevocationDate();
			if (revocationDate != null) {
				System.out.println("POLÍTICA REVOGADA");
				System.out.println("\tData de Revogação....: " + (revocationDate!=null?revocationDate.getTime():"não há data de revogação"));
			} else {
				System.out.println("OK");
			}
			System.out.println("\t===================================================");
		}
	}
	
	public static void printLPAv2FromFile(File file) {
		br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinatura = Read.readLPAv2FromFile(file);
		Read.printLPAv2(listaPoliticaAssinatura);
	}
	
	public static void printLPAv2FromURL(URL url) {
		br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinatura = Read.readLPAv2FromURL(url);
		Read.printLPAv2(listaPoliticaAssinatura);
	}

	public static void printLPAv2(br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinatura) {
		System.out.println("===================================================");
		System.out.println("Próxima Atualização.: " + listaPoliticaAssinatura.getNextUpdate().getDate());
		System.out.println("Qtds Políticas......: " + listaPoliticaAssinatura.getPolicyInfos().size());
		System.out.println("===================================================");
		for (PolicyInfo policyInfo : listaPoliticaAssinatura.getPolicyInfos()) {
			System.out.println("\tPeríodo de Assinatura: " + policyInfo.getSigningPeriod());
			System.out.println("\tOID da Política......: " + policyInfo.getPolicyOID().getValue());
			System.out.println("\tURI da Política......: " + policyInfo.getPolicyURI());
			System.out.println("\tAlgoritmo Hash.......: " + policyInfo.getPolicyDigest().getHashAlgorithm().getObjectId().getId());
			System.out.println("\tHash.................: " + policyInfo.getPolicyDigest().getHashValue().toString());
			System.out.print("\tStatus...............: ");
			GeneralizedTime revocationDate = policyInfo.getRevocationDate();
			if (revocationDate != null) {
				System.out.println("POLÍTICA REVOGADA");
				System.out.println("\tData de Revogação....: " + (revocationDate!=null?revocationDate.getDate():"não há data de revogação"));
			} else {
				System.out.println("OK");
			}
			System.out.println("\t===================================================");
		}
	}

	public static void verifySignaturePolicy(String policyFile, String lpaFile) {
		
	}
	
	public static SignaturePolicy loadSignaturePolicy(String OIDPolicy) {
		if (OIDPolicy == null)
			return null;
		URL url = null;
		SignaturePolicy result = null;
		try {
			url = new URL(Read.URL_LPAv2);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		br.gov.serpro.asn1.icpb.v2.LPA lpav2 = Read.readLPAv2FromURL(url);
		Collection<PolicyInfo> policyInfos = lpav2.getPolicyInfos();
		for (PolicyInfo policyInfo : policyInfos) {
			if (policyInfo.getPolicyOID() != null && policyInfo.getPolicyOID().getValue() != null && policyInfo.getPolicyOID().getValue().trim().equalsIgnoreCase(OIDPolicy)) {
				try {
					url = new URL(policyInfo.getPolicyURI());
				} catch (MalformedURLException e) {
					throw new RuntimeException(e);
				}
				result = Read.readSignaturePolicyFromURL(url);
				Read.printSignaturePolicy(result);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		String[] signaturePolicies = new String[] {"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v1_1.der"};
		signaturePolicies = new String[] {
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v1_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v1_2.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v2_0.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v2_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v2_2.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v1_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v2_0.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v2_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC_v1_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC_v2_0.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC_v2_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT_v1_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT_v2_0.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT_v2_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV_v1_1.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV_v2_0.der",
		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV_v2_1.der"};

		for (String file : signaturePolicies) {
			Read.printSignaturePolicyFromFile(new File(file));
		}
		
		Read.printLPAFromFile(new File("/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/LPA.der"));
		Read.printLPAv2FromFile(new File("/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/LPAv2.der"));
		
//		SignaturePolicy sp = Read.loadSignaturePolicy("2.16.76.1.7.1.5.1.2");

	}

}
