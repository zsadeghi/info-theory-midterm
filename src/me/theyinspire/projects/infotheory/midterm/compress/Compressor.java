package me.theyinspire.projects.infotheory.midterm.compress;

import me.theyinspire.projects.infotheory.midterm.io.TokenReader;
import me.theyinspire.projects.infotheory.midterm.node.Node;

import java.io.IOException;

public interface Compressor {

    CompressedContent compress(Node root, TokenReader tokenReader, int depth) throws IOException;
}
