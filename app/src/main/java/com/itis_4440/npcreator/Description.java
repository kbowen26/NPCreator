package com.itis_4440.npcreator;

import java.io.Serializable;
import java.util.ArrayList;

public class Description implements Serializable, Comparable<Description> {
    String name, childhood, deity, occupation, monsterName
            , flaws, strengths, notes;

    public Description() {
        //empty constructor
    }

    public Description(String name, String childhood, String deity, String occupation
            , String monsterName, String flaws, String strengths, String notes) {
        this.name = name;
        this.childhood = childhood;
        this.deity = deity;
        this.occupation = occupation;
        this.monsterName = monsterName;
        this.flaws = flaws;
        this.strengths = strengths;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChildhood() {
        return childhood;
    }

    public void setChildhood(String childhood) {
        this.childhood = childhood;
    }

    public String getDeity() {
        return deity;
    }

    public void setDeity(String deity) {
        this.deity = deity;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public String getFlaws() {
        return flaws;
    }

    public void setFlaws(String flaws) {
        this.flaws = flaws;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Description{" +
                "name='" + name + '\'' +
                ", childhood='" + childhood + '\'' +
                ", deity='" + deity + '\'' +
                ", occupation='" + occupation + '\'' +
                ", monsterName='" + monsterName + '\'' +
                ", flaws='" + flaws + '\'' +
                ", strengths='" + strengths + '\'' +
                ", notes='" + notes + '\'' +
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
