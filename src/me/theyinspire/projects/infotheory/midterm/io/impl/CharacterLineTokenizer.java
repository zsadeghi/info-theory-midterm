package me.theyinspire.projects.infotheory.midterm.io.impl;

import me.theyinspire.projects.infotheory.midterm.io.LineTokenizer;

/**
 * Assumes all characters in the line as separate tokens.
 */
public class CharacterLineTokenizer implements LineTokenizer {

    @Override
    public String[] tokenize(final String line) {
        // Remove all characters except a-z and make it all lower case.
        final String normalized = line.toLowerCase().replaceAll("[^a-z]", "");
        // Return each character as a separate token
        return normalized.chars().boxed()
                .map(i -> (char) (int) i)
                .map(String::valueOf)
                .toArray(String[]::new);
    }

}
