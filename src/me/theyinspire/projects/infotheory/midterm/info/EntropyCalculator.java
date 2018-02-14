package me.theyinspire.projects.infotheory.midterm.info;

import me.theyinspire.projects.infotheory.midterm.node.Node;

public interface EntropyCalculator {

    double calculate(Node root, int depth);

}
