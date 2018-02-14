package me.theyinspire.projects.infotheory.midterm.node;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A node represents a given piece of information in a corpus of data.
 */
public interface Node {

    /**
     * @return the frequency of the token associated with this node.
     */
    int frequency();

    /**
     * @return the depth at which this node has been created, starting at zero.
     */
    int depth();

    /**
     * @return the token associated with this node.
     */
    String token();

    /**
     * Adds a new token as a child to this node.
     *
     * @param token the token.
     */
    void add(String token);

    /**
     * Gets the node associated with the queried token.
     *
     * @param token the token.
     * @return the associated node.
     */
    Node get(String token);

    /**
     * The probability that the given token appears after this node.
     *
     * @param token the token.
     * @return the probability ratio.
     */
    default double probability(String token) {
        return (double) get(token).frequency() / (double) frequency();
    }

    /**
     * @return the children of this node.
     */
    Set<String> possibilities();

    /**
     * @return the parent node for this node.
     */
    Node parent();

    /**
     * @return the probability of this node happening, given that the parent has already happened.
     * This is equivalent to:
     * <pre>
     *     P(this | parent, grand-parent, ...)
     * </pre>
     */
    default double localProbability() {
        return parent() == null ? 1 : parent().probability(token());
    }

    /**
     * @return the probability of this node happening among all other nodes.
     * This is equivalent to:
     * <pre>
     *     P(this, parent, grand-parent, ...)
     * </pre>
     */
    default double globalProbability() {
        return Stream.iterate(this, Objects::nonNull, Node::parent)
                     .mapToDouble(Node::localProbability)
                     .reduce(1, (a, b) -> a * b);
    }

}
