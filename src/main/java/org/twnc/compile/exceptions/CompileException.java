package org.twnc.compile.exceptions;

public class CompileException extends RuntimeException {

    public CompileException() {
        // Allow default constructor.
    }
    
    public CompileException(String string) {
        super(string);
    }

}
