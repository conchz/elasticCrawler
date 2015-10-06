package com.github.dolphineor.filter;

import com.github.dolphineor.extractor.Page;

/**
 * Created on 2015-08-29.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface IFilter {

    /**
     * <p>判断内容的相似性, 为1表示已经存在, 为0表示不存在, 为浮点数则判断相似度的值.
     *
     * @param page {@link Page}
     * @return similarity
     */
    float similar(Page page);
}
