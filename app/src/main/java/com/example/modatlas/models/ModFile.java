package com.example.modatlas.models;

public class ModFile {
    private String filename;
    private String url;
    private long size;
    private Hashes hashes;

    public ModFile(String filename, String url, long size, Hashes hashes){
        this.filename = filename;
        this.url = url;
        this.size = size;
        this.hashes = hashes;
    }
    public String getFilename() { return filename; }
    public String getUrl() { return url; }
    public long getSize() { return size; }
    public Hashes getHashes() { return hashes; }
}

