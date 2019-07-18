package com.hm.iou.uikit.decoration.util;

import android.util.LruCache;

/**
 * 缓存工具
 * @author syl
 * @time 2019/3/13 3:25 PM
 */

public class CacheUtil<T> implements CacheInterface<T> {

    /**
     * 是否缓存
     */
    private boolean mUseCache = true;

    /**
     * lru花痴女
     */
    private LruCache<Integer, T> mLruCache;

    public CacheUtil() {
        mLruCache = new LruCache<Integer, T>(2 * 1024 * 1024) {
            @Override
            protected void entryRemoved(boolean evicted, Integer key, T oldValue, T newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
    }

    /**
     * 是否使用用缓存
     *
     * @param b
     */
    public void isCacheable(boolean b) {
        mUseCache = b;
    }


    @Override
    public void put(int position, T t) {
        if (!mUseCache) {
            return;
        }
        mLruCache.put(position, t);
    }

    @Override
    public T get(int position) {
        if (!mUseCache) {
            return null;
        }
        return mLruCache.get(position);
    }

    @Override
    public void remove(int position) {
        if (!mUseCache) {
            return;
        }
        mLruCache.remove(position);
    }

    @Override
    public void clean() {
        mLruCache.evictAll();
    }
}
