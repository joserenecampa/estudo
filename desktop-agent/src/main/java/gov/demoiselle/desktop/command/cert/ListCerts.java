package gov.demoiselle.desktop.command.cert;

import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.sun.security.auth.callback.DialogCallbackHandler;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;
import gov.demoiselle.desktop.command.AbstractCommand;
import gov.demoiselle.web.Execute;

public class ListCerts extends AbstractCommand<ListCertsRequest, ListCertsResponse> {

	public ListCertsResponse doCommand(final ListCertsRequest request) {
		try {
			KeyStoreLoader loader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
			if (request.getPassword() == null || request.getPassword().isEmpty())
				loader.setCallbackHandler(new DialogCallbackHandler());
			else
				loader.setCallbackHandler(new CallbackHandler() {
					public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
						for (Callback callback : callbacks)
							if (callback instanceof PasswordCallback)
								((PasswordCallback) callback).setPassword(request.getPassword().toCharArray());
					}
				});
			KeyStore keyStore = loader.getKeyStore();
			Enumeration<String> aliases = keyStore.aliases();
			ListCertsResponse response = new ListCertsResponse();
			response.setCertificates(new ArrayList<Certificate>());
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
				Certificate certJson = new Certificate();
				certJson.setAlias(alias);
				certJson.setProvider(keyStore.getProvider().getName());
				certJson.setSubject(cert.getSubjectDN().getName());
				certJson.setNotAfter(cert.getNotAfter().toGMTString());
				certJson.setNotBefore(cert.getNotBefore().toGMTString());
				response.getCertificates().add(certJson);
			}
			return response;
		} catch (Throwable error) {
			throw new RuntimeException("Erro ao tentar buscar os certificados digitais");
		}
	}

	public static void main(String[] args) {
		System.out.println((new Execute()).executeCommand(new ListCertsRequest()));
	}

}
