package com.itis_4440.npcreator;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Npc implements Serializable, Comparable<Npc> {
    @DocumentId
    private String id;
    private String name, type, index, creator, creator_id;
    private boolean publicNpc;
    private Description description;

    public Npc() {
        //empty constructor
    }

    public Npc(String id, String name, String type, String index
            , String creator, String creator_id, boolean publicNpc, Description description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.index = index;
        this.creator = creator;
        this.creator_id = creator_id;
        this.publicNpc = publicNpc;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public boolean isPublicNpc() {
        return publicNpc;
    }

    public void setPublicNpc(boolean publicNpc) {
        this.publicNpc = publicNpc;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Npc{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", index='" + index + '\'' +
                ", creator='" + creator + '\'' +
                ", creator_id='" + creator_id + '\'' +
                ", publicNpc=" + publicNpc +
                ", description=" + description +
                '}';
    }

    @Override
    public int compareTo(Npc npc) {
        return this.name.compareTo(npc.getName());
    }
}
