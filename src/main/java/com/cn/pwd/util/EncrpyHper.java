package com.cn.pwd.util;

import org.apache.commons.codec.binary.Base64;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EncrpyHper {


    /**
     * 传入名文和公钥钥对数据进行RSA解密
     * <br>生成时间：2014年5月2日  下午2:38:13
     * <br>返回值：String
     * <br>@param src
     * <br>@param pubkey
     * <br>@return
     */
    public static String rsaEncoding(String src, PublicKey pubkey) {
        try {
            Cipher cip = Cipher.getInstance("RSA");
            cip.init(cip.ENCRYPT_MODE, pubkey);
            byte[] by = cip.doFinal(src.getBytes());
            return new String(Base64.decodeBase64(by));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 传入RSA密文和私钥对数据进行解密
     * <br>生成时间：2014年5月2日  下午2:37:08
     * <br>返回值：String
     * <br>@param sec
     * <br>@param privkey
     * <br>@return
     */
    public static String rsaDeEncoding(String sec, PrivateKey privkey) {
        try {
            Cipher cip = Cipher.getInstance("RSA");
            cip.init(cip.DECRYPT_MODE, privkey);
            byte[] by = Base64.decodeBase64(sec.getBytes());
            return new String(cip.doFinal(by));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 传入字符串、密钥，并加密字符串（对称加密加密），支持：DES、AES、DESede(3DES)
     * <br>生成时间：2014年5月2日  下午12:05:44
     * <br>返回值：String 密文
     * <br>@param src
     * <br>@param key
     * <br>@param method(DES、AES、DESede)
     * <br>@return
     */
    //对称加密加密
    public static String doubKeyEncoding(String src, String keysrc, String method) {
        SecretKey key;
        try {
            //生成密钥
            KeyGenerator kg = KeyGenerator.getInstance(keysrc);
            //初始化此密钥生成器。
            kg.init(new SecureRandom(keysrc.getBytes("utf-8")));
            key = kg.generateKey();

            //加密
            Cipher ciph = Cipher.getInstance(method);
            ciph.init(Cipher.ENCRYPT_MODE, key);
            ciph.update(src.getBytes("utf-8"));
            //使用64进行编码，一避免出现丢数据情景
            byte[] by = Base64.decodeBase64(ciph.doFinal());
            return new String(by);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 传入字符串、密钥、加密方式，并解密字符串（对称加密解密密），支持：DES、AES、DESede(3DES)
     * <br>生成时间：2014年5月2日  下午1:12:13
     * <br>返回值：String 密钥原文
     * <br>@param sec
     * <br>@param key
     * <br>@param method(DES、AES、DESede)
     * <br>@return
     */
    public static String doubKeyDencoding(String sec, String keysrc, String method) {
        SecretKey key;
        try {
            //生成密钥
            KeyGenerator kg = KeyGenerator.getInstance(keysrc);
            //初始化此密钥生成器。
            kg.init(new SecureRandom(keysrc.getBytes("utf-8")));
            key = kg.generateKey();
            //加密
            Cipher ciph = Cipher.getInstance(method);
            ciph.init(ciph.DECRYPT_MODE, key);
            //使用64进行解码，一避免出现丢数据情况
            byte[] by = Base64.decodeBase64(sec.getBytes());
            ciph.update(by);
            return new String(ciph.doFinal());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 单向信息加密(信息摘要)，支持：md5、md2、SHA(SHA-1，SHA1)、SHA-256、SHA-384、SHA-512,
     * <br>生成时间：2014年5月2日  上午11:13:44
     * <br>返回值：String         加密后的密文
     * <br>@param src     传入加密字符串(明文)
     * <br>@param method  指定算法(md5、md2、SHA(SHA-1，SHA1)、SHA-256、SHA-384、SHA-512)
     * <br>@return
     */
    public static String ecodingPasswd(String src, String method) {

        try {
            //信息摘要算法
            MessageDigest md5 = MessageDigest.getInstance(method);
            md5.update(src.getBytes());
            byte[] encoding = md5.digest();
            //使用64进行编码，一避免出现丢数据情景
            return new String(Base64.decodeBase64(encoding));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e + "加密失败！！");
        }

    }

    /**
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    /**
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


}
