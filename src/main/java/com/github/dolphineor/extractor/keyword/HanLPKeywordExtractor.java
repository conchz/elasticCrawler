package com.github.dolphineor.extractor.keyword;

import com.alibaba.fastjson.JSON;
import com.github.dolphineor.util.Logs;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.util.*;

/**
 * Created on 2015-08-29.
 *
 * @author dolphineor
 */
public class HanLPKeywordExtractor extends Logs implements KeywordExtractor {

    public List<String> extractContent(String content, String algorithmType, int extractNum) {
        List<String> keywordList = null;
        try {
            keywordList = HanLP.extractKeyword(content, extractNum);
        } catch (NumberFormatException ignored) {
            logger.info("extractNum format exception");
        }
        return keywordList;
    }

    /**
     * <p>获取指定数量的关键字以及关键词出现的次数, 并封装成JSON的格式返回.
     *
     * @param content    文本内容
     * @param extractNum 关键字提取数量
     * @return 提取好的JSON格式关键字
     */
    public String getJSONKeyWordInfo(String content, int extractNum) throws NumberFormatException, IOException {

        //先将文本中的换行符去掉
        content = content.replaceAll("\n", "");
        //List<Term> str = HanLP.segment(text);
        //与直接new一个分词器相比, 使用本方法的好处是, 以后HanLP升级了, 总能用上最合适的分词器:当前使用的分词器: Viterbi分词器是目前效率和效果的最佳平衡
        List<Term> list = HanLP.newSegment().seg(content);
        final HashMap<String, Integer> map = new HashMap<>();
        int count;
        for (Term term : list) {
            count = 1;
            //去除符号
            if (!term.nature.toString().startsWith("w")) {
                //System.out.println(term.word+"--"+term.nature);
                if (map.containsKey(term.word)) {
                    count += map.get(term.word);
                    map.put(term.word, count);
                } else {
                    map.put(term.word, count);
                }
            }
        }

        final Comparator<String> comparator = (a, b) -> {
            if (map.get(a) >= map.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        };

//        MapValueComparator vc = new MapValueComparator(map);
        TreeMap<String, Integer> sortMapTmp = new TreeMap<>(comparator);
        TreeMap<String, Integer> sortMap = new TreeMap<>(comparator);
        sortMapTmp.putAll(map);

//        System.out.println(sortMapTmp);

        Set<String> keys = sortMapTmp.keySet();
        int i = 0;
        for (String key : keys) {
            if (i < extractNum) {
                sortMap.put(key, map.get(key));
                i++;
            } else {
                break;
            }
        }

        return JSON.toJSONString(sortMap);
    }

}
