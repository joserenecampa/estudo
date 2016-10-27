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

import com.sun.security.auth.callback.DialogCallbackHandler;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;
import br.gov.frameworkdemoiselle.certificate.signer.factory.PKCS7Factory;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.PKCS7Signer;
import br.gov.frameworkdemoiselle.policy.engine.factory.PolicyFactory.Policies;
import gov.demoiselle.desktop.command.AbstractCommand;
import gov.demoiselle.desktop.command.cert.Certificate;
import gov.demoiselle.web.Execute;

public class Signer extends AbstractCommand<SignerRequest, SignerResponse>{

	@Override
	public SignerResponse doCommand(final SignerRequest request) {
		this.validateRequest(request);
		KeyStoreLoader loader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
		if (request.getPassword() == null || request.getPassword().isEmpty())
			loader.setCallbackHandler(new DialogCallbackHandler());
		else
			loader.setCallbackHandler(new CallbackHandler() {
				public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
					for (Callback callback : callbacks)
						if (callback instanceof PasswordCallback)
							((PasswordCallback)callback).setPassword(request.getPassword().toCharArray());
				}
			});
		KeyStore keyStore = loader.getKeyStore();
		try {
			X509Certificate cert = (X509Certificate)keyStore.getCertificate(request.getAlias());
	        PrivateKey privateKey = (PrivateKey) keyStore.getKey(request.getAlias(), null);        
			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
	        signer.setCertificates(keyStore.getCertificateChain(request.getAlias()));
	        signer.setPrivateKey(privateKey);
	        Policies policie = null;
	        try {
	        	policie = Policies.valueOf(request.getSignaturePolicy());
	        } catch (Throwable error) {
	        	policie = Policies.AD_RB_CADES_2_1;
	        }
	        signer.setSignaturePolicy(policie);
	        signer.setAttached(false);
	        byte[] signed = signer.doSign(this.getContent(request));
	        String encripted = Base64.getEncoder().encodeToString(signed);
	        SignerResponse result = new SignerResponse();
	        result.setRequestId(request.getId());
	        result.setSigned(encripted);
	        Certificate by = new Certificate();
	        by.setAlias(request.getAlias());
	        by.setProvider(keyStore.getProvider().getName());
	        by.setSubject(cert.getSubjectDN().getName());
	        by.setNotAfter(cert.getNotAfter().toGMTString());
	        by.setNotBefore(cert.getNotBefore().toGMTString());
			result.setBy(by);
			result.setPublicKey(Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()));
			return result;
		} catch (Throwable error) {
			throw new RuntimeException(error.getMessage(), error);
		}
	}

	private void validateRequest(SignerRequest request) {
	}

	private byte[] getContent(SignerRequest request) {
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
		}
		return result;
	}
	
	public static void main(String[] args) {
		SignerRequest request = new SignerRequest();
		request.setId(1);
		request.setAlias("(1288991) JOSE RENE NERY CAILLERET CAMPANARIO");
		request.setCompacted(false);
		request.setProvider("SunPKCS11-TokenOuSmartCard_30");
		request.setContent("HELLO WORLD!");
		System.out.println(request.toJson());
		System.out.println((new Execute()).executeCommand(request));
	}

}