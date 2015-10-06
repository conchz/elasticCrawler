package com.github.dolphineor.filter.simhash;

import com.github.dolphineor.extractor.Page;
import com.github.dolphineor.util.Logs;
import com.github.dolphineor.filter.IFilter;
import org.nlpcn.commons.lang.finger.FingerprintService;

/**
 * Created on 2015-08-29.
 * <p>根据simhash的值对结果进行去重.
 *
 * @author dolphineor
 */
public class AnsjFingerPrintFilter extends Logs implements IFilter {

    /**
     * 日志信息存储
     */
    private static final BerkeleyDataStore bds = new BerkeleyDataStore();

    static {
        bds.init("FingerPrint");
    }


    @Override
    public float similar(Page page) {
        String fingerprint = new FingerprintService().fingerprint(page
                .getContent());
        logger.info("生成的指纹信息为：{}", fingerprint);

        // 判断指纹信息是否存在
        float resultNum = 0;
        boolean result = FingerPrintChecker.INSTANCE.checkExist(fingerprint);
        if (result) {
            resultNum = 1;
        }
        return resultNum;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}