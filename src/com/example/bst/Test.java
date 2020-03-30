package com.example.bst;

public class Test {
    public static void main(String[] args) {
        test02();
    }

    static void test01() {
        Map<Integer, Integer> map = new AVLTreeMap<>();
        for (int x : new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 55, 56, 57, 58}) {
            map.put(x, 0);
        }
        map.preOrderTrav();
        map.inOrderTrav();
        System.out.println();

        for (int x : new int[]{70, 80}) {
            map.remove(x);
        }
        map.preOrderTrav();
        map.inOrderTrav();
    }

    static void test02() {
        Map<Integer, Integer> map = new RBTreeMap<>();
        for (int x : new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 55, 56, 57, 58}) {
            map.put(x, 0);
        }
        map.preOrderTrav();
        map.inOrderTrav();
        System.out.println();

        for (int x : new int[]{70, 80}) {
            map.remove(x);
        }
        map.preOrderTrav();
        map.inOrderTrav();
    }
}
