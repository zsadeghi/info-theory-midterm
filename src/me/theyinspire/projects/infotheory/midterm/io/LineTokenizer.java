package me.theyinspire.projects.infotheory.midterm.io;

/**
 * This interface allows for asking for all the tokens present in a given line.
 */
@FunctionalInterface
public interface LineTokenizer {

    /**
     * Tokenizes the line, assuming proper line-breaks.
     * @param line the line of text
     * @return all the tokens in the line.
     */
    String[] tokenize(String line);

}
