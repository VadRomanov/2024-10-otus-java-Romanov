package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private final List<HwListener<K, V>> listeners = new ArrayList<>();
    private final Map<K, V> cache = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        notify(key, value, "put");
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notify(key, value, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notify(key, value, "get");
        return value;
    }

    @Override
    public List<V> getAll() {
        var values = cache.values().stream().toList();
        values.forEach(value -> notify(null, value, "getAll"));
        return values;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notify(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
