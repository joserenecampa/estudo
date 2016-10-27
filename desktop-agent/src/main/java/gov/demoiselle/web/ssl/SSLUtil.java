//package gov.demoiselle.web.ssl;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.KeyManagerFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.TrustManagerFactory;
//import javax.net.ssl.X509TrustManager;
//
//public class SSLUtil implements X509TrustManager {
//
//	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//	}
//
//	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//	}
//
//	public X509Certificate[] getAcceptedIssuers() {
//		return null;
//	}
//
//	public SSLContext createSslContext() throws IOException {
//		URL urlToKeyStore = getClass().getResource("localhost.jks");
//		String keyStoreName = urlToKeyStore.getPath();
//		String keyStoreType = "JKS";
//		String keyStorePassword = "changeit";
//		KeyStore keyStore = loadKeyStore(keyStoreName, keyStoreType, keyStorePassword);
//
//		KeyManager[] keyManagers = buildKeyManagers(keyStore, keyStorePassword.toCharArray());
//		TrustManager[] trustManagers = buildTrustManagers(keyStore);
//
//		SSLContext sslContext;
//		try {
//			sslContext = SSLContext.getInstance("TLS");
//			sslContext.init(keyManagers, trustManagers, null);
//		} catch (NoSuchAlgorithmException | KeyManagementException exc) {
//			throw new IOException("Unable to create and initialise the SSLContext", exc);
//		}
//
//		return sslContext;
//	}
//
//	private KeyStore loadKeyStore(final String location, String type, String storePassword) throws IOException {
//		String url = location;
//		if (url.indexOf(':') == -1) {
//			url = "file:" + location;
//		}
//
//		final InputStream stream = new URL(url).openStream();
//		try {
//			KeyStore loadedKeystore = KeyStore.getInstance(type);
//			loadedKeystore.load(stream, storePassword.toCharArray());
//			return loadedKeystore;
//		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException exc) {
//			throw new IOException(String.format("Unable to load KeyStore %s", location), exc);
//		} finally {
//			stream.close();
//		}
//	}
//
//	private TrustManager[] buildTrustManagers(final KeyStore trustStore) throws IOException {
//		TrustManager[] trustManagers = null;
//		if (trustStore == null) {
//			try {
//				TrustManagerFactory trustManagerFactory = TrustManagerFactory
//						.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//				trustManagerFactory.init(trustStore);
//				trustManagers = trustManagerFactory.getTrustManagers();
//			} catch (NoSuchAlgorithmException | KeyStoreException exc) {
//				throw new IOException("Unable to initialise TrustManager[]", exc);
//			}
//		} else {
//			trustManagers = new TrustManager[] { this };
//		}
//		return trustManagers;
//	}
//
//	private KeyManager[] buildKeyManagers(final KeyStore keyStore, char[] storePassword) throws IOException {
//		KeyManager[] keyManagers;
//		try {
//			KeyManagerFactory keyManagerFactory = KeyManagerFactory
//					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			keyManagerFactory.init(keyStore, storePassword);
//			keyManagers = keyManagerFactory.getKeyManagers();
//		} catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException exc) {
//			throw new IOException("Unable to initialise KeyManager[]", exc);
//		}
//		return keyManagers;
//	}
//
//}