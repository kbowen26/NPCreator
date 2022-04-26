package com.itis_4440.npcreator;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Feature implements Serializable, Comparable<Feature> {
    private String name, desc;


    public Feature() {
        //empty constructor
    }

    public Feature(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Feature{" +
                ", header='" + name + '\'' +
                ", body='" + desc + '\'' +
                '}';
    }


    @Override
    public int compareTo(Feature feature) {
        return this.name.compareTo(feature.getName());
    }
}
