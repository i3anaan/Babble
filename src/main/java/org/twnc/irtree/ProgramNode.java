package org.twnc.irtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.twnc.compile.exceptions.MainMethodNotFoundException;

public class ProgramNode extends Node {
    private List<MethodNode> methods;

    public ProgramNode(List<MethodNode> methods) {
        this.methods = methods;
    }

    public List<MethodNode> getMethods() {
        return methods;
    }

    public MethodNode getMain() {
        for (MethodNode m : methods) {
            if (m.getSelector().equals("main")) {
                return m;
            }
        }
        throw new MainMethodNotFoundException();
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<Node>(methods);
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
