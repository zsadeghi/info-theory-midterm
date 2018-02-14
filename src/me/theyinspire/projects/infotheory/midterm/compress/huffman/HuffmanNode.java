package me.theyinspire.projects.infotheory.midterm.compress.huffman;

import me.theyinspire.projects.infotheory.midterm.node.Node;

class HuffmanNode {

    private final Node node;
    private final int frequency;
    private final String prefix;
    private HuffmanNode left;
    private HuffmanNode right;
    private HuffmanNode parent;

    HuffmanNode(final int frequency) {
        this(null, frequency);
    }

    HuffmanNode(final Node node) {
        this(node, node.frequency());
    }

    private HuffmanNode(final Node node, final int frequency) {
        this.node = node;
        this.frequency = frequency;
        this.prefix = node == null ? null : prefix(node);
    }

    public Node node() {
        return node;
    }

    public int frequency() {
        return frequency;
    }

    public String prefix() {
        return prefix;
    }

    public HuffmanNode left() {
        return left;
    }

    public HuffmanNode left(final HuffmanNode left) {
        this.left = left.parent(this);
        return this;
    }

    public HuffmanNode right() {
        return right;
    }

    public HuffmanNode right(final HuffmanNode right) {
        this.right = right.parent(this);
        return this;
    }

    public HuffmanNode parent() {
        return parent;
    }

    public HuffmanNode parent(final HuffmanNode parent) {
        this.parent = parent;
        return this;
    }

    public int code() {
        if (parent() == null) {
            return 1;
        }
        int code = this == parent().left() ? 1 : 0;
        code = (parent().code() << 1) | code;
        return code;
    }

    private static String prefix(Node node) {
        if (node.parent() == null) {
            return "";
        } else {
            return prefix(node.parent()) + node.token();
        }
    }

    public HuffmanNode findByPrefix(String prefix) {
        if (prefix.equals(prefix())) {
            return this;
        }
        if (left() != null) {
            final HuffmanNode found = left().findByPrefix(prefix);
            if (found != null) {
                return found;
            }
        }
        if (right() != null) {
            final HuffmanNode found = right().findByPrefix(prefix);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

}
