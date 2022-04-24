package com.itis_4440.npcreator;

import java.io.Serializable;
import java.util.ArrayList;

public class Description implements Serializable, Comparable<Description> {
    String name, circumstances, childhood, deity, occupation, monsterName;
    ArrayList<String> flaws, strengths;

    public Description() {
        //empty constructor
    }

    public Description(String name, String circumstances, String childhood
            , String deity, String occupation, String monsterName, ArrayList<String> flaws
            , ArrayList<String> strengths) {
        this.name = name;
        this.circumstances = circumstances;
        this.childhood = childhood;
        this.deity = deity;
        this.occupation = occupation;
        this.monsterName = monsterName;
        this.flaws = flaws;
        this.strengths = strengths;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCircumstances() {
        return circumstances;
    }

    public void setCircumstances(String circumstances) {
        this.circumstances = circumstances;
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

    public ArrayList<String> getFlaws() {
        return flaws;
    }

    public void setFlaws(ArrayList<String> flaws) {
        this.flaws = flaws;
    }

    public ArrayList<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(ArrayList<String> strengths) {
        this.strengths = strengths;
    }

    @Override
    public String toString() {
        return "Description{" +
                "name='" + name + '\'' +
                ", circumstances='" + circumstances + '\'' +
                ", childhood='" + childhood + '\'' +
                ", deity='" + deity + '\'' +
                ", occupation='" + occupation + '\'' +
                ", monsterName='" + monsterName + '\'' +
                ", flaws=" + flaws +
                ", strengths=" + strengths +
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
