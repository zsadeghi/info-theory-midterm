package me.theyinspire.projects.infotheory.midterm.compress.lz77;

import me.theyinspire.projects.infotheory.midterm.compress.CompressedContent;
import me.theyinspire.projects.infotheory.midterm.compress.Compressor;
import me.theyinspire.projects.infotheory.midterm.compress.impl.ImmutableCompressedContent;
import me.theyinspire.projects.infotheory.midterm.io.Buffer;
import me.theyinspire.projects.infotheory.midterm.io.TokenReader;
import me.theyinspire.projects.infotheory.midterm.io.impl.ArrayBuffer;
import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeSelector;
import me.theyinspire.projects.infotheory.midterm.node.impl.RecursiveNodeSelector;
import me.theyinspire.projects.infotheory.midterm.node.impl.SuperTreeNodeMatcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Lz78Compressor implements Compressor {

    private final NodeSelector selector;

    public Lz78Compressor() {
        selector = new RecursiveNodeSelector();
    }

    @Override
    public CompressedContent compress(final Node root, TokenReader reader, int depth) throws IOException {
        // Find the phrases that are up to "depth" tokens long.
        final List<Node> selection = selector.select(root, new SuperTreeNodeMatcher(depth));
        // We want to check on the longest nodes first.
        selection.sort(Comparator.comparingInt(Node::depth).reversed());
        // Create a listing of all phrases mapped to the selected nodes.
        final List<String> phrases = selection.stream().map(Lz78Compressor::prefix).collect(Collectors.toList());
        // We need to remember which of the phrases has actually been used.
        final BitSet used = new BitSet(selection.size());
        // Keep a buffer of everything read from the stream for lookahead.
        final Buffer<String> buffer = new ArrayBuffer<>(depth);
        // We need to remember the suffixes of the buffer that didn't work out for us.
        final List<String> reused = new LinkedList<>();
        final List<Integer> data = new LinkedList<>();
        while (true) {
            if (reader.hasNext()) {
                buffer.add(reader.next());
            }
            // If we haven't filled the buffer up until the depth limit and there are still tokens
            // to be read, keep filling the buffer up.
            if (buffer.size() < depth && reader.hasNext()) {
                continue;
            }
            // If there is no lookahead content and the stream is empty, we are done.
            if (buffer.isEmpty() && !reader.hasNext()) {
                break;
            }
            int index;
            while (true) {
                final String phrase = buffer.toList().stream().reduce((a, b) -> a + b).orElse(null);
                index = phrases.indexOf(phrase);
                if (index != -1) {
                    break;
                } else {
                    if (buffer.isEmpty()) {
                        System.out.println("Could not find any occurrences of phrase: " + phrase);
                        return null;
                    }
                    reused.add(0, buffer.shrink());
                }
            }
            // Mark the selected phrase as used.
            used.set(index, true);
            // Emit the selected phrase to the output.
            data.add(index);
            // Now, we need to transfer all lookahead content to the buffer for the next iteration.
            buffer.clear();
            for (String token : reused) {
                buffer.add(token);
            }
        }
        // At this point we have the data all compressed. We just need to encode the metadata at the
        // beginning.
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final ObjectOutputStream outputStream = new ObjectOutputStream(stream);
        outputStream.writeInt(phrases.size());
        for (int i = 0; i < phrases.size(); i++) {
            String phrase = phrases.get(i);
            if (used.get(i)) {
                outputStream.writeObject(phrase);
            } else {
                outputStream.writeObject(null);
            }
        }
        // Now, transfer the compressed byte to the end of the stream
        outputStream.writeObject(data);
        final byte[] bytes = stream.toByteArray();
        return new ImmutableCompressedContent(root.frequency(), bytes.length, bytes);
    }

    private static String prefix(Node node) {
        if (node.parent() == null) {
            return "";
        } else {
            return prefix(node.parent()) + node.token();
        }
    }

}
