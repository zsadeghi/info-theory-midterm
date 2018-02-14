package me.theyinspire.projects.infotheory.midterm.node.impl;

import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeMatcher;
import me.theyinspire.projects.infotheory.midterm.node.NodeSelectionContext;

public class DepthNodeMatcher implements NodeMatcher {

    private final int depth;

    public DepthNodeMatcher(final int depth) {
        this.depth = depth;
    }

    @Override
    public boolean test(final NodeSelectionContext context, final Node node) {
        final boolean match = node.depth() == depth;
        if (match) {
            context.stop();
        }
        return match;
    }

}
