package me.theyinspire.projects.infotheory.midterm.node.impl;

import me.theyinspire.projects.infotheory.midterm.node.NodeSelectionContext;

import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultNodeSelectionContext implements NodeSelectionContext {

    private final AtomicBoolean stopped;

    public DefaultNodeSelectionContext() {
        stopped = new AtomicBoolean(false);
    }

    @Override
    public boolean stopped() {
        return stopped.get();
    }

    @Override
    public void stop() {
        stopped.set(true);
    }

}
