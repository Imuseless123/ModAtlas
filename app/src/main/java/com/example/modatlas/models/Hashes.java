package com.example.modatlas.models;

public class Hashes {
    private String sha1;
    private String sha512;
    public Hashes(String sha1, String sha512){
        this.sha1 = sha1;
        this.sha512 = sha512;
    }

    public String getSha1() { return sha1; }
    public String getSha512() { return sha512; }
}
