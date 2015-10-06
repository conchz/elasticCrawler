package com.github.dolphineor.util;

import org.nlpcn.commons.lang.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2015-08-29.
 * <p>语主信息的判断类.
 *
 * @author dolphineor
 */
public class RegexLanguageUtils {

    /**
     * 中文件信息的判断类
     */
    private static final String CHINESE_PATTERN = "[\u4e00-\u9fa5]";

    /**
     * 日语字符集
     */
    private static final String JAPANESE_PATTERN = "[\u0800-\u4e00]";

    /**
     * 英文字符集
     */
    private static final String ENGLISH_PATTERN = "[\u0020-\u007F]+";

    private RegexLanguageUtils() {
    }

    /**
     * 单一实例
     */
    public static final RegexLanguageUtils INSTANCE = new RegexLanguageUtils();

    /**
     * <p>判断是否只包含英文相关字符.
     *
     * @param text 文本信息
     * @return isEnglish
     */
    public boolean isEnglish(String text) {
        boolean result = false;
        if (StringUtil.isNotBlank(text)) {
            result = text.matches(ENGLISH_PATTERN);
        }
        return result;
    }

    /**
     * <p>判断字符串中是否包含英文字符.
     *
     * @param text 文本信息
     * @return isChinese
     */
    public boolean isChinese(String text) {
        boolean result = false;
        if (StringUtil.isNotBlank(text)) {
            Pattern p = Pattern.compile(CHINESE_PATTERN);
            Matcher m = p.matcher(text);
            if (m.find()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * <p>判断是否为日语.
     *
     * @param text 文本信息
     * @return isJapanese
     */
    public boolean isJapanese(String text) {
        boolean result = false;
        if (StringUtil.isNotBlank(text)) {
            Pattern p = Pattern.compile(JAPANESE_PATTERN);
            Matcher m = p.matcher(text);
            if (m.find()) {
                result = true;
            }
        }
        return result;
    }
}
