package me.theyinspire.projects.infotheory.midterm.compress.huffman;

import me.theyinspire.projects.infotheory.midterm.compress.CompressedContent;
import me.theyinspire.projects.infotheory.midterm.compress.impl.ImmutableCompressedContent;
import me.theyinspire.projects.infotheory.midterm.io.Buffer;
import me.theyinspire.projects.infotheory.midterm.io.TokenReader;
import me.theyinspire.projects.infotheory.midterm.io.impl.ArrayBuffer;
import me.theyinspire.projects.infotheory.midterm.io.impl.BitOutputStream;
import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeSelector;
import me.theyinspire.projects.infotheory.midterm.node.impl.DepthNodeMatcher;
import me.theyinspire.projects.infotheory.midterm.node.impl.RecursiveNodeSelector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HuffmanCompressor implements me.theyinspire.projects.infotheory.midterm.compress.Compressor {

    private final NodeSelector selector;

    public HuffmanCompressor() {
        this.selector = new RecursiveNodeSelector();
    }

    @Override
    public CompressedContent compress(Node root, final TokenReader tokenReader, int depth) throws IOException {
        // First, select all nodes at the indicated depth.
        final List<Node> selection = selector.select(root, new DepthNodeMatcher(depth));
        // If no nodes were found at this depth, we simply quit.
        if (selection.isEmpty()) {
            return new ImmutableCompressedContent(root.frequency() * 8, 0, new byte[0]);
        }
        // Then, create a Huffman node for each of the corpus nodes.
        final List<HuffmanNode> nodes = selection.stream().map(HuffmanNode::new).collect(Collectors.toList());
        // We will continue building the tree until we have one root.
        while (nodes.size() > 1) {
            // First, sort by frequency in descending order
            nodes.sort(Comparator.comparingInt(HuffmanNode::frequency));
            // Then, choose the first two:
            final HuffmanNode first = nodes.remove(0);
            final HuffmanNode second = nodes.remove(0);
            // And combine them into one:
            final HuffmanNode combined = new HuffmanNode(first.frequency() + second.frequency())
                    .left(first)
                    .right(second);
            nodes.add(combined);
        }
        final HuffmanNode huffmanRoot = nodes.get(0);
        // Now we take that root as the basis for our encoding table.
        final List<HuffmanNode> leaves = leaves(huffmanRoot);
        // We will next construct the Huffman code table
        final Map<String, Integer> codes = new HashMap<>();
        for (HuffmanNode leaf : leaves) {
            codes.put(leaf.prefix(), leaf.code());
        }
        // Now, let's construct a stream which will contain the actual bytes.
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // First, we will write the code table.
        final ObjectOutputStream metadata = new ObjectOutputStream(stream);
        // We need to save the inverse of the mapping since the actual mapping does not help with decompression.
        final Map<Integer, String> inverse = codes.keySet()
                                                  .stream()
                                                  .collect(Collectors.toMap(codes::get, Function.identity()));
        metadata.writeObject(inverse);
        // Next, we will write the variable length codes using the derived bits.
        final BitOutputStream output = new BitOutputStream(stream);
        // This buffer will help us look for codes for the indicated tuples of tokens
        // as opposed to just tokens.
        final Buffer<String> buffer = new ArrayBuffer<>(depth);
        while (tokenReader.hasNext()) {
            final String token = tokenReader.next();
            buffer.add(token);
            if (buffer.size() < depth) {
                // We need at least "depth" tokens to be able to look up a code.
                continue;
            }
            // Construct the word associated with the items in the buffer.
            final String word = buffer.toList().stream().reduce((a, b) -> a + b).orElse("");
            // Get the code for that word.
            final Integer code = codes.get(word);
            // Find out how many bits the code is.
            final int length = bits(code);
            // Write out those bits.
            output.write(length, code);
            output.flush();
        }
        // Now we take that root as the basis for our encoding table.
        long compressed = leaves.stream()
                                .mapToLong(leaf -> bits(leaf.code()) * leaf.node().frequency())
                                .sum();
        final byte[] bytes = stream.toByteArray();
        return new ImmutableCompressedContent(root.frequency(), (long) Math.ceil(compressed / 16.), bytes);
    }

    private int bits(final Integer code) {
        return Integer.SIZE - Integer.numberOfLeadingZeros(code);
    }

    private static List<HuffmanNode> leaves(HuffmanNode root) {
        final List<HuffmanNode> selection = new LinkedList<>();
        leaves(root, selection);
        return selection;
    }

    private static void leaves(HuffmanNode root, List<HuffmanNode> selection) {
        if (root.left() == null && root.right() == null) {
            selection.add(root);
        }
        if (root.left() != null) {
            leaves(root.left(), selection);
        }
        if (root.right() != null) {
            leaves(root.right(), selection);
        }
    }

}
