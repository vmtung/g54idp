package com.nottingham.cs.tung.g54idp.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Tung on 15/07/2016.
 */
//this class contains information about a step
//step is a unit of the directions from Google
public class DirStepData {
    private String instructions;
    private float distance;
    private int duration;
    private List<LatLng> pathList;

    public DirStepData(String instructions, float distance, int duration, List<LatLng>  pathList){
        this.instructions = instructions;
        this.distance = distance;
        this.duration = duration;
        this.pathList = pathList;
    }

    public String getInstructions() {return instructions;}
    public float getDistance() {return distance;}
    public int getDuration() {return duration;}
    public List<LatLng>  getPathList() {return pathList;}
}
