package org.twnc.irtree;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    public List<Node> getChildren() {
        return new ArrayList<Node>();
    }

    public Color getColor() {
        return new Color(234, 97, 83);
    }
}
