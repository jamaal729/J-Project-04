package com.teamtreehouse.blog.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {

    private String name;
    private String body;
    private String date;

    public Comment(String name, String body) {
        this.name = name;
        this.body = body;
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss"));
    }

    public String getName() { return name; }

    public String getBody() {return body;}

    public String getDate() {return date;}
}

