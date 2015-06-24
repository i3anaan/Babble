package org.twnc.irtree;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    public List<Node> getChildren() {
        return new ArrayList<Node>();
    }
}
