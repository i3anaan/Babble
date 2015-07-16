package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Node that represents an entire Babble program
 * (up until merging this equals a file).
 *
 */
public class ProgramNode extends Node {
    /** List of Babble classes defined in this program. */
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

    public ProgramNode() {
        this(new ArrayList<>());
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
    
    /**
     * Adds a Babble class to the program, no check is made whether there was already a class with the same name.
     * @param clazz Babble class to add.
     * @return true if succesfully added.
     */
    public boolean addClazz(ClazzNode clazz) {
        return classes.add(clazz);
    }
    
    /**
     * Looks for a class in this program with a given name.
     * @param name The class to look for.
     * @return The class with the requested name, or null if none found.
     */
    public ClazzNode getClazz(String name) {
        for (ClazzNode clazz : getClasses()) {
            if (clazz.getName().equals(name)) {
                return clazz;
            }
        }
        
        return null;
    }
}
