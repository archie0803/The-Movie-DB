package com.example.android.mymoviedb;

import java.util.ArrayList;

/**
 * Created by Archita on 03-08-2017.
 */

class Trailer {
    private int id;
    private ArrayList<TrailerResults> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<TrailerResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerResults> results) {
        this.results = results;
    }

    public static class TrailerResults{
        private String id;
        private String key;
        private String name;
        private String site;
        private String type;

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
