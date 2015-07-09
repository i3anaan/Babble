package org.twnc.irtree.nodes;

import org.twnc.irtree.ASTVisitor;
import org.twnc.irtree.Location;

import java.awt.Color;
import java.util.Optional;

public abstract class Node {
    public abstract <T> T accept(ASTVisitor<T> visitor);

    private Optional<Location> location = Optional.empty();

    public void setLocation(Location newLocation) {
        location = Optional.of(newLocation);
    }

    public void setLocation(String file, int line, int offset) {
        setLocation(new Location(file, line, offset));
    }

    public String getFilename() {
        return location.get().getFilename();
    }

    public int getLine() {
        return location.get().getLine();
    }

    public int getLineOffset() {
        return location.get().getOffset();
    }

    public Color getColor() {
        return new Color(234, 97, 83);
    }
}
