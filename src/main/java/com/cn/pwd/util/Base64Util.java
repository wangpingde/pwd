package com.cn.pwd.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {

    final static Base64.Decoder decoder = Base64.getDecoder();
    final static Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 解密base64字符串
     * @param base64String
     * @return
     */
    public static String decoder(String base64String) {
        try {
            return new String(decoder.decode(base64String), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decoder(String base64String, String charset) {
        try {
            return new String(decoder.decode(base64String), charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密base64字符串
     * @param string
     * @return
     */
    public static String encoder(String string) {
        try {
            return encoder.encodeToString(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encoder(String string, String charset) {
        try {
            return encoder.encodeToString(string.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


}
