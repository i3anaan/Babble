package org.twnc.util;

import org.twnc.irtree.Node;

public class Graphvizivier {
    
    private int nodeIndex;
    
    public Graphvizivier() {
        nodeIndex = 0;
    }
    
    public String nodeToGraph(Node node) {
        int parentIndex = nodeIndex;
        String dotOutput = "";
                
        for (Node child : node.getChildren()) {
            nodeIndex++;
            dotOutput += parentIndex + " -> " + nodeIndex + "\n";
            dotOutput += nodeToGraph(child);
        }
        
        return dotOutput;
    }
}
