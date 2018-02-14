package me.theyinspire.projects.infotheory.midterm.io.impl;

import me.theyinspire.projects.infotheory.midterm.io.LineTokenizer;
import me.theyinspire.projects.infotheory.midterm.io.TokenReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Wraps an input stream and returns all of its tokens
 */
public class InputStreamTokenReader implements TokenReader {

    private final LineTokenizer tokenizer;
    private final BufferedReader reader;
    private String nextLine;
    private String[] currentLine;
    private int cursor;

    public InputStreamTokenReader(final LineTokenizer tokenizer,
                                  final InputStream stream) {
        this.tokenizer = tokenizer;
        reader = new BufferedReader(new InputStreamReader(stream));
        // At the beginning, there is no next line, current line doesn't have any tokens to offer,
        // and we are in need of an advancing.
        nextLine = null;
        currentLine = new String[0];
        cursor = 0;
        // Initialize the values, so that the first test of requirements is auto-fulfilled.
        advance();
    }

    private void advance() {
        try {
            // If there is a next line, tokenize it
            if (nextLine != null) {
                currentLine = tokenizer.tokenize(nextLine);
                cursor = 0;
                nextLine = null;
            }
            do {
                // Read a line.
                String current = reader.readLine();
                // If it is null, there is nothing else left in the file.
                if (current == null) {
                    return;
                }
                // See if this line is empty, if it is, try reading another line.
                if (current.trim().isEmpty()) {
                    continue;
                }
                // If this line wasn't empty, we can set it as the next line.
                nextLine = current;
                break;
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read another line", e);
        }
    }

    @Override
    public String next() {
        // If there aren't any more tokens, return null.
        if (!hasNext()) {
            return null;
        }
        // If we need to replenish the current line, move forward.
        if (cursor >= currentLine.length) {
            advance();
        }
        // Return the next token.
        return currentLine[cursor ++];
    }

    @Override
    public boolean hasNext() {
        // We have more if there is at least another line, or there are other tokens
        // to be read from the current line.
        return nextLine != null || currentLine != null && cursor < currentLine.length;
    }

}
