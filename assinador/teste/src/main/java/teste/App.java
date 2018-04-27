package teste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.demoiselle.signer.core.keystore.loader.KeyStoreLoader;
import org.demoiselle.signer.core.keystore.loader.factory.KeyStoreLoaderFactory;
import org.demoiselle.signer.cryptography.DigestAlgorithmEnum;
import org.demoiselle.signer.policy.engine.factory.PolicyFactory;
import org.demoiselle.signer.policy.impl.cades.SignerAlgorithmEnum;
import org.demoiselle.signer.policy.impl.cades.factory.PKCS7Factory;
import org.demoiselle.signer.policy.impl.cades.pkcs7.PKCS7Signer;

/**
 * Hello world!
 *
 */
public class App 
{
    static KeyStoreLoader keyStoreLoader = KeyStoreLoaderFactory.factoryKeyStoreLoader();
    static KeyStore keyStore = keyStoreLoader.getKeyStore();
    static Certificate[] certificates = new Certificate[1];
    static SignerAlgorithmEnum algoritmo = SignerAlgorithmEnum.SHA512withRSA;

    static String certificateAlias = null;
    static PrivateKey chavePrivada = null;
    static Certificate certificate = null;
    static byte[] content = null;
    static MessageDigest md = null;
    static byte[] hash = null;
    static byte[] signature = null;

    public static void main( String[] args ) throws Throwable {
        content = App.loadFile("C:\\Users\\JoséRené\\Documents\\desktop.ini");
        Enumeration<String> aliases = keyStore.aliases();
        while(aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (alias.contains("CAMPANARIO"))
                App.certificateAlias = alias;
        }
        chavePrivada = (PrivateKey)keyStore.getKey(certificateAlias, null);
        certificate = keyStore.getCertificate(certificateAlias);
        certificates[0] = certificate;

        byte[] assinado = App.assinarComHash();
        App.saveFile("C:\\Users\\JoséRené\\Documents\\desktop-hash.p7s", assinado);
        assinado = App.assinarSemHash();
        App.saveFile("C:\\Users\\JoséRené\\Documents\\desktop.p7s", assinado);

    }

    public static byte[] assinarComHash() throws Throwable {
        md = MessageDigest.getInstance(algoritmo.getAlgorithmHash());
        hash = md.digest(content);
        PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
        signer.setCertificates(certificates);
        signer.setPrivateKey(chavePrivada);
        signer.setAlgorithm(algoritmo);
        signer.setSignaturePolicy(PolicyFactory.Policies.AD_RB_CADES_2_2);
        signature = signer.doHashSign(hash);        
        return signature;
    }

    public static byte[] assinarSemHash() throws Throwable {
        PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
        signer.setCertificates(certificates);
        signer.setPrivateKey(chavePrivada);
        signer.setAlgorithm(algoritmo);
        signer.setSignaturePolicy(PolicyFactory.Policies.AD_RB_CADES_2_2);
        signature = signer.doDetachedSign(content);
        return signature;
    }

    public static byte[] loadFile(String path) throws Throwable {
        byte[] result = null;
        File file = new File(path);
        FileInputStream is = new FileInputStream(file);
        result = new byte[(int) file.length()];
        is.read(result);
        is.close();
        return result;
    }

    public static void saveFile(String path, byte[] content) throws Throwable {
        FileOutputStream out = new FileOutputStream(new File(path));
        out.write(content);
        out.close();
    }
}
