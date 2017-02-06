package com.nottingham.cs.tung.g54idp.util;

import com.google.android.gms.maps.model.LatLng;
import com.nottingham.cs.tung.g54idp.data.RoomDataItem;
import com.nottingham.cs.tung.g54idp.data.RoomDataMenu;
import com.nottingham.cs.tung.g54idp.data.RoomDataSubMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tung on 17/07/2016.
 */
//this class initialize basic info of all computer room
//also organize them into which menu contains which item
//and store them into a list which can be retrieved
public class RoomDataHelper {
    private List<RoomDataMenu> menuList;
    private List<RoomDataSubMenu> subMenuList;
    private List<RoomDataItem> itemList;
    public RoomDataHelper(){
        menuList = new ArrayList<>();
        subMenuList = new ArrayList<>();
        itemList = new ArrayList<>();

        RoomDataMenu menu = new RoomDataMenu("Jubilee Campus");
            RoomDataSubMenu subMenu =new RoomDataSubMenu("Business School South", new LatLng(52.951795, -1.186421));
                RoomDataItem item = new RoomDataItem(1,"Business Library, Business School South");
                itemList.add(item);
                subMenu.addOptionItem(item);
            subMenuList.add(subMenu);
            menu.addOptionItem(subMenu);

            subMenu =new RoomDataSubMenu("Dearing Bulding", new LatLng(52.952450, -1.187170));
                item = new RoomDataItem(2,"A37 Computer Teaching Room, Dearing Building, JC");
                itemList.add(item);
                subMenu.addOptionItem(item);
            subMenuList.add(subMenu);
            menu.addOptionItem(subMenu);

            subMenu = new RoomDataSubMenu("Djanogly Learning Resource Centre", new LatLng(52.953728, -1.188341));
                item = new RoomDataItem(3,"Library Ramp, DLRC");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(4,"Computer Room, DLRC");
                itemList.add(item);
                subMenu.addOptionItem(item);
            subMenuList.add(subMenu);
            menu.addOptionItem(subMenu);

        //menu.addOptionItem(new RoomDataSubMenu("Exchange Building", new LatLng(52.953935, -1.187962)));
        //menu.addOptionItem(new RoomDataSubMenu("Si Yuan Centre of Contemporary", new LatLng(52.950870, -1.186112)));
        //menu.addOptionItem(new RoomDataSubMenu("Yang Fujia Building", new LatLng(52.952191, -1.185122)));
        menuList.add(menu);


        menu = new RoomDataMenu("Queens Medical Centre");

            subMenu = new RoomDataSubMenu("Greenfield Medical Library", new LatLng(52.943689, -1.186030));
                item = new RoomDataItem(5,"Greenfield Silent Study Area");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(6,"Greenfield Middle Computer Area");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(7,"Greenfield Learning Hub Computer Area");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(8,"Greenfield Back Computer Area");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(9,"A36 Computer Training Room, Greenfield Medical Library");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(10,"A32 Computer Room, Greenfield Medical Library");
                itemList.add(item);
                subMenu.addOptionItem(item);
            subMenuList.add(subMenu);
            menu.addOptionItem(subMenu);

            subMenu = new RoomDataSubMenu("Medical School", new LatLng(52.942664, -1.185625));
                item = new RoomDataItem(11,"Medical School Foyer");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(12,"C77 Computer Teaching Room, Medical School");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(13,"A18 Computer Teaching Room, Medical School");
                itemList.add(item);
                subMenu.addOptionItem(item);
            subMenuList.add(subMenu);
            menu.addOptionItem(subMenu);
        menuList.add(menu);

//------------------------

        menu = new RoomDataMenu("Sutton Bonington");

            subMenu = new RoomDataSubMenu("Gateway Building", new LatLng(52.829302, -1.249469));
                item = new RoomDataItem(14,"Biosciences Computer Room");
                itemList.add(item);
                subMenu.addOptionItem(item);
            subMenuList.add(subMenu);
            menu.addOptionItem(subMenu);

            subMenu = new RoomDataSubMenu("Main Building", new LatLng(52.831537, -1.250841));
                item = new RoomDataItem(15,"B10");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(16,"B09");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(17,"B08");
                itemList.add(item);
                subMenu.addOptionItem(item);

                item = new RoomDataItem(18,"B05");
                itemList.add(item);
                subMenu.addOptionItem(item);
            subMenuList.add(subMenu);
            menu.addOptionItem(subMenu);
        menuList.add(menu);

 //--------------------
/*
        menu = new RoomDataMenu("University Park");

        subMenu = new RoomDataSubMenu("Coates Building", new LatLng(52.940984, -1.189346));
        item = new RoomDataItem(19,"C20 Computer Teaching Room, Coates Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(20,"C19 Computer Teaching Room, Coates Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Engineering and Science Learning Centre", new LatLng(52.941530, -1.189133));
        item = new RoomDataItem(21,"C13 Computer Teching Room, Engineering and Science Learning Centre");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Engineering and Science Library", new LatLng(52.940993, -1.190936));
        item = new RoomDataItem(22,"F Floor Postgraduate Work Stations, Engineering and Science Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(23,"E Floor short stay PCs, Engineering and Science Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(24,"D Floor I/L Work Stations, Engineering and Science Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(25,"C Floor short stay PCs, Engineering and Science Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(26,"B Floor short stay PCs, Engineering and Science Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(27,"B Floor I/L Work Stations, Engineering and Science Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(28,"A Floor short stay PCs, Engineering and Science Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Level 1, Hallward Library", new LatLng(52.938908, -1.197209));
        item = new RoomDataItem(29,"Translation Suite 106, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(30,"Lvl 1 Short Stay, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(31,"Lvl 1 I/L Work Stations, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(32,"Computer Training Room 101, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Level 2, Hallward Library", new LatLng(52.938908, -1.197209));
        item = new RoomDataItem(33,"Lvl 2 Short Stay, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(34,"Lvl 2 I/L Work Stations, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(35,"Cafe, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Level 3, Hallward Library", new LatLng(52.938908, -1.197209));
        item = new RoomDataItem(36,"Lvl 3 Short Stay, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(37,"Lvl 3 Postgrad Study Carrel, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(38,"Lvl 3 I/L Work Stations, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Level 4, Hallward Library", new LatLng(52.938908, -1.197209));
        item = new RoomDataItem(39,"Lvl 4 Short Stay, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(40,"Lvl 4 Postgrad Study Carrel, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(41,"Lvl 4 I/L Work Stations, Hallward Library");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Humanities Building", new LatLng(52.936499, -1.204703));
        item = new RoomDataItem(42,"Humanities Atrium, Humanities Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(43,"A17 Computer Teaching Room, Humanities Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Law and Social Sciences Building", new LatLng(52.938495, -1.198277));
        item = new RoomDataItem(44,"Cafe, Law and Social Sciences Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(45,"A25 Computer Teaching Room, Law and Social Sciences Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Lenton Grove", new LatLng(52.935766, -1.203776));
        item = new RoomDataItem(46,"A17 Computer Teaching Room, Lenton Grove");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Life Sciences Building", new LatLng(52.940400, -1.190475));
        item = new RoomDataItem(47,"Cafe, Life Sciences Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(48,"A81 Computer Teaching Room, Life Sciences Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Mathematical Sciences", new LatLng(52.940303, -1.192645));
        item = new RoomDataItem(49,"A08 Computer Room, Mathematical Sciences Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Pope Building", new LatLng(52.941191, -1.189689));
        item = new RoomDataItem(50,"Telford Exhibition Hall, Pope Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(51,"A26 Computer Teaching Room, Pope Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(52,"A25 Language Lab, Pope Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(53,"A24 Computer Teaching Room, Pope Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(54,"A23 Language Lab, Pope Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(55,"A16 Computer Teaching Room, Pope Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(56,"A15 Computer Teaching Room, Pope Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Portland Building", new LatLng(52.938178, -1.194281));
        item = new RoomDataItem(57,"C Floor near Atrium, Portland Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(58,"B Floor near SSC, Portland Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(59,"B Floor inside SSC, Portland Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Sir Clive Granger", new LatLng(52.939948, -1.194307));
        item = new RoomDataItem(60,"B29 David Ebdon Lab, Sir Clive Granger");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(61,"A63 UG Common Room, Sir Clive Granger");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        subMenu = new RoomDataSubMenu("Trent Building", new LatLng(52.936765, -1.196032));
        item = new RoomDataItem(62,"LG27 Public Computer Room, Trent Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(63,"LG25 Public Computer Room, Trent Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(64,"English Undergraduate Common Room, Trent Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        item = new RoomDataItem(65,"C66 Self Access Centre, Trent Building");
        itemList.add(item);
        subMenu.addOptionItem(item);
        subMenuList.add(subMenu);
        menu.addOptionItem(subMenu);

        menuList.add(menu);
*/
//----------------------------

        //menu = new RoomDataMenu("Derby Medical School");
        //menu.addOptionItem(new RoomDataSubMenu("Derby Medical School C11", new LatLng(52.911473, -1.512539)));
        //menu.addOptionItem(new RoomDataSubMenu("Derby Medical School C8", new LatLng(52.911473, -1.512539)));
        //menu.addOptionItem(new RoomDataSubMenu("Derby Medical School Library", new LatLng(52.911473, -1.512539)));
        //menuList.add(menu);

    }

    public List<RoomDataMenu> getMenuList(){
        return menuList;
    }

    public List<RoomDataSubMenu> getSubMenuList(){
        return subMenuList;
    }

    public List<RoomDataItem> getItemList(){
        return itemList;
    }

}
