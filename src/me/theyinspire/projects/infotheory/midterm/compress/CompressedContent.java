package me.theyinspire.projects.infotheory.midterm.compress;

public interface CompressedContent {

    long compressed();

    long original();

    default double ratio() {
        return (double) compressed() / original();
    }

    default double invertedRatio() {
        return 1 / ratio();
    }

    byte[] bytes();
}
