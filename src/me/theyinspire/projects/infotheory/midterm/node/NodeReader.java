package me.theyinspire.projects.infotheory.midterm.node;

import me.theyinspire.projects.infotheory.midterm.io.TokenReader;

/**
 * This interface represents reading a single root node from a token-emitter
 */
public interface NodeReader {

    /**
     * Reads the root node of a tree (and therefore its entire tree structure) from a token reader.
     * @param reader the token reader.
     * @param depth  the depth to which the tree should be constructed.
     * @return the root of the tree associated with the provided tokens.
     */
    Node read(TokenReader reader, int depth);

}
