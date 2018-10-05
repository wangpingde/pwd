package com.cn.pwd.x509;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import sun.misc.BASE64Encoder;

public class Cert {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    /**
     * 根据seed产生密钥对
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     */
    public KeyPair generateKeyPair(int seed) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024, new SecureRandom(new byte[seed]));
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }

    /**
     * 产生数字公钥证书
     * String[] info长度为9，分别是{cn,ou,o,c,l,st,starttime,endtime,serialnumber}
     * @throws SignatureException
     * @throws SecurityException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     */
    public X509Certificate generateCert(String[] info, KeyPair keyPair_root, KeyPair keyPair_user) throws InvalidKeyException, NoSuchProviderException, SecurityException, SignatureException {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X509Certificate cert = null;
        certGen.setSerialNumber(new BigInteger(info[8]));
        certGen.setIssuerDN(new X509Name(
                "CN=huahua, OU=hnu, O=university , C=china"));
        certGen.setNotBefore(new Date(Long.parseLong(info[6])));
        certGen.setNotAfter(new Date(Long.parseLong(info[7])));
        certGen.setSubjectDN(new X509Name("C=" + info[0] + ",OU=" + info[1]
                + ",O=" + info[2] + ",C=" + info[3] + ",L=" + info[4] + ",ST="
                + info[3]));
        certGen.setPublicKey(keyPair_user.getPublic());
        certGen.setSignatureAlgorithm("SHA1WithRSA");
        cert = certGen.generateX509Certificate(keyPair_root.getPrivate(), "BC");
        return cert;
    }

    /**
     * 获取证书
     * @return
     */
    public X509Certificate featchX509() {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate cert = null;
        try {
            cert = (X509Certificate) cf.generateCertificate(new FileInputStream("D:/huahua_user.cer"));
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PublicKey publicKey = cert.getPublicKey();
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String publicKeyString = base64Encoder.encode(publicKey.getEncoded());
        System.out.println("-----------------公钥--------------------");
        System.out.println(publicKeyString);
        System.out.println("-----------------公钥--------------------");
        return cert;
    }


}
