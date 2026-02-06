package org.example.model;
import java.io.Serializable;

public class MyFile implements Serializable {

    private String name;
    private String content;

    public MyFile(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}