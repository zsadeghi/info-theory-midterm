package me.theyinspire.projects.infotheory.midterm.io;

import java.util.List;

public interface Buffer<E> {

    /**
     * @param index the index
     * @return the item at indicated index or {@code null}
     */
    E get(int index);

    /**
     * Adds an item to the buffer, potentially dropping another from the head
     * @param item the item
     */
    void add(E item);

    /**
     * Drops the last element of the buffer.
     */
    E shrink();

    /**
     * Clears the buffer.
     */
    void clear();

    /**
     * @return number of items in the buffer
     */
    int size();

    /**
     * @return all of the items in the buffer
     */
    List<E> toList();

    /**
     * @return {@code true} if the buffer is empty.
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
