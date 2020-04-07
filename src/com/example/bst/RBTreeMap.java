package com.example.bst;

public class RBTreeMap<K extends Comparable<K>, V> extends BSTMap<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;


    boolean isRed(Node<K, V> node) {
        return !isBlack(node);
    }

    boolean isBlack(Node<K, V> node) {
        return node == null || node.color == BLACK;
    }

    @Override
    public void put(K key, V val) {
        Node<K, V> x = _put(key, val);
        fixDoubleRed(x);
    }

    @Override
    public void remove(K key) {
        Node<K, V> x = search(root, key);
        if (x == null) return;
        Node<K, V> r = removeAt(x);
        size -= 1;
        if (root == null) return;
        if (hot == null) {
            root.color = BLACK;
            updateNodeHeight(root);
            return;
        }
        if (!needUpdateHeight(hot)) return;
        if (isRed(r)) {
            r.color = BLACK;
            r.height++;
            return;
        }
        fixDoubleBlack(r);
    }

    private Node<K, V> uncle(Node<K, V> x) {
        Node<K, V> p = x.parent;
        Node<K, V> g = p.parent;
        if (p.isLeftChild()) return g.right;
        else return g.left;
    }

    /**
     * 修复由于新插入节点x，导致可能出现父子节点同为红色的情况，称为双红缺陷
     *
     * @param x 新插入的节点，当前必为红节点
     */
    private void fixDoubleRed(Node<K, V> x) {
        if (x == root) {
            root.color = BLACK;
            root.height++;
            return;
        }
        Node<K, V> p = x.parent;
        if (isBlack(p)) return;     //父节点为黑色，无需修复
        Node<K, V> g = p.parent;    //父节点为红，祖父节点必定存在且为黑色
        Node<K, V> u = p.isLeftChild() ? g.right : g.left;

        if (isBlack(u)) {           //RR-1情况
            if (x.isLeftChild() == p.isLeftChild()) {   // x、p同侧
                p.color = BLACK;
            } else {                                    // x、p异侧
                x.color = BLACK;
            }
            g.color = RED;
            fixAndUpdateNodeHeight(x);
        } else {                    //RR-2情况
            p.color = BLACK;
            p.height++;
            u.color = BLACK;
            u.height++;
            if (g != root) {
                g.color = RED;
            }
            fixDoubleRed(g);
        }
    }

    /**
     * 修复由于无红孩子的黑节点x被删除后，导致出现的双黑缺陷
     *
     * @param r 被删除的节点的后继节电，必为黑色
     */
    private void fixDoubleBlack(Node<K, V> r) {
        Node<K, V> p = r != null ? r.parent : hot;
        if (p == null) return;
        Node<K, V> s = r.isLeftChild() ? p.right : p.left;
        if (isBlack(s)) {
            Node<K, V> t = null;
            if (isRed(s.right)) t = s.right;
            if (isRed(s.left)) t = s.left;
            if (t != null) {            //BB-1情况
                boolean oldColor = p.color;
                Node<K, V> b = t.parent.parent;
                fixAndUpdateNodeHeight(t);
                if (b.hasLeftChild()) {
                    b.left.color = BLACK;
                    updateNodeHeight(b.left);
                }
                if (b.hasRightChild()) {
                    b.right.color = BLACK;
                    updateNodeHeight(b.right);
                }
                b.color = oldColor;
                updateNodeHeight(b);
            } else {
                s.color = RED;
                s.height--;
                if (isRed(p)) {         //BB-2R情况
                    p.color = BLACK;
                } else {                //BB-2B情况
                    p.height--;
                    fixDoubleBlack(p);
                }
            }
        } else {                        //BB-3情况
            s.color = BLACK;
            p.color = RED;
            Node<K, V> t = s.isLeftChild() ? s.left : s.right;
            hot = p;
            fixAndUpdateNodeHeight(t);
            fixDoubleBlack(r);
        }
    }

    /**
     * 对x节点局部进行拓扑结构调整，并更新相关节点的高度
     *
     * @param x 待调整的节点
     */
    private void fixAndUpdateNodeHeight(Node<K, V> x) {
        Node<K, V> g = x.parent.parent;
        if (g == root) {
            root = _fixAndUpdateNodeHeight(x);
        } else if (g.isLeftChild()) {
            g.parent.left = _fixAndUpdateNodeHeight(x);
        } else {
            g.parent.right = _fixAndUpdateNodeHeight(x);
        }
    }

    private Node<K, V> _fixAndUpdateNodeHeight(Node<K, V> x) {
        //x-当前节点，p-父节点，g-祖父节点
        Node<K, V> p = x.parent;
        Node<K, V> g = p.parent;
        if (p.isLeftChild()) {
            if (x.isLeftChild()) {      // LL
                p.parent = g.parent;
                return connect34(x, p, g, x.left, x.right, p.right, g.right);
            } else {                    // LR
                x.parent = g.parent;
                return connect34(p, x, g, p.left, x.left, x.right, g.right);
            }
        } else {
            if (x.isLeftChild()) {      // RL
                x.parent = g.parent;
                return connect34(g, x, p, g.left, x.left, x.right, p.right);
            } else {                    // RR
                p.parent = g.parent;
                return connect34(g, p, x, g.left, p.left, x.left, x.right);
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

    private boolean needUpdateHeight(Node<K, V> node) {
        int lh = getNodeHeight(node.left);
        int rh = getNodeHeight(node.left);
        int vh = getNodeHeight(node);
        if (lh != rh) return false;
        if (isRed(node)) {
            return vh == lh;
        } else {
            return vh == lh + 1;
        }
    }

    @Override
    protected void preOrder(Node<K, V> root) {
        if (root == null) return;
        if (root.color == RED) System.out.print("*");
        System.out.print(root.key);
        System.out.print(" ");
        if (root.hasLeftChild()) preOrder(root.left);
        if (root.hasRightChild()) preOrder(root.right);
    }

    @Override
    protected void updateNodeHeight(Node<K, V> node) {
        node.height = Math.max(getNodeHeight(node.left), getNodeHeight(node.right));
        if (isBlack(node)) node.height += 1;
    }
}
