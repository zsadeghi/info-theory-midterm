package me.theyinspire.projects.infotheory.midterm.node.impl;

import me.theyinspire.projects.infotheory.midterm.node.Node;

import java.util.*;

public class DefaultNode implements Node {

    private final Node parent;
    private final String token;
    private final int depth;
    private int frequency;
    private final Map<String, DefaultNode> children;

    public DefaultNode() {
        this(null, "#", 0);
    }

    private DefaultNode(final Node parent, final String token,
                        final int depth) {
        this.parent = parent;
        this.token = token;
        this.depth = depth;
        frequency = 0;
        children = new HashMap<>();
    }

    @Override
    public int frequency() {
        return frequency;
    }

    @Override
    public int depth() {
        return depth;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public void add(final String token) {
        children.putIfAbsent(token, child(token));
        if (parent() == null) {
            seen();
        }
        children.get(token).seen();
    }

    private void seen() {
        frequency++;
    }

    @Override
    public Node get(final String token) {
        return children.getOrDefault(token, child(token));
    }

    private DefaultNode child(String token) {
        return new DefaultNode(this, token, depth + 1);
    }

    @Override
    public Set<String> possibilities() {
        return children.keySet();
    }

    @Override
    public Node parent() {
        return parent;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("P(");
        builder.append(token);
        if (parent() != null) {
            builder.append("|");
            final List<String> parents = new LinkedList<>();
            Node node = parent();
            while (node != null) {
                parents.add(0, node.token());
                node = node.parent();
            }
            final String conditional = parents.stream().reduce((a, b) -> a + "," + b).orElse("");
            builder.append(conditional);
        }
        builder.append(")=");
        builder.append(localProbability());
        return builder.toString();
    }

}
