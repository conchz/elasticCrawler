package org.spider.scheduler.bdb;

/**
 * Created on 2015-08-29.
 * <p>数据本地存储的服务接口.
 *
 * @author dolphineor
 */
public interface KeyValueDataStore<K, V> {

    /**
     * 将数据放入到数据库中
     *
     * @param name  数据库名称
     * @param key   主键
     * @param value 值
     */
    void putToStore(String name, K key, V value);

    /**
     * 从存储中获取指定键对应的值
     *
     * @param name 数据库名称
     * @param key  主键
     */
    V getFromStore(String name, K key);

    /**
     * 从数据存储中删除指定的记录
     *
     * @param name 数据库名称
     * @param key  主键
     */
    void deleteFromStore(String name, K key);
}
