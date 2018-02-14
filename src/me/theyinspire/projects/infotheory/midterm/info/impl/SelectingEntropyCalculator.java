package me.theyinspire.projects.infotheory.midterm.info.impl;

import me.theyinspire.projects.infotheory.midterm.info.EntropyCalculator;
import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeSelector;
import me.theyinspire.projects.infotheory.midterm.node.impl.DepthNodeMatcher;
import me.theyinspire.projects.infotheory.midterm.node.impl.RecursiveNodeSelector;

public class SelectingEntropyCalculator implements EntropyCalculator {

    private final NodeSelector selector;

    public SelectingEntropyCalculator() {
        selector = new RecursiveNodeSelector();
    }

    @Override
    public double calculate(final Node root, final int depth) {
        // First select all nodes at the given depth
        return selector.select(root, new DepthNodeMatcher(depth))
                        .parallelStream()
                       // Now calculate the P(X,Y) . -log_2(P(X|Y))
                        .mapToDouble(node -> node.globalProbability() * -Math.log(node.localProbability()))
                       // Get the sum of all statements
                        .sum();
    }

}
