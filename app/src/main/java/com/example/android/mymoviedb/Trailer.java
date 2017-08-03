package com.example.android.mymoviedb;

import java.util.ArrayList;

/**
 * Created by Archita on 03-08-2017.
 */

class Trailer {
    private int id;
    private ArrayList<TrailerResult> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<TrailerResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerResult> results) {
        this.results = results;
    }

    public static class TrailerResult {
        private int id;
        private String key;
        private String name;

        public int getId() {

            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
