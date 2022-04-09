package com.itis_4440.npcreator;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Feature implements Serializable, Comparable<Feature> {
    @DocumentId
    private String id;
    private String header, body;

    public Feature() {
        //empty constructor
    }

    public Feature(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "id='" + id + '\'' +
                ", header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }


    @Override
    public int compareTo(Feature feature) {
        return this.header.compareTo(feature.getHeader());
    }
}
