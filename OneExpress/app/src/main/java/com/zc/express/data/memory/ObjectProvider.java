package com.zc.express.data.memory;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 内存缓存
 */
public class ObjectProvider {
    private static ObjectProvider instance;

    private HashMap<String, WeakReference<Object>> mObjectMap;
    private Cache mCache;

    synchronized public String set(Object obj){
        if (null == obj)
            return null;
        String key = obj.getClass().getName();
        set(key, obj);
        return key;
    }

    synchronized public void set(String key, Object obj){
        if (null == obj)
            return ;
        mCache.addObject(key, obj);
        mObjectMap.put(key, new WeakReference<>(obj));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    synchronized public <T> T get(String key){
        WeakReference<Object> ref = mObjectMap.get(key);
        mCache.checkExpired();
        if (null != ref){
            Object obj = ref.get();
            if (null != obj){
                return (T)obj;
            }
        }
        return null;
    }

    @Nullable
    synchronized public <T> T get(Class<T> cls){
        return get(cls.getName());
    }

    synchronized public void remove(String key){
        mCache.removeObject(key);
        mObjectMap.remove(key);
    }

    synchronized public <T> void remove(Class<T> cls){
        remove(cls.getName());
    }

    private class Cache{
        class CacheEntry{
            public Object mObject;
            public long mExpiredTime;

            public CacheEntry(Object object, long expiredTime) {
                mObject = object;
                mExpiredTime = expiredTime;
            }
        }

        HashMap<String, CacheEntry> mEntryHashMap = new HashMap<>();

        public void addObject(String key, Object object){
            addObject(key, object, 5000);
        }

        public void addObject(String key, Object object, int expiredMillis){
            checkExpired();
            mEntryHashMap.put(key, new CacheEntry(object, System.currentTimeMillis() + expiredMillis));
        }

        public Object getObject(String key){
            checkExpired();
            return mEntryHashMap.get(key).mObject;
        }

        public void removeObject(String key){
            mEntryHashMap.remove(key);
        }

        /**
         * 移除过期的对象
         */
        public void checkExpired(){
            long timeCur = System.currentTimeMillis();
            Set<String> keySet = new HashSet<>();
            keySet.addAll(mEntryHashMap.keySet());
            for (String key : keySet){
                if (mEntryHashMap.get(key).mExpiredTime > timeCur)
                    mEntryHashMap.remove(key);
            }
        }
    }

    public static ObjectProvider sharedInstance() {
        if (null == instance) {
            synchronized (ObjectProvider.class) {
                if (null == instance) {
                    instance = new ObjectProvider();
                }
            }
        }
        return instance;
    }

    private ObjectProvider() {
        mObjectMap = new HashMap<>();
        mCache = new Cache();
    }
}
