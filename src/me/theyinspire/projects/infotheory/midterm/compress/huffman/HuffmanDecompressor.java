package me.theyinspire.projects.infotheory.midterm.compress.huffman;

import me.theyinspire.projects.infotheory.midterm.compress.Decompressor;
import me.theyinspire.projects.infotheory.midterm.io.impl.BitInputStream;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Writer;
import java.util.Map;

public class HuffmanDecompressor implements Decompressor {

    @Override
    public void decompress(byte[] compressed, Writer writer) throws Exception {
        final ByteArrayInputStream stream = new ByteArrayInputStream(compressed);
        final BitInputStream data = new BitInputStream(stream);
        // Read the code table from the input
        final ObjectInputStream metadata = new ObjectInputStream(stream);
        //noinspection unchecked
        final Map<Integer, String> codes = (Map<Integer, String>) metadata.readObject();
        int code = 0;
        while (true) {
            final int bit = data.readBits(1);
            if (bit == -1) {
                break;
            }
            code = code << 1 | bit;
            if (codes.containsKey(code)) {
                final String token = codes.get(code);
                writer.write(token);
                code = 0;
            }
        }
        stream.close();
    }

}
