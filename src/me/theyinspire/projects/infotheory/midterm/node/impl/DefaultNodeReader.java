package me.theyinspire.projects.infotheory.midterm.node.impl;

import me.theyinspire.projects.infotheory.midterm.io.Buffer;
import me.theyinspire.projects.infotheory.midterm.io.TokenReader;
import me.theyinspire.projects.infotheory.midterm.io.impl.ArrayBuffer;
import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeReader;

public class DefaultNodeReader implements NodeReader {

    @Override
    public Node read(final TokenReader reader, final int depth) {
        final Node root = new DefaultNode();
        final Buffer<String> buffer = new ArrayBuffer<>(depth);
        while (reader.hasNext()) {
            final String word = reader.next();
            buffer.add(word);
            for (int i = buffer.size() - 1; i >= 0; i --) {
                Node context = root;
                for (int j = i; j < buffer.size() - 1; j++) {
                    context = context.get(buffer.get(j));
                }
                context.add(word);
            }
        }
        return root;
    }

}
