package me.theyinspire.projects.infotheory.midterm.console;

import java.util.HashMap;
import java.util.Map;

public class Stopwatch {

    private final Map<String, Long> watches;

    public Stopwatch() {
        watches = new HashMap<>();
    }

    public void start(String name) {
        watches.put(name, System.currentTimeMillis());
    }

    public long stop(String name) {
        return System.currentTimeMillis() - watches.remove(name);
    }

}
