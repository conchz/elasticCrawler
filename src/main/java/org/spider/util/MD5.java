package org.spider.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2015-10-12.
 *
 * @author dolphineor
 */
public class MD5 {

    private String md5;


    private MD5(String value, String salt) {
        this.md5 = generateMD5(value + "{" + salt + "}");
    }

    private String generateMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            md5 = buf.toString();
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMD5() {
        return md5;
    }

    public static class Builder {

        private String value;
        private String salt;

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder salt(String salt) {
            this.salt = salt;
            return this;
        }

        public String build() {
            return new MD5(value, salt).getMD5();
        }
    }
}
