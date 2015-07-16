package org.twnc.irtree;

/** 
 * A Class that contains all the location information of the Babble code that is represented in a Node.
 * 
 */
public class Location {
    /** The line number. */
    private final int line;
    /** The line offset. */
    private final int offset;
    /** The file. */
    private final String filename;

    public Location(String filename, int line, int offset) {
        this.filename = filename;
        this.line = line;
        this.offset = offset;
    }

    public int getLine() {
        return line;
    }

    public int getOffset() {
        return offset;
    }

    public String getFilename() {
        return filename;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %d:%d", filename, line, offset);
    }
}
