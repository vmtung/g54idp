package com.nottingham.cs.tung.g54idp.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tung on 17/07/2016.
 */

//Contains all info of a computer room
//predict free, ID, how many arriving, last known data, is it suggested by the server or not
public class RoomDataItem extends RoomDataGeneral {
    private List<SlotData> predictList;
    private int id;
    private int arriving;
    private SlotData lastKnown;
    private Boolean suggested;

    public RoomDataItem(int id, String name) {
        super(name);
        this.id = id;
        predictList = new ArrayList<>();
        arriving = 0;
        suggested = false;
    }

    public List<SlotData> getPredictList() {
        return predictList;
    }

    public void setLastKnown(SlotData lastKnown) {
        this.lastKnown = lastKnown;
    }

    public SlotData getLastKnown() {
        return lastKnown;
    }

    //find the first free prediction slot
    public SlotData getFirstFreeSlot(){
        for (SlotData slot: predictList){
            if (slot.getFree()>0)
                return slot;
        }
        return null;
    }

    //get first free prediction slot after a specified time
    public SlotData getFirstFreeSlotAfter(Date time){
        //SlotData before = null;
        for (SlotData slot: predictList){
            if (slot.getFree()>0 && slot.getTime().after(time))
                return slot;
        }
        return null;
    }

    public void setSuggested(Boolean suggested) {
        this.suggested = suggested;
    }

    public Boolean getSuggested() {
        return suggested;
    }

    public void setArriving(int arriving) {
        this.arriving = arriving;
    }

    public int getArriving() {
        return arriving;
    }

    public void setPredictList(List<SlotData> predictList) {
        this.predictList.clear();
        this.predictList.addAll(predictList);
    }

    public void clearPredict(){
        predictList.clear();
    }

    public void addPredictDataItem(SlotData predict){
        this.predictList.add(predict);
    }

    public int getId() {
        return id;
    }
}
