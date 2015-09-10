package org.ec.filter.simhash;

import com.google.common.base.Strings;

/**
 * Created on 2015-08-29.
 * <p>检查指纹信息是否已经存在.
 *
 * @author dolphineor
 */
public class FingerPrintChecker {

    /**
     * 单一实例
     */
    public static final FingerPrintChecker INSTANCE = new FingerPrintChecker();

    /**
     * Berkeley的数据存储
     */
    private BerkeleyDataStore dbs = new BerkeleyDataStore();

    /**
     * 数据库名称
     */
    private String dbName = "FingerPrint";

    /**
     * Creates a new instance of FingerPrintChecker.
     */
    private FingerPrintChecker() {
        this.dbs.init(dbName);
    }

    /**
     * <p>检查指纹信息是否存在.
     *
     * @param fingerPrint the finger message
     * @return isExists
     */
    public boolean checkExist(String fingerPrint) {
        boolean resultFlag = false;
        String result = this.dbs.getFromStore(dbName, fingerPrint);
        if (Strings.isNullOrEmpty(result)) {
            this.dbs.putToStore(dbName, fingerPrint,
                    String.valueOf(System.currentTimeMillis()));
        } else {
            resultFlag = true;
        }

        return resultFlag;
    }

    @Override
    protected void finalize() throws Throwable {
        this.dbs.closeConnection();
        super.finalize();
    }
}