package me.theyinspire.projects.infotheory.midterm.io.impl;

import me.theyinspire.projects.infotheory.midterm.io.Buffer;

import java.util.ArrayList;
import java.util.List;

public class ArrayBuffer<E> implements Buffer<E> {

    private final Object[] storage;
    private int head;
    private int size;

    public ArrayBuffer(int capacity) {
        storage = new Object[capacity];
        head = 0;
        size = 0;
    }

    @Override
    public E get(final int index) {
        if (index >= size() || index < 0) {
            return null;
        }
        //noinspection unchecked
        return (E) storage[(head + index) % storage.length];
    }

    @Override
    public void add(final E item) {
        if (size == storage.length) {
            storage[head] = item;
            head ++;
            if (head >= storage.length) {
                head = 0;
            }
        } else {
            storage[size] = item;
            size ++;
        }
    }

    @Override
    public E shrink() {
        if (size > 0) {
            final E value = get(size - 1);
            size --;
            return value;
        }
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < storage.length; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<E> toList() {
        final List<E> list = new ArrayList<>(size());
        for (int i = 0; i < size(); i++) {
            list.add(get(i));
        }
        return list;
    }

}
