package com.cn.pwd;

import java.io.File;
import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import com.cn.pwd.domain.Person;
import com.cn.pwd.util.Base64Util;
import com.cn.pwd.util.SerializerUtil;
import com.cn.pwd.x509.Cert;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import static com.cn.pwd.rsa.RSAEncrpy.generateKey;
import static com.cn.pwd.rsa.RSAEncrpy.saveFile;

public class TestApp {

    public static void main(String[] args) throws Exception{

      /*   KeyPair keyPair = generateKey();
        // 私钥
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] privateBT = privateKey.getEncoded();
        // base64
        File file = new File("d://privateKey_.key");
        file.createNewFile();
        saveFile("d://privateKey_.key", Base64.encodeBase64String(privateBT));
        //============================================//
        // 公钥
        PublicKey publicKey = keyPair.getPublic();
        byte[] publicBT = publicKey.getEncoded();
        // base64
        file = new File("d://publicKey_.key");
        file.createNewFile();
        saveFile("d://publicKey_.key", Base64.encodeBase64String(publicBT));


        Map map = new HashMap<>();*/

        Cert myCert = new Cert();
        /*KeyPair keyPair_root = myCert.generateKeyPair(10);
        KeyPair keyPair_user = myCert.generateKeyPair(100);
        String[] info = {"huahua_user","hnu","university","china","hunan","changsha","111111","11111111","2"};
        X509Certificate cert = myCert.generateCert(info, keyPair_root, keyPair_user);
        String certPath = "d:/"+info[0]+".cer";
        FileOutputStream fos = new FileOutputStream(certPath);
        fos.write(cert.getEncoded());
        fos.close();*/


       // myCert.featchX509();

        String secret = "MDE2ZWY3MzMtNzg5MS00NWMyLTg2YmYtMDk5YzEzMzBhMDdi";
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = kpg.generateKeyPair();
        String token = Jwts.builder().setSubject("test1").signWith(SignatureAlgorithm.RS256,keyPair.getPrivate()).compact();
        String header = Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(token).getBody().getSubject();
        System.out.println(token);
        System.out.println(header);


    }

}
