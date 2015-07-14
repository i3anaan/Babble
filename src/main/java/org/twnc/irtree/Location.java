package org.twnc.irtree;

public class Location {
    private final int line;
    private final int offset;
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
