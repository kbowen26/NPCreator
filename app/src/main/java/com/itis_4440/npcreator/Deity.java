package com.itis_4440.npcreator;

public class Deity {
    String deity, description;

    public Deity() {
        //empty constructor
    }

    public Deity(String deity, String description) {
        this.deity = deity;
        this.description = description;
    }

    public String getDeity() {
        return deity;
    }

    public void setDeity(String deity) {
        this.deity = deity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return deity + ", " + description;
    }
}
