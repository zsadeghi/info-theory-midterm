package me.theyinspire.projects.infotheory.midterm.node;

import java.util.List;

/**
 * This interface is used to select a series of nodes.
 */
public interface NodeSelector {

    /**
     * Selects all nodes rooted at the given node that match the provided criteria
     * @param root the root of the selection
     * @param matcher the selection criteria
     * @return all nodes matching the provided criteria
     */
    List<Node> select(Node root, NodeMatcher matcher);

}
