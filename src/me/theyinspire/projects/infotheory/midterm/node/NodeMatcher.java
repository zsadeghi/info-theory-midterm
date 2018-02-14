package me.theyinspire.projects.infotheory.midterm.node;

/**
 * This interface is used to decide whether or not a node matches a criteria.
 */
@FunctionalInterface
public interface NodeMatcher {

    /**
     * Tests to see if the node was a match
     * @param node the node
     * @return {@code true} if the node matched the query.
     */
    boolean test(NodeSelectionContext context, Node node);

}
