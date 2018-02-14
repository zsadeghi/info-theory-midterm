package me.theyinspire.projects.infotheory.midterm.console;

import me.theyinspire.projects.infotheory.midterm.compress.CompressedContent;
import me.theyinspire.projects.infotheory.midterm.compress.huffman.HuffmanCompressor;
import me.theyinspire.projects.infotheory.midterm.compress.huffman.HuffmanDecompressor;
import me.theyinspire.projects.infotheory.midterm.compress.lz77.Lz78Compressor;
import me.theyinspire.projects.infotheory.midterm.compress.lz77.Lz78Decompressor;
import me.theyinspire.projects.infotheory.midterm.info.EntropyCalculator;
import me.theyinspire.projects.infotheory.midterm.info.impl.SelectingEntropyCalculator;
import me.theyinspire.projects.infotheory.midterm.io.LineTokenizer;
import me.theyinspire.projects.infotheory.midterm.io.TokenReader;
import me.theyinspire.projects.infotheory.midterm.io.impl.CharacterLineTokenizer;
import me.theyinspire.projects.infotheory.midterm.io.impl.InputStreamTokenReader;
import me.theyinspire.projects.infotheory.midterm.node.Node;
import me.theyinspire.projects.infotheory.midterm.node.NodeReader;
import me.theyinspire.projects.infotheory.midterm.node.impl.DefaultNodeReader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) throws Exception {
        final Stopwatch stopwatch = new Stopwatch();
        final NodeReader nodeReader = new DefaultNodeReader();
        final LineTokenizer tokenizer = new CharacterLineTokenizer();
        final InputStream stream = Launcher.class.getClassLoader().getResourceAsStream("sample.txt");
        final TokenReader tokenReader = new InputStreamTokenReader(tokenizer, stream);
        stopwatch.start("startup");
        System.out.println("Reading from the source ...");
        final Node root = nodeReader.read(tokenReader, 20);
        final EntropyCalculator entropyCalculator = new SelectingEntropyCalculator();
        Node context = root;
        final Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Welcome!");
        System.out.println("Startup time: " + stopwatch.stop("startup") + "ms");
        System.out.println();
        final HuffmanCompressor huffmanCompressor = new HuffmanCompressor();
        final HuffmanDecompressor huffmanDecompressor = new HuffmanDecompressor();
        final Lz78Compressor lz78Compressor = new Lz78Compressor();
        final Lz78Decompressor lz78Decompressor = new Lz78Decompressor();
        CompressedContent compressedContent = null;
        do {
            System.out.print(prompt(context));
            final String input = scanner.nextLine().trim();
            if (input.equals("exit")) {
                break;
            }
            if (input.equals("ls")) {
                for (String token : context.possibilities()) {
                    System.out.println(token);
                }
            } else if (input.matches("cd\\s+\\S+")) {
                context = changeContext(context, input.split("\\s+")[1]);
            } else if (input.equals("cd")) {
                context = root;
            } else if (input.equals("frequency")) {
                System.out.println("Frequency: " + context.frequency());
            } else if (input.equals("global")) {
                System.out.println("Global probability: " + context.globalProbability());
            } else if (input.equals("local")) {
                System.out.println("Local probability: " + context.localProbability());
            } else if (input.equals("depth")) {
                System.out.println("Depth: " + context.depth());
            } else if (input.matches("entropy\\s+\\d+")) {
                final int depth = Integer.parseInt(input.split("\\s+")[1]);
                final double entropy = entropyCalculator.calculate(context, depth);
                System.out.println("Entropy: " + entropy);
            } else if (input.matches("compress\\s+\\S+\\s+\\d+")) {
                final String algorithm = input.split("\\s+")[1];
                final int depth = Integer.parseInt(input.split("\\s+")[2]);
                final LineTokenizer compressionTokenizer = new CharacterLineTokenizer();
                final InputStream compressionStream
                        = Launcher.class.getClassLoader().getResourceAsStream("sample.txt");
                final TokenReader compressionTokenReader = new InputStreamTokenReader(compressionTokenizer, compressionStream);
                stopwatch.start(algorithm);
                switch (algorithm) {
                    case "huffman":
                        compressedContent = huffmanCompressor.compress(root, compressionTokenReader, depth);
                        break;
                    case "lz78":
                        compressedContent = lz78Compressor.compress(root, compressionTokenReader, depth);
                        break;
                    default:
                        stopwatch.stop(algorithm);
                        System.out.println("Unknown algorithm: " + algorithm);
                        continue;
                }
                final long time = stopwatch.stop(algorithm);
                System.out.println("Original size: " + compressedContent.original() + " bytes");
                System.out.println("Compressed size: " + compressedContent.compressed() + " bytes");
                System.out.println("Compression ratio: " + compressedContent.ratio());
                System.out.println("Inverted compression ratio: " + compressedContent.invertedRatio());
                System.out.println("Compression time: " + time + "ms");
            } else if (input.matches("decompress\\s+\\S+")) {
                if (compressedContent == null) {
                    System.out.println("There is nothing to decompress");
                    continue;
                }
                final String algorithm = input.split("\\s+")[1];
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                stopwatch.start(algorithm);
                switch (algorithm) {
                    case "huffman":
                        huffmanDecompressor.decompress(compressedContent.bytes(), new OutputStreamWriter(out));
                        break;
                    case "lz78":
                        lz78Decompressor.decompress(compressedContent.bytes(), new OutputStreamWriter(out));
                        break;
                    default:
                        stopwatch.stop(algorithm);
                        System.out.println("");
                        break;
                }
                final long time = stopwatch.stop(algorithm);
                System.out.println("Decompression time: " + time + "ms");
            }
        } while (true);
    }

    private static Node changeContext(final Node context, final String path) {
        final Node node = findTargetContext(context, path);
        if (node == null) {
            System.out.println("Cannot switch to: " + path);
            return context;
        }
        return node;
    }

    private static Node findTargetContext(Node context, final String path) {
        final String[] split = path.split("/+", 2);
        final String prefix = split[0];
        if (prefix.equals("..")) {
            if (context.parent() == null) {
                return null;
            }
            context = context.parent();
        } else {
            context = context.get(prefix);
        }
        if (split.length == 1 || split[1].isEmpty()) {
            return context;
        }
        return findTargetContext(context, split[1]);
    }

    private static String prompt(final Node context) {
        final StringBuilder builder = new StringBuilder();
        Node current = context;
        while (current != null) {
            builder.insert(0, current.token() + "/");
            current = current.parent();
        }
        builder.append(" $ ");
        return builder.toString();
    }

}
