package org.twnc.irtree.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.twnc.compile.exceptions.MainMethodNotFoundException;
import org.twnc.irtree.ASTVisitor;

public class ProgramNode extends Node {
    private List<ClazzNode> classes;

    public ProgramNode(List<ClazzNode> methods) {
        this.classes = methods;
    }

    public List<ClazzNode> getMethods() {
        return classes;
    }

    public ClazzNode getMain() {
        List<ClazzNode> list =  new ArrayList<>();
        for (ClazzNode c : classes) {
            for (MethodNode m : c.getMethods()) {
                if (m.getSelector().equals("main")) {
                    list.add(c);
                }
            }
        }
        if (!list.isEmpty()) {
            return list.get(new Random().nextInt(list.size()));
        }
        throw new MainMethodNotFoundException();
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<Node>(classes);
    }

    @Override
    public String toString() {
        return "Program";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
