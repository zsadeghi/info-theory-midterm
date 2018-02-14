package me.theyinspire.projects.infotheory.midterm.io;

import java.util.stream.Stream;

/**
 * This interface wraps another object and returns its tokens in order.
 */
public interface TokenReader {

    /**
     * @return the next token, or {@code null} if not more tokens are present
     */
    String next();

    /**
     * @return {@code true} if there are any more tokens to be read
     */
    boolean hasNext();

    /**
     * @return a stream of all the tokens in the wrapped object
     */
    default Stream<String> stream() {
        return Stream.iterate(null, o -> o != null || this.hasNext(), o -> this.next());
    }

}
