package me.theyinspire.projects.infotheory.midterm.compress;

import java.io.Writer;

public interface Decompressor {

    void decompress(byte[] compressed, Writer writer) throws Exception;

}
