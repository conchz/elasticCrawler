package org.spider.extractor.keyword;

import java.util.List;

/**
 * Created on 2015-08-29.
 *
 * @author dolphineor
 */
public interface KeywordExtractor {

    /**
     * <p>提取文本关键字.
     *
     * @param content       文本内容
     * @param algorithmType 算法类型
     * @param extractNum    关键字数量
     * @return 排序好的关键字列表
     */
    List<String> extractContent(String content, String algorithmType, int extractNum);
}
