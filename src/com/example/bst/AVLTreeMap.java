package com.example.bst;

public class AVLTreeMap<K extends Comparable<K>, V> extends BSTMap<K, V> {
    @Override
    public void put(K key, V val) {
        _put(key, val);
        for (Node<K, V> p = hot; p != null; p = p.parent) {
            if (avlBalanced(p)) {
                updateNodeHeight(p);
            } else {
                fixAndUpdateNodeHeight(p);
                break;
            }
        }
    }

    @Override
    public void remove(K key) {
        Node<K, V> s = search(root, key);
        if (s == null) return;
        removeAt(s);
        size -= 1;
        for (Node<K, V> p = hot; p != null; p = p.parent) {
            if (!avlBalanced(p)) {
                fixAndUpdateNodeHeight(p);
            } else {
                updateNodeHeight(p);
            }
        }
    }

    private void fixAndUpdateNodeHeight(Node<K, V> g) {
        if (g == root) {
            root = _fixAndUpdateNodeHeight(g);
        } else if (g.isLeftChild()) {
            g.parent.left = _fixAndUpdateNodeHeight(g);
        } else {
            g.parent.right = _fixAndUpdateNodeHeight(g);
        }
    }

    private Node<K, V> _fixAndUpdateNodeHeight(Node<K, V> g) {
        //v-当前节点，p-父节点，g-祖父节点
        Node<K, V> p = g.tallerChild();
        Node<K, V> v = p.tallerChild();
        if (p.isLeftChild()) {
            if (v.isLeftChild()) {    // LL
                p.parent = g.parent;
                return connect34(v, p, g, v.left, v.right, p.right, g.right);
            } else {    // LR
                v.parent = g.parent;
                return connect34(p, v, g, p.left, v.left, v.right, g.right);
            }
        } else {
            if (v.isLeftChild()) {    // RL
                v.parent = g.parent;
                return connect34(g, v, p, g.left, v.left, v.right, p.right);
            } else {    // RR
                p.parent = g.parent;
                return connect34(g, p, v, g.left, p.left, v.left, v.right);
            }
        }
    }

    private Node<K, V> connect34(Node<K, V> a, Node<K, V> b, Node<K, V> c,
                                 Node<K, V> T0, Node<K, V> T1, Node<K, V> T2, Node<K, V> T3) {
        a.left = T0;
        if (T0 != null) T0.parent = a;
        a.right = T1;
        if (T1 != null) T1.parent = a;
        updateNodeHeight(a);

        c.left = T2;
        if (T2 != null) T2.parent = c;
        c.right = T3;
        if (T3 != null) T3.parent = c;
        updateNodeHeight(c);

        b.left = a;
        a.parent = b;
        b.right = c;
        c.parent = b;
        updateNodeHeight(b);
        return b;
    }

    private int balanceFac(Node<K, V> node) {
        return getNodeHeight(node.left) - getNodeHeight(node.right);
    }

    private boolean avlBalanced(Node<K, V> node) {
        int fac = balanceFac(node);
        return -1 <= fac && fac <= 1;
    }

}
