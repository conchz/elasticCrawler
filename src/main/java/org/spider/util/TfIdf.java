package org.spider.util;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created on 2015-10-13.
 * <p>TF-IDF算法实现.</p>
 * <a href="http://www.cnblogs.com/biyeymyhjob/archive/2012/07/17/2595249.html"></a>
 *
 * @author dolphineor
 */
public class TfIdf {

    /**
     * <p>读取指定目录下及其子目录内的所有文件
     *
     * @param dir 目录路径
     * @return 文件列表
     */
    private static List<String> readDirs(String dir) {
        final List<String> fileList = new ArrayList<>();
        Path path = Paths.get(dir);
        try {
            if (Files.isDirectory(path)) {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        fileList.add(file.getFileName().toFile().getAbsolutePath());
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    /**
     * <p>将读取的文件转化成String.
     *
     * @param filePath 文件路径
     * @return java.lang.String
     */
    private static String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8)
                    .forEach(line -> stringBuilder.append(line).append("\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    /**
     * <p>统计包含单词的文档数  key:单词  value:包含该词的文档数.
     *
     * @param allSegmentsMap 所有文件分词结果
     * @return 统计包含单词的文档数  key: 单词, value: 包含该词的文档数
     */
    private static Map<String, Integer> containWordOfAllDocNumber(Map<String, Map<String, Integer>> allSegmentsMap) {
        if (allSegmentsMap == null || allSegmentsMap.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Integer> containWordOfAllDocNumberMap = new HashMap<>();

        Set<String> fileList = allSegmentsMap.keySet();
        for (String filePath : fileList) {
            Map<String, Integer> fileSegmentsMap = allSegmentsMap.get(filePath);
            // 获取该文件分词为空或为0, 进行下一个文件
            if (fileSegmentsMap == null || fileSegmentsMap.isEmpty()) {
                continue;
            }
            // 统计每个分词的idf
            Set<String> segments = fileSegmentsMap.keySet();
            for (String segment : segments) {
                if (containWordOfAllDocNumberMap.containsKey(segment)) {
                    containWordOfAllDocNumberMap.put(segment, containWordOfAllDocNumberMap.get(segment) + 1);
                } else {
                    containWordOfAllDocNumberMap.put(segment, 1);
                }
            }

        }

        return containWordOfAllDocNumberMap;
    }

    /**
     * <p>分词结果转化为tf, 公式为: tf(w,d) = count(w, d) / size(d)
     * 即词w在文档d中出现次数count(w, d)和文档d中总词数size(d)的比值</p>
     *
     * @param segmentedWords
     * @return TF
     */
    private static HashMap<String, Double> tf(Map<String, Integer> segmentedWords) {
        HashMap<String, Double> tf = new HashMap<>();   // 正规化
        if (segmentedWords == null || segmentedWords.isEmpty()) {
            return tf;
        }

        Double size = (double) segmentedWords.size();
        Set<String> keys = segmentedWords.keySet();
        for (String key : keys) {
            Integer value = segmentedWords.get(key);
            tf.put(key, Double.valueOf(value) / size);
        }

        return tf;
    }


    /**
     * <p>用ik进行字符串分词, 统计各个词出现的次数.
     *
     * @param content 文本内容
     * @return 各个词出现的次数
     */
    public static Map<String, Integer> segmentPlaintext(String content) {
        // 分词
        Map<String, Integer> words = new LinkedHashMap<>();
        try (Reader input = new StringReader(content)) {
            // 智能分词关闭 (对分词的精度影响很大)
            IKSegmenter iks = new IKSegmenter(input, true);
            Lexeme lexeme;

            while ((lexeme = iks.next()) != null) {
                if (words.containsKey(lexeme.getLexemeText())) {
                    words.put(lexeme.getLexemeText(), words.get(lexeme.getLexemeText()) + 1);
                } else {
                    words.put(lexeme.getLexemeText(), 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

    public static Map<String, Integer> getMostFrequentWords(int num, Map<String, Integer> words) {
        Map<String, Integer> keywords = new LinkedHashMap<>();
        int count = 0;
        // 词频统计
        List<Map.Entry<String, Integer>> infoMap = new ArrayList<>(words.entrySet());
        Collections.sort(infoMap, (o1, o2) -> o2.getValue() - o1.getValue());

        // 高频词输出
        for (Map.Entry<String, Integer> entry : infoMap) {
            // 词-->频
            if (entry.getKey().length() > 1) {
                if (num > count) {
                    keywords.put(entry.getKey(), entry.getValue());
                    count++;
                } else {
                    break;
                }
            }
        }

        return keywords;
    }

    /**
     * <p>得到所有文件的tf.
     *
     * @param dir
     * @return 所有文件tf结果  key:文件名, value:该文件tf
     */
    public static Map<String, Map<String, Double>> allTf(String dir) {
        Map<String, Map<String, Double>> allTfMap = new HashMap<>();

        List<String> fileList = readDirs(dir);
        for (String filePath : fileList) {
            String content = readFile(filePath);
            Map<String, Integer> segments = segmentPlaintext(content);
            allTfMap.put(filePath, tf(segments));
        }

        return allTfMap;
    }

    /**
     * <p>wordSegmentCount 返回分词结果, 以LinkedHashMap保存.
     *
     * @param dir
     * @return 所有文件分词结果  key:文件名, value:该文件分词统计
     */
    public static Map<String, Map<String, Integer>> wordSegmentCount(String dir) {
        Map<String, Map<String, Integer>> allSegmentsMap = new HashMap<>();

        List<String> fileList = readDirs(dir);
        for (String filePath : fileList) {
            String content = readFile(filePath);
            Map<String, Integer> segments = segmentPlaintext(content);
            allSegmentsMap.put(filePath, segments);
        }

        return allSegmentsMap;
    }

    /**
     * <p>idf = log(n / docs(w, D))
     *
     * @param allSegmentsMap
     * @return 所有文件分词的idf结果
     * key: 文件名,
     * value: 词w在整个文档集合中的逆向文档频率idf (Inverse Document Frequency), 即文档总数n与词w所出现文件数docs(w, D)比值的对数
     */
    public static Map<String, Double> idf(Map<String, Map<String, Integer>> allSegmentsMap) {
        if (allSegmentsMap == null || allSegmentsMap.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Double> idfMap = new HashMap<>();

        Map<String, Integer> containWordOfAllDocNumberMap = containWordOfAllDocNumber(allSegmentsMap);
        Set<String> words = containWordOfAllDocNumberMap.keySet();
        Double wordSize = (double) containWordOfAllDocNumberMap.size();
        for (String word : words) {
            Double number = Double.valueOf(containWordOfAllDocNumberMap.get(word));
            idfMap.put(word, Math.log(wordSize / (number + 1.0d)));
        }

        return idfMap;
    }

    /**
     * <p>TF-IDF
     *
     * @param allTfMap
     * @param idf
     * @return 统计的单词的TF-IDF  key:文件名, value:该文件tf-idf
     */
    public static Map<String, Map<String, Double>> tfIdf(Map<String, Map<String, Double>> allTfMap, Map<String, Double> idf) {
        Map<String, Map<String, Double>> tfIdfMap = new HashMap<>();

        Set<String> fileList = allTfMap.keySet();
        for (String filePath : fileList) {
            Map<String, Double> tfMap = allTfMap.get(filePath);
            Map<String, Double> docTfIdf = new HashMap<>();
            Set<String> words = tfMap.keySet();
            for (String word : words) {
                Double tfValue = tfMap.get(word);
                Double idfValue = idf.get(word);
                docTfIdf.put(word, tfValue * idfValue);
            }
            tfIdfMap.put(filePath, docTfIdf);
        }

        return tfIdfMap;
    }
}
