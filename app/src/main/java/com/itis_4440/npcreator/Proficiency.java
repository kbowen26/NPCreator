package com.itis_4440.npcreator;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Proficiency implements Comparable<Proficiency> {
    private String index, name, url, type;
    private int value;

    String A = "Arrived at:";
    String E = "Error:";

    public Proficiency() {
        //empty constructor
    }

    public Proficiency(int value, JSONObject proficiency) throws JSONException {
        this.value = value;
        String name = proficiency.getString("name");

        String[] nameArray = name.split(" ");
        int arrayLength = nameArray.length - 1;
        String editedName = nameArray[arrayLength];
        if (nameArray[0].equalsIgnoreCase("skill:")) {
            this.type = "skill";
        } else if (nameArray[0].equalsIgnoreCase("saving")) {
            this.type = "saving";
        } else {
            this.type = "";
            Log.d(E, "Proficiency shouldn't get here, constructor");
        }
        this.name = editedName;
    }

    public String getName() {
        return name;
    }

    public void setName(JSONObject proficiency) throws JSONException {
        String name = proficiency.getString("name");

        String[] nameArray = name.split(" ");
        int arrayLength = nameArray.length - 1;
        String editedName = nameArray[arrayLength];
        if (nameArray[0].equalsIgnoreCase("skill:")) {
            this.type = "skill";
        } else if (nameArray[0].equalsIgnoreCase("saving")) {
            this.type = "saving";
        } else {
            this.type = "";
            Log.d(E, "Proficiency shouldn't get here, setName");
        }
        this.name = editedName;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " +" + value;
    }

    @Override
    public int compareTo(Proficiency proficiency) {
        return 0;
    }
}
