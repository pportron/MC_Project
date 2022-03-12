package com.mobilecomputing.mc_project;

import androidx.annotation.NonNull;

import java.util.Map;

public class MapCompat {
    public static <K, V> V getOrDefault(@NonNull Map<K, V> map, K key, V defaultValue) {
        V v;
        return (((v = map.get(key)) != null) || map.containsKey(key))
                ? v
                : defaultValue;
    }
}
