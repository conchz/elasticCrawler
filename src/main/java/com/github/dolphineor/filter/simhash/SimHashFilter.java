package com.github.dolphineor.filter.simhash;

import com.github.dolphineor.extractor.Page;
import com.github.dolphineor.filter.IFilter;

import java.math.BigInteger;


/**
 * Created on 2015-08-29.
 * <p>根据simhash算法对文档进行过滤.
 *
 * @author dolphineor
 */
public class SimHashFilter implements IFilter {

    @Override
    public float similar(Page page) {
        SimHash simHash = new SimHash(page.getContent());
        BigInteger hash = simHash.simHash();

        float resultNum = 0;
        if (SimHashChecker.INSTANCE.checkSimilar(hash)) {
            resultNum = 1;
        }
        return resultNum;
    }
}