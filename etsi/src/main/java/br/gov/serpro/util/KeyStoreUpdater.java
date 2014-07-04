package br.gov.serpro.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.security.KeyStore;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class KeyStoreUpdater {

    private static String STRING_URL = "http://acraiz.icpbrasil.gov.br/credenciadas/CertificadosAC-ICP-Brasil/ACcompactado.zip";
    private static String HASH_URL = "http://acraiz.icpbrasil.gov.br/credenciadas/CertificadosAC-ICP-Brasil/hashsha512.txt";
    public static final String PROVIDER = "SUN";
    public static final String TYPE = "JKS";
    private static final int BYTE_BUFFER = 512;
    private static final KeyStoreUpdater instance = new KeyStoreUpdater();

    private KeyStoreUpdater() {
    }

    public static KeyStoreUpdater getInstance() {
        return KeyStoreUpdater.instance;
    }

    public Collection<X509Certificate> getFromKeyStore(File keyStoreFile, String keyStorePass) throws RuntimeException {
        return this.getFromKeyStore(keyStoreFile, keyStorePass, false);
    }

    public Collection<X509Certificate> getFromKeyStore(File keyStoreFile, String keyStorePass, boolean valida) throws RuntimeException {
        Map<String, X509Certificate> result = this.getFromKeyStoreWithAlias(keyStoreFile, keyStorePass, valida);
        return result.values();
    }

    public Map<String, X509Certificate> getFromKeyStoreWithAlias(File keyStoreFile, String keyStorePass) throws RuntimeException {
        return this.getFromKeyStoreWithAlias(keyStoreFile, keyStorePass, false);
    }

    public Map<String, X509Certificate> getFromKeyStoreWithAlias(File keyStoreFile, String keyStorePass, boolean valida) throws RuntimeException {
        Map<String, X509Certificate> result = new LinkedHashMap<String, X509Certificate>();
        if (keyStoreFile == null) {
            throw new RuntimeException("É necessário informar um KeyStore");
        }
        KeyStore ks = null;

        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException error) {
            throw new RuntimeException("Erro ao instanciar o KeyStore", error);
        }

        try {
            ks.load(new FileInputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Não há o algoritmo necessário", error);
        } catch (CertificateException error) {
            throw new RuntimeException("Não foi possível carregar algum certificado interno do keystore, verifique a integridade das informações contidas no keystore", error);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("O arquivo informado não é um arquivo ou não é possível ler o arquivo", error);
        } catch (IOException error) {
            throw new RuntimeException("O formato do arquivo não é conhecido ou a senha para abrí-lo é incorreta ou inexistente", error);
        }

        try {
            for (Enumeration<String> e = ks.aliases(); e.hasMoreElements();) {
                String alias = e.nextElement();
                Certificate certificate = ks.getCertificate(alias);
                X509Certificate cert = (X509Certificate) certificate;
                if (cert != null) {
                    if (valida) {
                        if (isValid(cert)) {
                            result.put(alias, cert);
                        }
                    } else {
                        result.put(alias, cert);
                    }
                }
            }
        } catch (KeyStoreException error) {
            throw new RuntimeException("Não foi possível ler o keystore", error);
        }
        return result;
    }

    public Collection<X509Certificate> getFromZipFile(File zipFile) throws RuntimeException {
        return this.getFromZipFile(zipFile, false);
    }

    public Collection<X509Certificate> getFromZipFile(File zipFile, boolean valida) throws RuntimeException {
        Map<String, X509Certificate> result = this.getFromZipFileWithAlias(zipFile, valida);
        return result.values();
    }

    public Map<String, X509Certificate> getFromZipFileWithAlias(File zipFile) throws RuntimeException {
        return this.getFromZipFileWithAlias(zipFile, false);
    }

    public Map<String, X509Certificate> getFromZipFileWithAlias(File zipFile, boolean valida) throws RuntimeException {
        InputStream is = null;
        try {
            is = new FileInputStream(zipFile);
            return this.getFromZipWithAlias(is, valida);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("Arquivo não encontrado", error);
        }
    }

    public Map<String, X509Certificate> getFromZipWithAlias(InputStream zip, boolean valida) throws RuntimeException {
        Map<String, X509Certificate> result = new LinkedHashMap<String, X509Certificate>();
        InputStream in = new BufferedInputStream(zip);
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry arquivoInterno = null;
        try {
            while ((arquivoInterno = zin.getNextEntry()) != null) {
                if (!arquivoInterno.isDirectory()) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] b = new byte[BYTE_BUFFER];
                    int len = 0;
                    while ((len = zin.read(b)) != -1) {
                        out.write(b, 0, len);
                    }
                    ByteArrayInputStream is = new ByteArrayInputStream(out.toByteArray());
                    out.close();
                    X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(is);
                    is.close();
                    if (valida) {
                        if (isValid(certificate)) {
                            result.put(arquivoInterno.getName(), certificate);
                        }
                    } else {
                        result.put(arquivoInterno.getName(), certificate);
                    }
                }
            }
        } catch (CertificateException error) {
            throw new RuntimeException("Certificado inválido", error);
        } catch (IOException error) {
            throw new RuntimeException("Erro ao tentar abrir o stream", error);
        }
        return result;
    }

    public Map<String, X509Certificate> getACsFromICPURL(boolean valida) throws RuntimeException {
        return this.getFromZipWithAlias(this.getInputStreamACsFileZipFromICPBrasil(), valida);
    }

    public Collection<X509Certificate> getFromDirectory(File directory) throws RuntimeException {
        return this.getFromDirectory(directory, false);
    }

    public Map<String, X509Certificate> getFromDirectoryWithFileName(File directory) throws RuntimeException {
        return this.getFromDirectoryWithFileName(directory, false);
    }

    public Collection<X509Certificate> getFromDirectory(File directory, boolean valida) throws RuntimeException {
        Map<String, X509Certificate> result = this.getFromDirectoryWithFileName(directory, valida);
        return result.values();
    }

    public X509Certificate getCertificateFromFile(File certificateFile, boolean valida) throws RuntimeException {
        InputStream is = null;
        try {
            is = new FileInputStream(certificateFile);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("Arquivo não encontrado", error);
        }
        X509Certificate certificate = null;
        try {
            certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(is);
        } catch (CertificateException error) {
            throw new RuntimeException("Erro ao carregar o Certificado do arquivo " + certificateFile.getName(), error);
        }
        if (valida) {
            if (isValid(certificate)) {
                return certificate;
            }
        } else {
            return certificate;
        }
        return null;
    }

    public Map<String, X509Certificate> getFromDirectoryWithFileName(File directory, boolean valida) throws RuntimeException {
        Map<String, X509Certificate> result = new LinkedHashMap<String, X509Certificate>();
        if (directory == null) {
            throw new RuntimeException("É necessário informar um diretório");
        }
        if (!directory.isDirectory()) {
            throw new RuntimeException("Não é um diretório válido");
        }
        for (File file : directory.listFiles()) {
            X509Certificate certificate = this.getCertificateFromFile(file, valida);
            if (certificate != null) {
                result.put(file.getName(), certificate);
            }
        }
        return result;
    }

    public boolean isValid(X509Certificate cert) throws RuntimeException {
        boolean result = false;
        try {
            cert.checkValidity(new Date());
            result = true;
        } catch (CertificateExpiredException error) {
            result = false;
        } catch (CertificateNotYetValidException error) {
            result = false;
        }
        return result;
    }

    public String makeAliasToCertificate(X509Certificate certificate) throws RuntimeException {
        return getHexDigest(certificate.getSubjectDN().getName());
    }

    private String getHexDigest(String phrase) {
        return this.getHexDigest(phrase.getBytes());
    }

    private String getHexDigest(byte[] bytes) {
        return this.getHexDigest(bytes, "SHA-1");
    }

    private String getHexDigest512(byte[] bytes) {
        return this.getHexDigest(bytes, "SHA-512");
    }

    private String getHexDigest(byte[] bytes, String algorithm) {
        return this.convertToHex(this.getDigest(bytes, algorithm));
    }

    private byte[] getDigest(byte[] bytes, String algorithm) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Algorithm " + algorithm + " not found.", error);
        }
        digest.update(bytes);
        return digest.digest();
    }

    private String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();

    }

    public void createKeyStoreFromCertificates(Collection<X509Certificate> certificates, File keyStoreFile, String keyStorePass) throws RuntimeException {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, keyStorePass == null ? null : keyStorePass.toCharArray());
            ks.store(new FileOutputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
            ks.load(new FileInputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
            for (X509Certificate x509 : certificates) {
                String alias = makeAliasToCertificate(x509);
                try {
                    ks.setEntry(alias, new TrustedCertificateEntry(x509), null);
                } catch (KeyStoreException e) {
                }
            }
            ks.store(new FileOutputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
        } catch (KeyStoreException error) {
            throw new RuntimeException("O provedor não suporta este tipo de keystore", error);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Não há o algoritmo necessário", error);
        } catch (CertificateException error) {
            throw new RuntimeException("Não foi possível carregar algum certificado interno do keystore, verifique a integridade das informações contidas no keystore", error);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("O arquivo informado não é um arquivo ou não é possível ler o arquivo", error);
        } catch (IOException error) {
            throw new RuntimeException("O formato do arquivo não é conhecido ou a senha para abrí-lo é incorreta ou inexistente", error);
        }
    }

    public void createKeyStoreFromCertificates(Map<String, X509Certificate> certificates, File keyStoreFile, String keyStorePass) throws RuntimeException {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, keyStorePass == null ? null : keyStorePass.toCharArray());
            ks.store(new FileOutputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
            ks.load(new FileInputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
            for (String alias : certificates.keySet()) {
                ks.setEntry(alias, new TrustedCertificateEntry(certificates.get(alias)), null);
            }
            ks.store(new FileOutputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
        } catch (KeyStoreException error) {
            throw new RuntimeException("O provedor não suporta este tipo de keystore", error);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Não há o algoritmo necessário", error);
        } catch (CertificateException error) {
            throw new RuntimeException("Não foi possível carregar algum certificado interno do keystore, verifique a integridade das informações contidas no keystore", error);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("O arquivo informado não é um arquivo ou não é possível ler o arquivo", error);
        } catch (IOException error) {
            throw new RuntimeException("O formato do arquivo não é conhecido ou a senha para abrí-lo é incorreta ou inexistente", error);
        }
    }

    public KeyStore getKeyStore(File keyStoreFile, String keyStorePass) {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStoreUpdater.TYPE, KeyStoreUpdater.PROVIDER);
            ks.load(new FileInputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
            return ks;
        } catch (NoSuchProviderException error) {
            throw new RuntimeException("Provider inválido", error);
        } catch (KeyStoreException error) {
            throw new RuntimeException("O provedor não suporta este tipo de keystore", error);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Não há o algoritmo necessário", error);
        } catch (CertificateException error) {
            throw new RuntimeException("Não foi possível carregar algum certificado interno do keystore, verifique a integridade das informações contidas no keystore", error);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("O arquivo informado não é um arquivo ou não é possível ler o arquivo", error);
        } catch (IOException error) {
            throw new RuntimeException("O formato do arquivo não é conhecido ou a senha para abrí-lo é incorreta ou inexistente", error);
        }
    }

    public void addCertificatesToKeyStore(Map<String, X509Certificate> certificates, File keyStoreFile, String keyStorePass) throws RuntimeException {
        try {
            KeyStore ks = this.getKeyStore(keyStoreFile, keyStorePass);
            for (String alias : certificates.keySet()) {
                ks.setEntry(alias, new TrustedCertificateEntry(certificates.get(alias)), null);
            }
            ks.store(new FileOutputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
        } catch (KeyStoreException error) {
            throw new RuntimeException("O provedor não suporta este tipo de keystore", error);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Não há o algoritmo necessário", error);
        } catch (CertificateException error) {
            throw new RuntimeException("Não foi possível carregar algum certificado interno do keystore, verifique a integridade das informações contidas no keystore", error);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("O arquivo informado não é um arquivo ou não é possível ler o arquivo", error);
        } catch (IOException error) {
            throw new RuntimeException("O formato do arquivo não é conhecido ou a senha para abrí-lo é incorreta ou inexistente", error);
        }
    }

    public void addCertificatesToKeyStore(Collection<X509Certificate> certificates, File keyStoreFile, String keyStorePass) throws RuntimeException {
        try {
            KeyStore ks = this.getKeyStore(keyStoreFile, keyStorePass);
            ks.load(new FileInputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
            for (X509Certificate x509 : certificates) {
                String alias = makeAliasToCertificate(x509);
                ks.setEntry(alias, new TrustedCertificateEntry(x509), null);
            }
            ks.store(new FileOutputStream(keyStoreFile), keyStorePass == null ? null : keyStorePass.toCharArray());
        } catch (KeyStoreException error) {
            throw new RuntimeException("O provedor não suporta este tipo de keystore", error);
        } catch (NoSuchAlgorithmException error) {
            throw new RuntimeException("Não há o algoritmo necessário", error);
        } catch (CertificateException error) {
            throw new RuntimeException("Não foi possível carregar algum certificado interno do keystore, verifique a integridade das informações contidas no keystore", error);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("O arquivo informado não é um arquivo ou não é possível ler o arquivo", error);
        } catch (IOException error) {
            throw new RuntimeException("O formato do arquivo não é conhecido ou a senha para abrí-lo é incorreta ou inexistente", error);
        }
    }

    public void addCertificateToKeyStore(X509Certificate certificate, File keyStoreFile, String keyStorePass) throws RuntimeException {
        Collection<X509Certificate> certificates = new ArrayList<X509Certificate>();
        certificates.add(certificate);
        addCertificatesToKeyStore(certificates, keyStoreFile, keyStorePass);
    }

    public void saveCertificateInFile(File file, X509Certificate certificate) throws RuntimeException {
        try {
            byte[] bytes = certificate.getEncoded();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (CertificateEncodingException error) {
            throw new RuntimeException("Certificado em formato inválido", error);
        } catch (FileNotFoundException error) {
            throw new RuntimeException("O arquivo informado não é um arquivo ou não é possível ler o arquivo", error);
        } catch (IOException error) {
            throw new RuntimeException("O formato do arquivo não é conhecido ou a senha para abrí-lo é incorreta ou inexistente", error);
        }
    }

    public byte[] getACsFileZipFromICPBrasil() throws RuntimeException {
        byte[] file = this.getFileFromURL(STRING_URL);
        String hash = new String(this.getFileFromURL(HASH_URL)).split(" ")[0];
        String fileHash = this.getHexDigest512(file);
        if (hash.equals(fileHash)) {
            return file;
        } else {
            throw new RuntimeException("O arquivo está corrompido.\nHash Original: " + hash + "\nHash baixado: " + fileHash);
        }
    }

    public InputStream getInputStreamACsFileZipFromICPBrasil() throws RuntimeException {
        return this.getInputStreamFromURL(STRING_URL);
    }

    public InputStream getInputStreamFromURL(String stringURL) throws RuntimeException {
        try {
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            return connection.getInputStream();
        } catch (MalformedURLException error) {
            throw new RuntimeException("URL mal formada", error);
        } catch (UnknownServiceException error) {
            throw new RuntimeException("Serviço da URL desconhecido", error);
        } catch (IOException error) {
            throw new RuntimeException("Algum erro de I/O ocorreu", error);
        }
    }

    public byte[] getFileFromURL(String stringURL) throws RuntimeException {
        byte[] result = null;
        try {
            BufferedInputStream in = new BufferedInputStream(this.getInputStreamFromURL(stringURL));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[BYTE_BUFFER];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            in.close();
            result = out.toByteArray();
            out.close();
        } catch (IOException error) {
            throw new RuntimeException("Algum erro de I/O ocorreu", error);
        }
        return result;
    }

    public void setICPURL(String text) {
        KeyStoreUpdater.STRING_URL = text;
    }

    public String getICPURL() {
        return KeyStoreUpdater.STRING_URL;
    }
    
}