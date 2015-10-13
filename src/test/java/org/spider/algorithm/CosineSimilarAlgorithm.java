package org.spider.algorithm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2015-10-13.
 * <p>余弦获取文章相似性.
 *
 * @author dolphineor
 */
public class CosineSimilarAlgorithm {

    /**
     * <p>获取两个文件相似性.
     *
     * @param firstFile  第一个文件
     * @param secondFile 第二个文件
     * @return 文件相似性
     */
    public static Double cosSimilarityByFile(String firstFile, String secondFile) {
        try {
            Map<String, Map<String, Integer>> firstTfMap = TfIdf.wordSegCount(firstFile);
            Map<String, Map<String, Integer>> secondTfMap = TfIdf.wordSegCount(secondFile);
            if (firstTfMap == null || firstTfMap.size() == 0) {
                throw new IllegalArgumentException("firstFile not found or firstFile is empty! ");
            }
            if (secondTfMap == null || secondTfMap.size() == 0) {
                throw new IllegalArgumentException("secondFile not found or secondFile is empty! ");
            }
            Map<String, Integer> firstWords = firstTfMap.get(firstFile);
            Map<String, Integer> secondWords = secondTfMap.get(secondFile);
            if (firstWords.size() < secondWords.size()) {
                Map<String, Integer> temp = firstWords;
                firstWords = secondWords;
                secondWords = temp;
            }
            return calculateCos((LinkedHashMap<String, Integer>) firstWords, (LinkedHashMap<String, Integer>) secondWords);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0d;
    }

    /**
     * <p>获取两个字符串的相似性.
     *
     * @param first  第一个字符串
     * @param second 第二个字符串
     * @return 相似程度
     */
    public static Double cosSimilarityByString(String first, String second) {
        try {
            Map<String, Integer> firstTfMap = TfIdf.segStr(first);
            Map<String, Integer> secondTfMap = TfIdf.segStr(second);
            if (firstTfMap.size() < secondTfMap.size()) {
                Map<String, Integer> temp = firstTfMap;
                firstTfMap = secondTfMap;
                secondTfMap = temp;
            }
            return calculateCos((LinkedHashMap<String, Integer>) firstTfMap, (LinkedHashMap<String, Integer>) secondTfMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0d;
    }

    /**
     * <p>计算余弦相似性.
     *
     * @param first  第一个字符串
     * @param second 第二个字符串
     * @return 余弦相似性
     */
    private static Double calculateCos(LinkedHashMap<String, Integer> first, LinkedHashMap<String, Integer> second) {

        List<Map.Entry<String, Integer>> firstList = new ArrayList<>(first.entrySet());
        List<Map.Entry<String, Integer>> secondList = new ArrayList<>(second.entrySet());
        //计算相似度
        double vectorFirstModulo = 0.00;    // 向量1的模
        double vectorSecondModulo = 0.00;   // 向量2的模
        double vectorProduct = 0.00;        // 向量积
        int secondSize = second.size();
        for (int i = 0; i < firstList.size(); i++) {
            if (i < secondSize) {
                vectorSecondModulo += secondList.get(i).getValue().doubleValue() * secondList.get(i).getValue().doubleValue();
                vectorProduct += firstList.get(i).getValue().doubleValue() * secondList.get(i).getValue().doubleValue();
            }
            vectorFirstModulo += firstList.get(i).getValue().doubleValue() * firstList.get(i).getValue().doubleValue();
        }
        return vectorProduct / (Math.sqrt(vectorFirstModulo) * Math.sqrt(vectorSecondModulo));
    }

    public static void main(String[] args) {
        Double result = cosSimilarityByString("Scala可伸缩的语言是一门多范式的编程语言，一种类似java的编程语言，设计初衷是要集成面向对象编程和函数式编程的各种特性。",
                "Scala编程语言抓住了很多开发者的眼球。如果你粗略浏览Scala的网站，你会觉得Scala是一种纯粹的面向对象编程语言，而又无缝地结合了命令式和函数式的编程风格。");

        System.out.println(result);
    }
}
