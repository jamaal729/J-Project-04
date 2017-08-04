package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.ArrayList;
import java.util.List;

public class SimpleBlogDao implements BlogDao {

    private final List<BlogEntry> blogEntryList = new ArrayList<>();

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        boolean added = blogEntryList.add(blogEntry);
        return added;
    }

    @Override
    public BlogEntry editEntry(BlogEntry blogEntry, String title, String body, String slug) {
        blogEntry.setTitle(title);
        blogEntry.setBody(body);
        blogEntry.setSlug(slug);
        return blogEntry;
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return blogEntryList;
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {

        return blogEntryList.stream().filter(b -> b.getSlug()
                .equals(slug))
                .findFirst()
                .orElse(null);
    }
}

