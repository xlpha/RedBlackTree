package com.example.bst;

public class BSTMap<K extends Comparable<K>, V> implements Map<K, V> {

    /**
     * 根节点指针
     */
    protected Node<K, V> root;

    /**
     * 节点个数
     */
    protected int size;

    /**
     * 在执行查找、插入、删除操作时，该指针会指向目标节点的父节点
     */
    protected Node<K, V> hot;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    protected Node<K, V> search(Node<K, V> root, K key) {
        Node<K, V> p = root;
        hot = null;
        while (p != null) {
            int cmp = key.compareTo(p.key);
            if (cmp < 0) {
                hot = p;
                p = p.left;
            } else if (cmp > 0) {
                hot = p;
                p = p.right;
            } else {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return search(root, key) != null;
    }

    @Override
    public V get(K key) {
        Node<K, V> p = search(root, key);
        if (p == null) return null;
        return p.val;
    }

    @Override
    public void put(K key, V val) {
        _put(key, val);
        for (Node<K, V> p = hot; p != null; p = p.parent) {
            updateNodeHeight(p);
        }
    }

    protected Node<K, V> _put(K key, V value) {
        hot = null;
        if (root == null) {
            root = new Node<>(key, value, null);
            size = 1;
            return root;
        }

        Node<K, V> s = search(root, key);
        if (s != null) {
            s.val = value;
            return s;
        }
        Node<K, V> temp = new Node<>(key, value, hot);
        if (key.compareTo(hot.key) < 0) {
            hot.left = temp;
        } else {
            hot.right = temp;
        }
        size += 1;
        return temp;
    }

    /**
     * 删除指定位置的节点，并返回实际删除节点的继承者位置
     */
    protected Node<K, V> removeAt(Node<K, V> x) {
        Node<K, V> w = x;       //实际被摘除的节点，初值同x
        Node<K, V> succ;        //实际被删除节点的接替者
        if (!x.hasLeftChild()) {
            succ = x.right;
        } else if (!x.hasRightChild()) {
            succ = x.left;
        } else {
            w = minimum(x.right);
            succ = w.right;
            x.key = w.key;
            x.val = w.val;
            Node<K, V> u = w.parent;
            if (u == x) {
                u.right = w.right;
            } else {
                u.left = w.right;
            }
        }
        hot = w.parent;
        if (succ != null) {
            succ.parent = w.parent;
        }
        w.clear();
        return succ;
    }

    @Override
    public void remove(K key) {
        Node<K, V> s = search(root, key);
        if (s == null) return;
        removeAt(s);
        size -= 1;
        for (Node<K, V> p = hot; p != null; p = p.parent) {
            updateNodeHeight(p);
        }
    }

    @Override
    public void clear() {
        deleteTree(root);
        size = 0;
    }

    @Override
    public void preOrderTrav() {
        preOrder(root);
        System.out.println();
    }

    protected void preOrder(Node<K, V> root) {
        if (root == null) return;
        System.out.print(root.key);
        System.out.print(" ");
        if (root.hasLeftChild()) preOrder(root.left);
        if (root.hasRightChild()) preOrder(root.right);
    }

    @Override
    public void inOrderTrav() {
        inOrder(root);
        System.out.println();
    }

    protected void inOrder(Node<K, V> root) {
        if (root == null) return;
        if (root.hasLeftChild()) inOrder(root.left);
        System.out.print(root.key);
        System.out.print(" ");
        if (root.hasRightChild()) inOrder(root.right);
    }

    protected void deleteTree(Node<K, V> p) {
        if (p == null) return;
        if (p.hasLeftChild()) deleteTree(p.left);
        if (p.hasRightChild()) deleteTree(p.right);
        deleteNode_noChild(p);
    }

    protected void deleteNode_noChild(Node<K, V> p) {
        assert p.hasNoChild();
        if (p == root) {
            p.clear();
            root = null;
            return;
        }
        if (p.isLeftChild()) {
            p.parent.left = null;
        } else {
            p.parent.right = null;
        }
        p.clear();
    }

    protected Node<K, V> deleteNode_oneChild(Node<K, V> x) {
        assert x.hasOneChild();
        if (x == root) {
            if (x.hasLeftChild()) {
                root = x.left;
            } else {
                root = x.right;
            }
            x.clear();
            return root;
        }
        Node<K, V> ret;
        if (x.hasLeftChild()) {
            ret = x.left;
            if (x.isLeftChild()) {
                x.parent.left = x.left;
            } else {
                x.parent.right = x.left;
            }
        } else {
            ret = x.right;
            if (x.isLeftChild()) {
                x.parent.left = x.right;
            } else {
                x.parent.right = x.right;
            }
        }
        x.clear();
        return ret;
    }

    protected Node<K, V> deleteNode_twoChild(Node<K, V> p) {
        assert p.hasTwoChild();
        Node<K, V> temp = minimum(p);
        p.key = temp.key;
        p.val = temp.val;
        if (temp.hasNoChild()) {
            deleteNode_noChild(temp);
            return null;
        } else {
            return deleteNode_oneChild(temp);
        }
    }

    protected Node<K, V> minimum(Node<K, V> p) {
        if (p == null) return null;
        while (p.hasLeftChild()) {
            p = p.left;
        }
        return p;
    }

    protected int getNodeHeight(Node<K, V> node) {
        if (node == null) return 0;
        return node.height;
    }

    protected void updateNodeHeight(Node<K, V> node) {
        if (node == null) return;
        node.height = Math.max(getNodeHeight(node.left), getNodeHeight(node.right)) + 1;
    }

}
