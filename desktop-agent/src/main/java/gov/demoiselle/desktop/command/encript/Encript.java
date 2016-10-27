package gov.demoiselle.desktop.command.encript;

import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.sun.security.auth.callback.DialogCallbackHandler;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;
import gov.demoiselle.desktop.command.AbstractCommand;
import gov.demoiselle.desktop.command.cert.Certificate;
import gov.demoiselle.web.Execute;

public class Encript extends AbstractCommand<EncriptRequest, EncriptResponse> {

	public EncriptResponse doCommand(final EncriptRequest data) {
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
	        final Cipher cipherEnc = Cipher.getInstance(data.getAlgorithm());
	        PrivateKey privateKey = (PrivateKey) keyStore.getKey(data.getAlias(), null);        
	        cipherEnc.init(Cipher.ENCRYPT_MODE, privateKey);
	        byte[] cript = cipherEnc.doFinal(this.getBytes(data));
	        String encripted = Base64.getEncoder().encodeToString(cript);
	        EncriptResponse result = new EncriptResponse();
	        result.setRequestId(data.getId());
	        result.setEncripted(encripted);
	        Certificate by = new Certificate();
	        by.setAlias(data.getAlias());
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
	
	private byte[] getBytes(EncriptRequest data) {
		return data.getContent().getBytes();
	}

	public static void main(String[] args) {
		EncriptRequest request = new EncriptRequest();
		request.setAlias("(1288991) JOSE RENE NERY CAILLERET CAMPANARIO");
		request.setCompacted(false);
		request.setProvider("SunPKCS11-TokenOuSmartCard_30");
		request.setContent("HELLO WORLD!");
		System.out.println((new Execute()).executeCommand(request));
		
	}
	
}
