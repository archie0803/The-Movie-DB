package com.example.android.mymoviedb;

import java.util.ArrayList;

/**
 * Created by Archita on 03-08-2017.
 */

public class Review {

    private int id;
    private ArrayList<ReviewResult> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<ReviewResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewResult> results) {
        this.results = results;
    }

    public static class ReviewResult{
        private String author;
        private String content;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content.trim();
        }
    }
}
