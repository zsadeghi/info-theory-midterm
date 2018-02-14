package me.theyinspire.projects.infotheory.midterm.node.impl;

import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeMatcher;
import me.theyinspire.projects.infotheory.midterm.node.NodeSelectionContext;
import me.theyinspire.projects.infotheory.midterm.node.NodeSelector;

import java.util.LinkedList;
import java.util.List;

public class RecursiveNodeSelector implements NodeSelector {

    @Override
    public List<Node> select(final Node root, final NodeMatcher matcher) {
        final List<Node> selection = new LinkedList<>();
        select(root, matcher, selection);
        return selection;
    }

    private void select(Node current, NodeMatcher criteria, List<Node> selection) {
        final NodeSelectionContext context = new DefaultNodeSelectionContext();
        if (criteria.test(context, current)) {
            selection.add(current);
        }
        if (context.stopped()) {
            return;
        }
        current.possibilities()
               .stream().sorted()
               .forEach(token -> {
                   select(current.get(token), criteria, selection);
               });
    }

}
