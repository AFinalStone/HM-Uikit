package com.hm.iou.uikit.decoration.util;

/**
 * 缓存工具需要暴露的接口
 * @author syl
 * @time 2019/3/13 3:26 PM
 */

public interface CacheInterface<T> {

    /**
     * 加入缓存
     * @param position
     * @param t
     */
    void put(int position, T t);

    /**
     * 从缓存中获取
     * @param position
     * @return
     */
    T get(int position);

    /**
     * 移除
     * @param position
     */
    void remove(int position);

    /**
     * 清空缓存
     */
    void clean();

}
