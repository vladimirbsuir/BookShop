package com.example.bookshop.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Class to store cache. */
public class CacheUtil<K, V> {
    private final LinkedHashMap<K, V> cache;
    private final int maxSize;

    /** Constructor to create Cache object.
     *
     * @param maxSize max capacity of the cache
     */
    public CacheUtil(int maxSize) {
        this.cache = new LinkedHashMap<K, V>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
        this.maxSize = maxSize;
    }

    /** Function to get object from cache by key.
     *
     * @param key key of the object
     * @return object if exists, null otherwise
     */
    public V get(K key) {
        return cache.get(key);
    }

    /** Function to put object with specified key to the cache.
     *
     * @param key key of the object
     * @param value object
     */
    public void put(K key, V value) {
        cache.put(key, value);
    }

    /** Function to remove object with specified key.
     *
     * @param key key of the object
     */
    public void remove(K key) {
        cache.remove(key);
    }

    /** Function to clear cache. */
    public void clear() {
        cache.clear();
    }

    /** Function to check if specified key exists in the cache.
     *
     * @param key key of the object
     * @return true if exists, false otherwise
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /** Function to get objects with their keys as pairs.
     *
     * @return returns a Set view of the mappings contained in this cache
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return cache.entrySet();
    }
}
