package com.nottingham.cs.tung.g54idp.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by Tung on 17/07/2016.
 */
//This class is the type contains computer rooms directly as its children
//it has more data than the server's version
public class RoomDataSubMenu extends RoomDataMenu {
    //location of the building, list of direction to this place
    //first estimated arrival time
    private LatLng latLng;
    private List<DirStepData> stepList;
    private Date firstArriveTime;

    public RoomDataSubMenu(String name, LatLng latLng) {
        super(name);
        this.latLng = latLng;
    }
    public void setStepList(List<DirStepData> stepList) {
        this.stepList = stepList;
        firstArriveTime = new Date(System.currentTimeMillis()+getTravelTime()*1000);
    }

    public List<DirStepData> getStepList() {
        return stepList;
    }

    public Date getFirstArriveTime() {
        return firstArriveTime;
    }

    public int getTravelTime(){
        int travelTime = 0;

        for (DirStepData step:stepList){
            travelTime+=step.getDuration();
        }
        return travelTime;
    }

    public LatLng getLatLng(){
        return latLng;
    }
}
