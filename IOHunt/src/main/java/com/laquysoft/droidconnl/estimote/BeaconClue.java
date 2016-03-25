package com.laquysoft.droidconnl.estimote;

public class BeaconClue {

    private String name;
    private String summary;
    private double temperature;

    public BeaconClue(String name, String summary) {
        this.name = name;
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
