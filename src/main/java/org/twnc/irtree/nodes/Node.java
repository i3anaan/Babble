package org.twnc.irtree.nodes;

import org.twnc.Scope;
import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.Location;

import java.awt.Color;
import java.util.Optional;

public abstract class Node {
    public abstract void accept(ASTVisitor visitor);

    private Scope scope;
    private Location location;
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public void setLocation(String filename, int line, int lineOffset) {
        setLocation(new Location(filename, line, lineOffset));
    }
    
    public Location getLocation() {
        return location;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Color getColor() {
        return new Color(234, 97, 83);
    }
}
