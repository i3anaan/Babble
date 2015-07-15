package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramNode extends Node {
    private final List<ClazzNode> classes;

    /**
     * A map from variable names to Babble-constructable types. Global
     * 'variables' like true, false and nil are kept here. New instances
     * of the respective classes are created every time either 'variable'
     * is used.
     */
    private final Map<String, String> globals;

    public ProgramNode(List<ClazzNode> classes) {
        this(classes, new HashMap<>());
    }

    public ProgramNode(List<ClazzNode> classes, Map<String, String> globals) {
        this.classes = classes;
        this.globals = globals;
    }

    public List<ClazzNode> getClasses() {
        return classes;
    }

    public Map<String, String> getGlobals() {
        return globals;
    }

    @Override
    public String toString() {
        return "Program";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    
    public boolean addClazz(ClazzNode clazz) {
        return classes.add(clazz);
    }
    
    public ClazzNode getClazz(String name) {
        for (ClazzNode clazz : getClasses()) {
            if (clazz.getName().equals(name)) {
                return clazz;
            }
        }
        
        return null;
    }
}
