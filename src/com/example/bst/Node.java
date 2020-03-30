package com.example.bst;

public class Node<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    K key;
    V val;
    Node<K, V> left, right, parent;
    int height;
    boolean color;

    Node(K key, V val, Node<K, V> parent) {
        this.key = key;
        this.val = val;
        this.parent = parent;
        this.height = 1;
        this.color = RED;
    }

    public void clear() {
        left = right = parent = null;
    }

    public boolean hasNoChild() {
        return left == null && right == null;
    }

    public boolean hasOneChild() {
        return left == null && right != null || left != null && right == null;
    }

    public boolean hasTwoChild() {
        return left != null && right != null;
    }

    public boolean hasLeftChild() {
        return left != null;
    }

    public boolean hasRightChild() {
        return right != null;
    }

    public boolean isLeftChild() {
        return parent != null && parent.left == this;
    }

    public boolean isRightChild() {
        return parent != null && parent.right == this;
    }

    public static int getHeight(Node node) {
        if (node == null) return 0;
        return node.height;
    }

    public Node<K, V> tallerChild() {
        int l = getHeight(left);
        int r = getHeight(right);
        if (l > r) return left;
        else if (l < r) return right;
        else if (isLeftChild()) return left;
        else return right;
    }
}
