package com.cn.pwd.sercurity;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

public class EncryptUtil {

    private static final int PASSWORDKEYLENGTH = 8;

    /**
     * 加密
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt(String password) {
        String sSafeCode = getRandomString(PASSWORDKEYLENGTH);// 产生一段随机字符串，作为安全域。
        return sSafeCode + encryptWithSafeCode(password, sSafeCode);
    }

    /**
     * 密码校验<br/>
     * password-明文<br/>
     * safePassword-密文<br/>
     * @param password-明文
     * @param safePassword-密文
     * @return
     * @throws Exception
     */
    public static boolean checkedPassword(String password,String safePassword){
        if(encryptWithSafeCode(password,safePassword.trim().substring(0, PASSWORDKEYLENGTH)).equals(safePassword.trim().substring(PASSWORDKEYLENGTH, safePassword.length()))){
            return true;
        }
        return false;
    }

    private static String encryptWithSafeCode(String password, String safeCode){
        try {
            byte[] abyteSafeCode = Base64.decodeBase64(safeCode);
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            //messagedigest.update(abyteSafeCode); 此句在最后加密中没有实际意义
            messagedigest.update(password.getBytes("UTF8"));
            return Base64.encodeBase64String(messagedigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static java.util.Random random = new java.util.Random();

    private static String getRandomString(int length) {
        char[] ach = new char[length];
        for (int i = 0; i < ach.length; i++) {
            ach[i] = getRandomChar();
        }

        return new String(ach);
    }

    private static char getRandomChar() {
        char ch;

        int iRandomInt = random.nextInt(52);

        if (iRandomInt < 10) {
            // 数字，0的ASCII码是48。
            ch = (char) (iRandomInt + 48);
        } else if (iRandomInt < 36) {
            // 大写字母，A的ASCII码是65。
            // ch = (char)(iRandomInt - 10 + 66);
            ch = (char) (iRandomInt + 56);
        } else {
            // 小写字母，a的ASCII码是97。
            // ch = (char)(iRandomInt - 36 + 97);
            ch = (char) (iRandomInt + 61);
        }

        return ch;
    }
}
