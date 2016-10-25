package gov.demoiselle.desktop.command.signer;

import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.google.gson.Gson;
import com.sun.security.auth.callback.DialogCallbackHandler;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;
import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.bc.policies.ADRBCMS_2_1;
import gov.demoiselle.desktop.command.ParameterCommand;
import gov.demoiselle.desktop.command.json.CertificateJson;
import gov.demoiselle.web.ExecuteCommand;

public class Signer extends ParameterCommand<SignerJson>{

	@Override
	public String doCommand(SignerJson signerRequest) {
		this.validateRequest(signerRequest);
		final SignerRequestJson data = signerRequest.getParam();
		KeyStoreLoader loader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
		if (data.getPassword() == null || data.getPassword().isEmpty())
			loader.setCallbackHandler(new DialogCallbackHandler());
		else
			loader.setCallbackHandler(new CallbackHandler() {
				public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
					for (Callback callback : callbacks)
						if (callback instanceof PasswordCallback)
							((PasswordCallback)callback).setPassword(data.getPassword().toCharArray());
				}
			});
		KeyStore keyStore = loader.getKeyStore();
		try {
			X509Certificate cert = (X509Certificate)keyStore.getCertificate(data.getAlias());
	        PrivateKey privateKey = (PrivateKey) keyStore.getKey(data.getAlias(), null);        
			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
	        signer.setCertificates(keyStore.getCertificateChain(data.getAlias()));
	        signer.setPrivateKey(privateKey);
	        signer.setSignaturePolicy(new ADRBCMS_2_1());
	        signer.setAttached(false);
	        byte[] signed = signer.signer(this.getContent(signerRequest.getParam()));
	        String encripted = Base64.getEncoder().encodeToString(signed);
	        SignerResponseJson result = new SignerResponseJson();
	        result.setSigned(encripted);
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
			throw new RuntimeException("generic encript error. " + error.getMessage(), error);
		}
	}

	private void validateRequest(SignerJson signerRequest) {
	}

	private byte[] getContent(SignerRequestJson request) {
		byte[] result = null;
		if (request.getFormat().equalsIgnoreCase("text")) {
			result = request.getContent().getBytes();
		} else if (request.getFormat().equalsIgnoreCase("base64")) {
			result = Base64.getDecoder().decode(request.getContent().getBytes());
		} else if (request.getFormat().equalsIgnoreCase("hexa")) {
		    int len = request.getContent().length();
		    byte[] data = new byte[len / 2];
		    for (int i = 0; i < len; i += 2) {
		        data[i / 2] = (byte) ((Character.digit(request.getContent().charAt(i), 16) << 4)
		                             + Character.digit(request.getContent().charAt(i+1), 16));
		    }
		    result = data;
		}
		if (request.getCompacted()) {
			// TODO: Descompactar
		}
		return result;
	}
	
	public static void main(String[] args) {
		SignerJson j = new SignerJson();
		j.setCommand("signer");
		SignerRequestJson param = new SignerRequestJson();
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
