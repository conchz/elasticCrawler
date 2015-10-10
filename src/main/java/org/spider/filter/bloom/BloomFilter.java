package org.spider.filter.bloom;

import com.google.common.hash.Funnels;
import org.spider.extractor.Page;
import org.spider.filter.IFilter;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2015-8-29.
 * <p>布隆过滤器.
 *
 * @author dolphineor
 */
public class BloomFilter implements IFilter {

    /**
     * 需要插入的数据量.
     */
    private int expectedInsertions;

    /**
     * 精准度.
     */
    private double fpp;

    /**
     * 当前数据量.
     */
    private AtomicInteger counter;

    private final com.google.common.hash.BloomFilter<CharSequence> bloomFilter;

    public BloomFilter() {
        this(1000, 0.01);
    }

    public BloomFilter(int expectedInsertions) {
        this(expectedInsertions, 0.01);
    }

    public BloomFilter(int expectedInsertions, double fpp) {
        this.expectedInsertions = expectedInsertions;
        this.fpp = fpp;
        this.bloomFilter = buildBloomFilter();
    }

    protected com.google.common.hash.BloomFilter<CharSequence> buildBloomFilter() {
        counter = new AtomicInteger(0);
        return com.google.common.hash.BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions, fpp);
    }

    @Override
    public float similar(Page page) {
        float similarValue = 1;
        boolean isDuplicate = bloomFilter.mightContain(page.getUrl());
        if (!isDuplicate) {
            bloomFilter.put(page.getUrl());
            counter.incrementAndGet();
            similarValue = 0;
        }
        return similarValue;
    }
}
