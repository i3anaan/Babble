package org.twnc.util;

import org.twnc.irtree.Node;

public class Graphvizivier {
    
    private static int nodeIndex;
    private static String labels;
    
    public static String nodeToGraph(Node node) {
        nodeIndex = 0;
        labels = "";
        String dotOutput = "digraph g{\n";
        String edges = nodeToGraph2(node);
        dotOutput += labels;
        dotOutput += edges;
        dotOutput += "}\n";
        return dotOutput;
    }
    
    private static String nodeToGraph2(Node node) {
        int parentIndex = nodeIndex;
        String dotOutput = "";
        labels += parentIndex + "[label=\""+node.toString()+"\"]\n";
        for (Node child : node.getChildren()) {
            nodeIndex++;
            int childIndex = nodeIndex;
            dotOutput += parentIndex + " -> " + childIndex + "\n";
            dotOutput += nodeToGraph2(child);
        }
        
        return dotOutput;
    }
}
