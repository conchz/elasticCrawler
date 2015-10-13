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
     * 所有文件tf结果
     * key:文件名, value:该文件tf
     */
    private static Map<String, Map<String, Double>> allTfMap = new HashMap<>();

    /**
     * 所有文件分词结果
     * key:文件名, value:该文件分词统计
     */
    private static Map<String, Map<String, Integer>> allSegsMap = new HashMap<>();

    /**
     * 所有文件分词的idf结果
     * key: 文件名,
     * value: 词w在整个文档集合中的逆向文档频率idf (Inverse Document Frequency), 即文档总数n与词w所出现文件数docs(w, D)比值的对数
     */
    private static Map<String, Double> idfMap = new HashMap<>();

    /**
     * 统计包含单词的文档数
     * key: 单词, value: 包含该词的文档数
     */
    private static Map<String, Integer> containWordOfAllDocNumberMap = new HashMap<>();

    /**
     * 统计单词的TF-IDF
     * key:文件名, value:该文件tf-idf
     */
    private static Map<String, Map<String, Double>> tfIdfMap = new HashMap<>();


    /**
     * <p>读取指定目录下及其子目录内的所有文件
     *
     * @param directory 目录路径
     * @return 文件列表
     */
    private static List<String> readDirs(String directory) {
        final List<String> fileList = new ArrayList<>();
        Path path = Paths.get(directory);
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
     * <p>用ik进行字符串分词, 统计各个词出现的次数.
     *
     * @param content 文本内容
     * @return 各个词出现的次数
     */
    private static Map<String, Integer> segString(String content) {
        // 分词
        Reader input = new StringReader(content);
        // 智能分词关闭 (对分词的精度影响很大)
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme;
        Map<String, Integer> words = new HashMap<>();
        try {
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

    public static Map<String, Integer> segStr(String content) {
        // 分词
        Reader input = new StringReader(content);
        // 智能分词关闭（对分词的精度影响很大）
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme;
        Map<String, Integer> words = new LinkedHashMap<>();
        try {
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
     * <p>分词结果转化为tf, 公式为: tf(w,d) = count(w, d) / size(d)
     * 即词w在文档d中出现次数count(w, d)和文档d中总词数size(d)的比值</p>
     *
     * @param segWordsResult
     * @return TF
     */
    private static HashMap<String, Double> tf(Map<String, Integer> segWordsResult) {
        HashMap<String, Double> tf = new HashMap<>();   // 正规化
        if (segWordsResult == null || segWordsResult.size() == 0) {
            return tf;
        }

        Double size = (double) segWordsResult.size();
        Set<String> keys = segWordsResult.keySet();
        for (String key : keys) {
            Integer value = segWordsResult.get(key);
            tf.put(key, Double.valueOf(value) / size);
        }

        return tf;
    }

    /**
     * <p>得到所有文件的tf.
     *
     * @param dir
     * @return {@link #allTfMap}
     */
    public static Map<String, Map<String, Double>> allTf(String dir) {
        List<String> fileList = readDirs(dir);
        for (String filePath : fileList) {
            String content = readFile(filePath);
            Map<String, Integer> segs = segString(content);
            allSegsMap.put(filePath, segs);
            allTfMap.put(filePath, tf(segs));
        }

        return allTfMap;
    }

    /**
     * <p>wordSegCount 返回分词结果, 以LinkedHashMap保存.
     *
     * @param dir
     * @return {@link #allSegsMap}
     */
    public static Map<String, Map<String, Integer>> wordSegCount(String dir) {
        List<String> fileList = readDirs(dir);
        for (String filePath : fileList) {
            String content = readFile(filePath);
            Map<String, Integer> segs = segStr(content);
            allSegsMap.put(filePath, segs);
        }

        return allSegsMap;
    }


    /**
     * <p>统计包含单词的文档数  key:单词  value:包含该词的文档数.
     *
     * @param allSegsMap
     * @return {@link #containWordOfAllDocNumberMap}
     */
    private static Map<String, Integer> containWordOfAllDocNumber(Map<String, Map<String, Integer>> allSegsMap) {
        if (allSegsMap == null || allSegsMap.size() == 0) {
            return containWordOfAllDocNumberMap;
        }

        Set<String> fileList = allSegsMap.keySet();
        for (String filePath : fileList) {
            Map<String, Integer> fileSegs = allSegsMap.get(filePath);
            // 获取该文件分词为空或为0, 进行下一个文件
            if (fileSegs == null || fileSegs.size() == 0) {
                continue;
            }
            // 统计每个分词的idf
            Set<String> segs = fileSegs.keySet();
            for (String seg : segs) {
                if (containWordOfAllDocNumberMap.containsKey(seg)) {
                    containWordOfAllDocNumberMap.put(seg, containWordOfAllDocNumberMap.get(seg) + 1);
                } else {
                    containWordOfAllDocNumberMap.put(seg, 1);
                }
            }

        }

        return containWordOfAllDocNumberMap;
    }

    /**
     * <p>idf = log(n / docs(w, D))
     *
     * @param allSegsMap
     * @return {@link #idfMap}
     */
    public static Map<String, Double> idf(Map<String, Map<String, Integer>> allSegsMap) {
        if (allSegsMap == null || allSegsMap.size() == 0) {
            return idfMap;
        }

        containWordOfAllDocNumberMap = containWordOfAllDocNumber(allSegsMap);
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
     * @return {@link #tfIdfMap}
     */
    public static Map<String, Map<String, Double>> tfIdf(Map<String, Map<String, Double>> allTfMap, Map<String, Double> idf) {
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


    public static void main(String[] args) {
        System.out.println("tf--------------------------------------");
        Map<String, Map<String, Double>> allTfMap = TfIdf.allTf(".");
        Set<String> fileList = allTfMap.keySet();
        for (String filePath : fileList) {
            Map<String, Double> tfMap = allTfMap.get(filePath);
            Set<String> words = tfMap.keySet();
            for (String word : words) {
                System.out.println("fileName:" + filePath + "   word:" + word + "   tf:" + tfMap.get(word));
            }
        }

        System.out.println("idf--------------------------------------");
        Map<String, Double> idfMap = TfIdf.idf(allSegsMap);
        Set<String> words = idfMap.keySet();
        for (String word : words) {
            System.out.println("word:" + word + "   tf:" + idfMap.get(word));
        }

        System.out.println("tf-idf--------------------------------------");
        Map<String, Map<String, Double>> tfIdfMap = TfIdf.tfIdf(allTfMap, idfMap);
        Set<String> files = tfIdfMap.keySet();
        for (String filePath : files) {
            Map<String, Double> tfIdf = tfIdfMap.get(filePath);
            Set<String> segs = tfIdf.keySet();
            for (String word : segs) {
                System.out.println("fileName:" + filePath + "   word:" + word + "   tf-idf:" + tfIdf.get(word));
            }
        }
    }
}
