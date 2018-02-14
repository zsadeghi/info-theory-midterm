package me.theyinspire.projects.infotheory.midterm.compress.lz77;

import me.theyinspire.projects.infotheory.midterm.compress.Decompressor;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Lz78Decompressor implements Decompressor {

    @Override
    public void decompress(final byte[] compressed, final Writer writer) throws Exception {
        final ByteArrayInputStream stream = new ByteArrayInputStream(compressed);
        final ObjectInputStream data = new ObjectInputStream(stream);
        final int phrasesCount = data.readInt();
        final List<String> phrases = new ArrayList<>();
        for (int i = 0; i < phrasesCount; i++) {
            phrases.add((String) data.readObject());
        }
        //noinspection unchecked
        final List<Integer> indices = (List<Integer>) data.readObject();
        for (Integer index : indices) {
            writer.write(phrases.get(index));
        }
    }

}
