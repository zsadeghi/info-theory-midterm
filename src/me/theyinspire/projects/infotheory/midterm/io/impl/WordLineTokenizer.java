package me.theyinspire.projects.infotheory.midterm.io.impl;

import me.theyinspire.projects.infotheory.midterm.io.LineTokenizer;

public class WordLineTokenizer implements LineTokenizer {

    @Override
    public String[] tokenize(final String line) {
        // Remove all characters except a-z and space and make it all lower case.
        final String normalized = line.trim()
                                      .toLowerCase()
                                      .replaceAll("[^a-z\\s]", " ")
                                      .replaceAll("\\s+", " ");
        return normalized.split("\\s+");
    }

}
