package org.spider.filter.simhash;

import org.nlpcn.commons.lang.dic.DicManager;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.StringUtil;
import org.spider.util.Logs;
import org.spider.util.RegexLanguageUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created on 2015-08-29.
 * <p>SimHash类, 用于计算文本的相似度信息.
 *
 * @author dolphineor
 */
public class SimHash extends Logs {

    /**
     * 中文词典对象
     */
    private static Forest forest = null;

    /**
     * 加载词典信息
     */
    static {
        try {
            forest = Library.makeForest(DicManager.class
                    .getResourceAsStream("/finger.dic"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String tokens;

    private BigInteger intSimHash;

    private int hashbits = 64;

    public SimHash(String tokens) {
        this.tokens = tokens;
        this.intSimHash = this.simHash();
    }

    public SimHash(String tokens, int hashbits) {
        this.tokens = tokens;
        this.hashbits = hashbits;
        this.intSimHash = this.simHash();
    }

    public BigInteger simHash() {
        // 定义特征向量/数组
        int[] v = new int[this.hashbits];
        this.tokens = StringUtil.rmHtmlTag(this.tokens);
        String tmp;

        // 英文处理
        if (RegexLanguageUtils.INSTANCE.isEnglish(this.tokens)) {
            StringTokenizer stringTokens = new StringTokenizer(this.tokens);
            while (stringTokens.hasMoreTokens()) {
                tmp = stringTokens.nextToken();
                BigInteger t = this.hash(tmp);
                for (int i = 0; i < this.hashbits; i++) {
                    BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                    if (t.and(bitmask).signum() != 0) {
                        v[i] += 1;
                    } else {
                        v[i] -= 1;
                    }
                }
            }
        }
        // 中文处理
        else {
            GetWord word = new GetWord(forest, this.tokens);
            while ((tmp = word.getFrontWords()) != null) {
                if (tmp.isEmpty()) {
                    continue;
                }
                tmp = tmp.toLowerCase();

                BigInteger t = this.hash(tmp);
                for (int i = 0; i < this.hashbits; i++) {
                    BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                    // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
                    // 对每一个分词hash后的数列进行判断,如果是1000...1, 那么数组的第一位和末尾一位加1,
                    // 中间的62位减一, 也就是说, 逢1加1, 逢0减1. 一直到把所有的分词hash数列全部判断完毕.
                    if (t.and(bitmask).signum() != 0) {
                        // 这里是计算整个文档的所有特征的向量和,
                        // 实际使用中需要 +- 权重, 而不是简单的 +1/-1.
                        v[i] += 1;
                    } else {
                        v[i] -= 1;
                    }
                }
            }
        }
        // 生成密识串
        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < this.hashbits; i++) {
            // 4、最后对数组进行判断, 大于0的记为1, 小于等于0的记为0, 得到一个 64bit 的数字指纹/签名.
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }

        return fingerprint;
    }

    private BigInteger hash(String source) {
        if (source == null || source.isEmpty()) {
            return new BigInteger("0");
        } else {
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }

            return x;
        }
    }

    public int hammingDistance(SimHash other) {
        BigInteger x = this.intSimHash.xor(other.intSimHash);
        int tot = 0;

        // 统计x中二进制位数为1的个数
        // 我们想想, 一个二进制数减去1, 那么, 从最后那个1(包括那个1)后面的数字全都反了, 对吧, 然后, n&(n-1)就相当于把后面的数字清0,
        // 我们看n能做多少次这样的操作就OK了。

        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }

        return tot;
    }

    public int getDistance(String str1, String str2) {
        int distance;
        if (str1.length() != str2.length()) {
            distance = -1;
        } else {
            distance = 0;
            for (int i = 0, l = str1.length(); i < l; i++) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    distance++;
                }
            }
        }

        return distance;
    }

    public List<BigInteger> subByDistance(SimHash simHash, int distance) {
        // 分成几组来检查
        int numEach = this.hashbits / (distance + 1);
        List<BigInteger> characters = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < this.intSimHash.bitLength(); i++) {
            // 当且仅当设置了指定的位时，返回 true
            boolean sr = simHash.intSimHash.testBit(i);
            if (sr) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((i + 1) % numEach == 0) {
                // 将二进制转为BigInteger
                BigInteger eachValue = new BigInteger(buffer.toString(), 2);
                logger.info("----{}", eachValue);
                buffer.delete(0, buffer.length());
                characters.add(eachValue);
            }
        }

        return characters;
    }
}