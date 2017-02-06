package com.nottingham.cs.tung.g54idp.data;

/**
 * Created by Tung on 17/07/2016.
 */
//This is general data class
//all class can add parent to itself
public class RoomDataGeneral {
    private String name;
    private RoomDataGeneral parent;

    public RoomDataGeneral(String name){
        this.name = name;
    }

    public void setName(String name){this.name = name;}
    public void addParent(RoomDataGeneral parent){
        this.parent = parent;
    }
    public RoomDataGeneral getParent(){
        return parent;
    }
    public String getName(){
        return name;
    }
}
