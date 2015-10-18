package org.spider.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

/**
 * Created on 2015-10-12.
 *
 * @author dolphineor
 */
public class MD5 {

    /**
     * <p>用来将字节转换成十六进制表示的字符.
     */
    static final char[] hexDigests = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    static final Function<String, String> md5Function = source -> {
        String md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes(StandardCharsets.UTF_8));
            byte tmp[] = md.digest();

            char chars[] = new char[32];

            int index = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                chars[index++] = hexDigests[byte0 >>> 4 & 0xf];
                chars[index++] = hexDigests[byte0 & 0xf];
            }

            md5 = new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return md5;
    };


    public static String getMD5(String source) {
        return md5Function.apply(source);
    }
}
