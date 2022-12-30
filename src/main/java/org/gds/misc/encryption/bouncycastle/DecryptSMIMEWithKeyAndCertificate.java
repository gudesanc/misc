package org.gds.misc.encryption.bouncycastle;

import org.bouncycastle.asn1.pkcs.EncryptedData;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMEUtil;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.FileTypeMap;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.*;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * Decripta un file "smime" con la chiave privat
 * i comandi corrispettivi "openssl" sono:
 *
 *  cripta utilizzando la public key del certificato  ($HOME/seicon.crt)
 *  openssl  smime  -encrypt -aes256  -in  rc_vie_20221229.tsv.gz  -binary  -outform DEM  -out  rc_vie_20221229.tsv.gz.enc  $HOME/seicon.crt
 *
 *
 *  decripta utilizzando la chiave privata (privatekey.pem)
 *  openssl  smime -decrypt  -in  rc_vie_20221229.tsv.gz.enc  -binary -inform DEM -inkey privatekey.pem  -out  rc_vie_20221229.tsv.gz
 *
 */
public class DecryptSMIMEWithKeyAndCertificate {


    public static void main(String[] args) throws Exception {
        if(args==null || args.length<1){
            System.out.println("Necessari 2 argomenti: 1) la password del p12, 2) il path del file da decriptare -Inserire la password come argomento del mail...");
            System.exit(1);
        }
        char[] pwd = args[0].toCharArray();
        decriptaFileSMIME(args[1],pwd);

    }




    private static PublicKey loadPublicKeyFromCertificate() throws Exception{
        InputStream is = DecryptSMIMEWithKeyAndCertificate.class.getResourceAsStream("/seicon.crt");
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        X509Certificate crt = (X509Certificate) cf.generateCertificate(is);
        return crt.getPublicKey();
    }

    /**
     * Nota bene questo metodo serve per decriptare un file critato con la specifica smime
     * ...se bisogna lavorare con le mail fare riferiemtno all'esempio
     * https://github.com/bcgit/bc-java/blob/master/mail/src/main/java/org/bouncycastle/mail/smime/examples/ReadEncryptedMail.java
     *
     * presente anche nel jar
     *
     * @param fileIn il file da decriptare
     * @param pwd la password del pkcs12
     * @throws Exception in caso di errori
     */
    public static void decriptaFileSMIME(String fileIn, char[] pwd) throws Exception{
        //Leggiamo la chiave privata
        PrivateKey privateKey = loadPrivateKeyFromPKCS12(pwd);

        //
        // Creiamo una body part con il file criptato
        //
        MimeBodyPart msg = new MimeBodyPart();
        ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(readAllBytes(new FileInputStream(fileIn)),"application/octet-stream");
        msg.setDataHandler(new DataHandler(byteArrayDataSource));
        msg.setFileName("gino.rere");


        //Creiamo il messaggio SMIME
        SMIMEEnveloped  m = new SMIMEEnveloped(msg);
        //Vediamo i destinatari
        RecipientInformationStore   recipientsInfo = m.getRecipientInfos();
        //
        // Assunto c'e' un solo destinatario
        //
        // altrimenti vedi https://github.com/bcgit/bc-java/blob/master/mail/src/main/java/org/bouncycastle/mail/smime/examples/ReadEncryptedMail.java
        // Fondamnetalmente dobbiamo caricare il certificato associato alla chiave privata e vediamo
        // quale recipient ha quel recipient id
        //
        RecipientInformation  recipientInfo = recipientsInfo.iterator().next();

        // Destinatario unico
        JceKeyTransEnvelopedRecipient me = new JceKeyTransEnvelopedRecipient(privateKey);


        FileOutputStream fos = new FileOutputStream(fileIn+".plain");
        fos.write(recipientInfo.getContent(me));
        fos.flush();
        System.out.println("Contento decriptato salvato su "+fileIn+".plain");
    }
    private static PrivateKey loadPrivateKeyFromPKCS12(char[] pwd) throws Exception {
        // reading from resource folder
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(DecryptSMIMEWithKeyAndCertificate.class.getResourceAsStream("/seicon.p12"),pwd);
        return (PrivateKey) ks.getKey("1",pwd);
    }

    private static byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int byteRead = -1;
        while((byteRead=is.read(buffer)) != -1){
            baos.write(buffer,0,byteRead);
        }
        return baos.toByteArray();
    }
}
