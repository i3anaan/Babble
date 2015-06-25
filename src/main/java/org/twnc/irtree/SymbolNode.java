package org.twnc.irtree;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SymbolNode extends ExprNode {
    private String id;
    
    public SymbolNode(String id) {
        this.id = id;
    }
    
    public String getID() {
        return id;
    }
    
    @Override
    public String toString() {
        return "#"+id;
    }

    @Override
    public Color getColor() {
        return new Color(46, 204, 113);
    }
}
