package com.example.bst;

public interface Map<K extends Comparable<K>, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    V get(K key);

    void put(K key, V val);

    void remove(K key);

    void clear();

    void preOrderTrav();

    void inOrderTrav();
}
