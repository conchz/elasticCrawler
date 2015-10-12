package org.spider.bdb;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.*;
import org.spider.downloader.HttpClientDownloader;
import org.spider.extractor.HtmlExtractor;
import org.spider.scheduler.Task;
import org.spider.util.MD5;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created on 2015-10-12.
 *
 * @author dolphineor
 */
public class BerkeleyDataStoreTest {

    /**
     * 数据库实例对象
     */
    private final Database myDatabase;

    /**
     * 数据库的环境信息
     */
    private final Environment myDbEnvironment;


    public BerkeleyDataStoreTest(String dbName) {
        System.out.println("打开数据库: " + dbName);
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(true);
        envConfig.setReadOnly(false);
        envConfig.setTxnTimeout(10000, TimeUnit.MILLISECONDS);
        envConfig.setLockTimeout(10000, TimeUnit.MILLISECONDS);
        File file = new File(dbName);
        if (!file.exists())
            file.mkdirs();

        this.myDbEnvironment = new Environment(file, envConfig);
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setTransactional(true);
        dbConfig.setReadOnly(false);
        this.myDatabase = myDbEnvironment.openDatabase(null, dbName, dbConfig);
    }

    public void test(Task task) {
        Transaction txn = myDbEnvironment.beginTransaction(null, null);

        ClassCatalog catalog = new StoredClassCatalog(myDatabase);
        EntryBinding<Task> dataBinding = new SerialBinding<>(catalog, Task.class);

        // WRITE
        DatabaseEntry theKey = new DatabaseEntry(
                new MD5.Builder()
                        .value(task.getUrl())
                        .salt(task.getCharset())
                        .build()
                        .getBytes(StandardCharsets.UTF_8));

        DatabaseEntry theData = new DatabaseEntry();
        dataBinding.objectToEntry(task, theData);

        myDatabase.put(txn, theKey, theData);
        txn.commit();
        txn.abort();

        // READ
        theData = new DatabaseEntry();
        myDatabase.get(null, theKey, theData, LockMode.DEFAULT);

        Task _task = dataBinding.entryToObject(theData);
        System.out.println();
    }

    public void findAll() {
        ClassCatalog catalog = new StoredClassCatalog(myDatabase);
        EntryBinding<Task> dataBinding = new SerialBinding<>(catalog, Task.class);

        Cursor cursor = myDatabase.openCursor(null, null);
        DatabaseEntry foundKey = new DatabaseEntry();
        DatabaseEntry foundData = new DatabaseEntry();

        while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            // getData() on the DatabaseEntry objects returns the byte array
            // held by that object. We use this to get a String value. If the
            // DatabaseEntry held a byte array representation of some other data
            // type (such as a complex object) then this operation would look
            // considerably different.
            String keyString = new String(foundKey.getData());
            Task data = dataBinding.entryToObject(foundData);

            System.out.println();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        BerkeleyDataStoreTest berkeleyData = new BerkeleyDataStoreTest("elastic_crawler");

        String k = "冬装";
        Task task = new Task();
        task.setCharset("GBK");
        k = encode(k, UTF_8.name());
        task.setUrl(String.format("http://search.jd.com/Search?keyword=%s&enc=utf-8", k));
        task.setDownloader(HttpClientDownloader.create());
        task.setExtractor(HtmlExtractor.create());

//        berkeleyData.test(task);
        berkeleyData.findAll();

    }

}
