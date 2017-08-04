package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.List;

public interface BlogDao {

    boolean addEntry(BlogEntry blogEntry);

    BlogEntry editEntry(BlogEntry blogEntry, String title, String body, String slug);

    List<BlogEntry> findAllEntries();

    BlogEntry findEntryBySlug(String slug);
}

