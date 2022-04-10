package com.itis_4440.npcreator;

public class Monster {
    String index, name, url;

    public Monster() {
    }

    public Monster(String index, String name, String url) {
        this.index = index;
        this.name = name;
        this.url = url;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "index='" + index + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
