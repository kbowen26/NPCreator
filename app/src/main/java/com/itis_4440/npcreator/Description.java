package com.itis_4440.npcreator;

import java.io.Serializable;

public class Description implements Serializable, Comparable<Description> {
    String name, personality, backstory, goal, monsterName;

    public Description() {
        //empty constructor
    }

    public Description(String name, String personality, String backstory
            , String goal, String monsterName) {
        this.name = name;
        this.personality = personality;
        this.backstory = backstory;
        this.goal = goal;
        this.monsterName = monsterName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getBackstory() {
        return backstory;
    }

    public void setBackstory(String backstory) {
        this.backstory = backstory;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    @Override
    public String toString() {
        return "Description{" +
                "name='" + name + '\'' +
                ", personality='" + personality + '\'' +
                ", backstory='" + backstory + '\'' +
                ", goal='" + goal + '\'' +
                ", monsterName='" + monsterName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Description description) {
        if (this.name.compareTo(description.getName()) == 0) {
            return this.monsterName.compareTo(description.getMonsterName());
        } else {
            return this.name.compareTo(description.getName());
        }
    }
}
