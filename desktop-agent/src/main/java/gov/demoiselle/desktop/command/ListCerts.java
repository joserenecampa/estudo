package gov.demoiselle.desktop.command;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;

import com.google.gson.Gson;
import com.sun.security.auth.callback.DialogCallbackHandler;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.factory.KeyStoreLoaderFactory;
import gov.demoiselle.desktop.command.json.CertificateJson;
import gov.demoiselle.desktop.command.json.CertificatesJson;

public class ListCerts extends AbstractCommand {
	
	public String doCommand(String params) {
		try {
			KeyStoreLoader loader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
			loader.setCallbackHandler(new DialogCallbackHandler());
			KeyStore keyStore = loader.getKeyStore();
			Enumeration<String> aliases = keyStore.aliases();
			CertificatesJson certs = new CertificatesJson();
			certs.setCertificates(new ArrayList<CertificateJson>());
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				X509Certificate cert = (X509Certificate)keyStore.getCertificate(alias);
				CertificateJson certJson = new CertificateJson();
				certJson.setAlias(alias);
				certJson.setProvider(keyStore.getProvider().getName());
				certJson.setSubject(cert.getSubjectDN().getName());
				certJson.setNotAfter(cert.getNotAfter().toGMTString());
				certJson.setNotBefore(cert.getNotBefore().toGMTString());
				certs.getCertificates().add(certJson);
			}
			Gson gson = new Gson();
			String jsonResponse = gson.toJson(certs);
			return jsonResponse;
		} catch (Throwable error) {
			return "{ \"erro\": \"Erro ao tentar buscar os certificados digitais\"}";
		}
	}

}
