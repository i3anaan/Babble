package org.twnc;

import java.util.HashMap;
import java.util.Map;

import org.twnc.irtree.nodes.VarDeclNode;

public class Scope extends HashMap<String, VarDeclNode>{
    
    public Scope() {
    }
    
    @Override
    public VarDeclNode put(String name, VarDeclNode decl) {
        decl.setOffset(this.size());        
        return super.put(name, decl);
    }
}
