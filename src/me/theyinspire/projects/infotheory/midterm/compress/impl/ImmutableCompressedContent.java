package me.theyinspire.projects.infotheory.midterm.compress.impl;

import me.theyinspire.projects.infotheory.midterm.compress.CompressedContent;

public class ImmutableCompressedContent implements CompressedContent {

    private final long original;
    private final long compressed;
    private final byte[] bytes;

    public ImmutableCompressedContent(final long original, final long compressed, final byte[] bytes) {
        this.original = original;
        this.compressed = compressed;
        this.bytes = bytes;
    }

    @Override
    public long original() {
        return original;
    }

    @Override
    public long compressed() {
        return compressed;
    }

    @Override
    public byte[] bytes() {
        return bytes;
    }

}
