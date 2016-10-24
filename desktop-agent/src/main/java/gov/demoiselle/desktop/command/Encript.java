package gov.demoiselle.desktop.command;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.crypto.Cipher;

import com.google.gson.Gson;
import com.sun.security.auth.callback.DialogCallbackHandler;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;
import gov.demoiselle.desktop.command.json.CertificateJson;
import gov.demoiselle.desktop.command.json.EncriptJson;
import gov.demoiselle.desktop.command.json.EncriptRequestJson;
import gov.demoiselle.desktop.command.json.EncriptResponseJson;
import gov.demoiselle.web.ExecuteCommand;

public class Encript extends ParameterCommand<EncriptJson> {

	public String doCommand(EncriptJson dataCommandJson) {
		EncriptRequestJson data = dataCommandJson.getParam();
		KeyStoreLoader loader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
		loader.setCallbackHandler(new DialogCallbackHandler());
		KeyStore keyStore = loader.getKeyStore();
		try {
			X509Certificate cert = (X509Certificate)keyStore.getCertificate(data.getAlias());
	        final Cipher cipherEnc = Cipher.getInstance(data.getAlgorithm());
	        PrivateKey privateKey = (PrivateKey) keyStore.getKey(data.getAlias(), null);        
	        cipherEnc.init(Cipher.ENCRYPT_MODE, privateKey);
	        byte[] cript = cipherEnc.doFinal(this.getBytes(data));
	        String encripted = Base64.getEncoder().encodeToString(cript);
	        EncriptResponseJson result = new EncriptResponseJson();
	        result.setEncripted(encripted);
	        CertificateJson by = new CertificateJson();
	        by.setAlias(data.getAlias());
	        by.setProvider(keyStore.getProvider().getName());
	        by.setSubject(cert.getSubjectDN().getName());
	        by.setNotAfter(cert.getNotAfter().toGMTString());
	        by.setNotBefore(cert.getNotBefore().toGMTString());
			result.setBy(by);
			result.setPublicKey(Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()));
			Gson gson = new Gson();
			return gson.toJson(result);
		} catch (Throwable error) {
			throw new RuntimeException("generic encript error", error);
		}
	}
	
	private byte[] getBytes(EncriptRequestJson data) {
		return data.getContent().getBytes();
	}

	public static void main(String[] args) {
		EncriptJson j = new EncriptJson();
		j.setCommand("encript");
		EncriptRequestJson param = new EncriptRequestJson();
		param.setAlgorithm("RSA");
		param.setAlias("(1288991) JOSE RENE NERY CAILLERET CAMPANARIO");
		param.setCompacted(false);
		param.setFormat("text");
		param.setType("raw");
		param.setProvider("SunPKCS11-TokenOuSmartCard_30");
		param.setContent("HELLO WORLD!");
		j.setParam(param );
		Gson gson = new Gson();
		String json = gson.toJson(j);
		System.out.println(json);
		ExecuteCommand exec = new ExecuteCommand();
		String result = exec.executeCommand(json);
		System.out.println(result);
		
	}
	
}
