package com.nottingham.cs.tung.g54idp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tung on 17/07/2016.
 */

public class RoomDataMenu extends RoomDataGeneral {
    private List<RoomDataGeneral> itemList;

    public RoomDataMenu(String name){
        super(name);
        itemList = new ArrayList<RoomDataGeneral>();
    }

    //Menu type class can add children to itself
    //and each child will add this class as the parent
    public void addOptionItem(RoomDataGeneral item){
        item.addParent(this);
        itemList.add(item);

    }

    public List<RoomDataGeneral> getOptionItemList(){
        return itemList;
    }
}
