package me.theyinspire.projects.infotheory.midterm.node.impl;

import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeMatcher;
import me.theyinspire.projects.infotheory.midterm.node.NodeSelectionContext;

public class SuperTreeNodeMatcher implements NodeMatcher {

    private final int depth;

    public SuperTreeNodeMatcher(final int depth) {
        this.depth = depth;
    }

    @Override
    public boolean test(final NodeSelectionContext context, final Node node) {
        if (node.depth() == depth) {
            context.stop();
        }
        return node.depth() <= depth;
    }

}
