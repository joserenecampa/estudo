package br.gov.serpro.asn1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;

import br.gov.serpro.asn1.etsi.SignaturePolicy;
import br.gov.serpro.asn1.icpb.LPA;
import br.gov.serpro.asn1.icpb.Time;
import br.gov.serpro.asn1.icpb.v2.PolicyInfo;

public class Read {

	public static DERObject readDERFromFile(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException error) {
			throw new RuntimeException(error);
		}
		ASN1InputStream asn1is = new ASN1InputStream(fis);
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
	
	public static SignaturePolicy readSignaturePolicyFromFile(File file) {
		SignaturePolicy signaturePolicy = new SignaturePolicy();
		DERObject derObject = Read.readDERFromFile(file);
		signaturePolicy.parse(derObject);
		return signaturePolicy;
	}
	
	public static LPA readLPAFromFile(File file) {
		LPA listaPoliticaAssinatura = new LPA();
		DERObject derObject = Read.readDERFromFile(file);
		listaPoliticaAssinatura.parse(derObject);
		return listaPoliticaAssinatura;
	}
	
	public static br.gov.serpro.asn1.icpb.v2.LPA readLPAv2FromFile(File file) {
		br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinaturaV2 = new br.gov.serpro.asn1.icpb.v2.LPA();
		DERObject derObject = Read.readDERFromFile(file);
		listaPoliticaAssinaturaV2.parse(derObject);
		return listaPoliticaAssinaturaV2;
	}

	public static void printSignaturePolicyFromFile(File file) {
		SignaturePolicy signaturePolicy = Read.readSignaturePolicyFromFile(file);
		System.out.println("===================================================");
		System.out.println("Algoritmo Hash da Política.....: " + signaturePolicy.getSignPolicyHashAlg().getAlgorithm().getValue());
		System.out.println("Hash da Política...............: " + signaturePolicy.getSignPolicyHash().getValue());
		System.out.println("OID da Política................: " + signaturePolicy.getSignPolicyInfo().getSignPolicyIdentifier().getValue());
		System.out.println("Data Lancamento da Política....: " + signaturePolicy.getSignPolicyInfo().getDateOfIssue().getDate());
		System.out.println("Emissor da Política............: " + signaturePolicy.getSignPolicyInfo().getPolicyIssuerName());
		System.out.println("Campo de aplicação da Política.: " + signaturePolicy.getSignPolicyInfo().getFieldOfApplication().getValue());
		System.out.println("Politica válida entre..........: " + signaturePolicy.getSignPolicyInfo().getSignatureValidationPolicy().getSigningPeriod());
	}

	public static void printLPAFromFile(File file) {
		LPA listaPoliticaAssinatura = Read.readLPAFromFile(file);
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
	
	public static void verifySignaturePolicy(String policyFile, String lpaFile) {
		
	}

	public static void printLPAv2FromFile(File file) {
		br.gov.serpro.asn1.icpb.v2.LPA listaPoliticaAssinatura = Read.readLPAv2FromFile(file);
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

	public static void main(String[] args) {
		String[] signaturePolicies = new String[] {"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v1_1.der"};
//		signaturePolicies = new String[] {
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v1_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v1_2.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v2_0.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v2_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RA_v2_2.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v1_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v2_0.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RB_v2_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC_v1_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC_v2_0.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RC_v2_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT_v1_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT_v2_0.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RT_v2_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV_v1_1.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV_v2_0.der",
//		"/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/PA_AD_RV_v2_1.der"};

		for (String file : signaturePolicies) {
			Read.printSignaturePolicyFromFile(new File(file));
		}
		
//		Read.printLPAFromFile(new File("/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/LPA.der"));
//		Read.printLPAv2FromFile(new File("/home/09275643784/Documentos/ICP-Brasil/artefatos_assinatura/LPAv2.der"));

	}

}
