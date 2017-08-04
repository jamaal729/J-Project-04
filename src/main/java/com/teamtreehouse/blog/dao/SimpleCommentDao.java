package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;

import java.util.List;

public class SimpleCommentDao implements CommentDao {

    @Override
    public boolean addComment(BlogEntry blogEntry, Comment comment) {
        List<Comment> commentList = blogEntry.getComments();
        boolean added = commentList.add(comment);
        return added;
    }
}

