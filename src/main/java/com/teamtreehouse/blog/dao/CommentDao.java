package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;

public interface CommentDao {

    boolean addComment(BlogEntry blogEntry, Comment comment);
}

