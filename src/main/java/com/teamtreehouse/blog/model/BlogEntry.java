package com.teamtreehouse.blog.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.slugify.Slugify;

public class BlogEntry {
    private String title;
    private String body;
    private String date;
    private String slug;

    private List<Comment> commentList = new ArrayList<>();

    public BlogEntry(String title, String body) {
        this.title = title;
        this.body = body;
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy  hh-mm-ss"));

        try {
            Slugify slg = new Slugify();
            slug = slg.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() { return title; }

    public String getBody() { return body; }

    public String getDate() { return date; }

    public String getSlug() { return slug; }

    public List<Comment> getComments() { return commentList; }

    public void setTitle(String title) { this.title = title; }

    public void setBody(String body) { this.body = body;}

    public void setDate(String date) {this.date = date;}

    public void setSlug(String slug) {this.slug = slug;}

    public void setComments(List<Comment> commentList) { this.commentList = commentList; }

    public boolean addComment(Comment comment) { return commentList.add(comment);}
}

