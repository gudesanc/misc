package org.gds.misc.encryption;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.*;
import java.util.Base64;

/**
 * Hello world!
 */
public class EncryptTextWithKeyAndCertificate {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        String daCriptare = "Ciao gino";
        if(args==null || args.length<1){
            System.out.println("Inserire la password come argomento del mail...");
            System.exit(1);
        }
        char[] pwd = args[0].toCharArray();
        String criptato = cripta(daCriptare);
        System.out.println(criptato);
        String decode = decripta(criptato, pwd);
        System.out.println(decode);

    }

    private static PublicKey loadPublicKeyFromCertificate() throws Exception{
        InputStream is = EncryptTextWithKeyAndCertificate.class.getResourceAsStream("/seicon.crt");
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        X509Certificate crt = (X509Certificate) cf.generateCertificate(is);
        return crt.getPublicKey();
    }

    public static String decripta(String toDecode,char[] pwd) throws Exception {
        PrivateKey privateKey = loadPrivateKeyFromPKCS12(pwd);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(toDecode));
        return new String(bytes);
    }
    private static String cripta(String toEncode) throws Exception{
        PublicKey publicKey = loadPublicKeyFromCertificate();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytes = cipher.doFinal(toEncode.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(bytes));
    }

    private static PublicKey loadPublicKey() throws Exception{
        // reading from resource folder
        byte[] publicKeyBytes = readAllBytes(
                EncryptTextWithKeyAndCertificate.class.getResourceAsStream("/seicon.pub"));

        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = publicKeyFactory.generatePublic(publicKeySpec);
        return publicKey;

    }

    private static PrivateKey loadProtectedPrivateKey(char[] pwd) throws Exception {
        // reading from resource folder
        byte[] privateKeyBytes = readAllBytes(EncryptTextWithKeyAndCertificate.class.getResourceAsStream("/seicon.key.pkcs8"));
        EncryptedPrivateKeyInfo encryptPKInfo = new EncryptedPrivateKeyInfo(privateKeyBytes);
        Cipher cipher = Cipher.getInstance(encryptPKInfo.getAlgName());
        PBEKeySpec pbeKeySpec = new PBEKeySpec(pwd);
        SecretKeyFactory secFac = SecretKeyFactory.getInstance(encryptPKInfo.getAlgName());
        Key pbeKey = secFac.generateSecret(pbeKeySpec);
        AlgorithmParameters algParams = encryptPKInfo.getAlgParameters();
        cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);
        KeySpec pkcs8KeySpec = encryptPKInfo.getKeySpec(cipher);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return  kf.generatePrivate(pkcs8KeySpec);
    }
    private static PrivateKey loadUnProtectedPrivateKey() throws Exception {
        // reading from resource folder
        byte[] privateKeyBytes = readAllBytes(EncryptTextWithKeyAndCertificate.class.getResourceAsStream("/seicon.key.pem"));
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
//        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = privateKeyFactory.generatePrivate(privateKeySpec);
//        return privateKey;
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
        catch (InvalidKeySpecException excep1) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                return  keyFactory.generatePrivate(keySpec);
            } catch (InvalidKeySpecException excep2) {
                KeyFactory keyFactory = KeyFactory.getInstance("EC");
                return  keyFactory.generatePrivate(keySpec);
            } // inner catch
        }
    }
    private static PrivateKey loadPrivateKeyFromPKCS12(char[] pwd) throws Exception {
        // reading from resource folder
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(EncryptTextWithKeyAndCertificate.class.getResourceAsStream("/seicon.p12"),pwd);
        return (PrivateKey) ks.getKey("1",pwd);
    }

    private static byte[] readAllBytes(InputStream is) throws IOException {
        System.out.println(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int byteRead = -1;
        while((byteRead=is.read(buffer)) != -1){
            baos.write(buffer,0,byteRead);
        }
        return baos.toByteArray();
    }
}
