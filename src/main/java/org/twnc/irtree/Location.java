package org.twnc.irtree;

public class Location {
    private int line;
    private int offset;
    private String filename;

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
}
