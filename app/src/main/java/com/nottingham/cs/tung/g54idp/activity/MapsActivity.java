package com.nottingham.cs.tung.g54idp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nottingham.cs.tung.g54idp.R;
import com.nottingham.cs.tung.g54idp.data.RoomDataItem;
import com.nottingham.cs.tung.g54idp.data.RoomDataSubMenu;
import com.nottingham.cs.tung.g54idp.util.RoomDataHelper;
import com.nottingham.cs.tung.g54idp.fragment.InstructionFragment;
import com.nottingham.cs.tung.g54idp.fragment.InfoListFragment;
import com.nottingham.cs.tung.g54idp.data.DirStepData;
import com.nottingham.cs.tung.g54idp.util.DirectionsAsync;
import com.nottingham.cs.tung.g54idp.util.ServerAsync;
import com.nottingham.cs.tung.g54idp.fragment.StatusFragment;
import com.nottingham.cs.tung.g54idp.util.ServiceCheck;

import java.util.ArrayList;
import java.util.List;

/*Main activity class
* it connects all the classes and direct what to do at what time
* Other class can contact this activity to give information through a reference to the activity itself
* Fragments can contact this activity because this activity has implemented listener of that fragment
*
*
*/
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener, InstructionFragment.OnInstructionListFragmentInteractionListener, StatusFragment.OnStatusFragmentInteractionListener, InfoListFragment.OnOptionListFragmentInteractionListener {
    private LatLng userLoc;
    private LocationManager locationManager;
    private GoogleMap mMap;

    private RoomDataHelper roomDataHelper;

    private InstructionFragment instructionFragment;
    private InfoListFragment infoListFragment;
    private StatusFragment statusFragment;

    private FrameLayout statusLayoutFrame;
    private FrameLayout instructionLayoutFrame;

    private LatLng destination;
    private int stage = 0;
    private boolean firstTimeAutoFocus = true;

    private RoomDataItem selectedItem;
    private List<LatLng> focusLatLngList = new ArrayList<>();

    private Handler updateHandler;
    private Runnable updateRunnable;

    private MapsActivity mapsActivity = this;

    private int locReturnCount=0;

    private boolean firstLocFound = false;
    private boolean checkClear = false;

    private ServiceCheck serviceCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get all the layout frames
        statusLayoutFrame = (FrameLayout) findViewById(R.id.status_fragment_container);
        instructionLayoutFrame = (FrameLayout) findViewById(R.id.instruction_fragment_container);
        instructionLayoutFrame.setVisibility(View.GONE);
        //get location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Important: check if internet and GPS is turned on or not
        serviceCheck = new ServiceCheck(this);
        serviceCheck.doTotalCheck();
        //get a handler for updating the info regularyly, will be used later
        updateHandler = new Handler();

        //initialize all the needed fragments
        infoListFragment = new InfoListFragment();
        instructionFragment = new InstructionFragment();
        statusFragment = new StatusFragment();

        //put fragments into view
        getSupportFragmentManager().beginTransaction()
                .add(R.id.status_fragment_container, statusFragment).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.instruction_fragment_container, infoListFragment).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.instruction_fragment_container, instructionFragment).commit();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    //Throughout the activity, there will be 4 stage of operations
    //They were named with the number of stage for easy remember
    //First stage is to find directions from the user to all computer rooms location
    private void doFirstStage(){
        //get a new list of basic computer room information
        roomDataHelper = new RoomDataHelper();
        //set stage number and set status to notify user what we are doing
        stage =1;
        statusFragment.setDisplayMode(1);
        //start finding distance from the user to all
        findAllDistance();
    }

    //Second stage is after all distane and directions from the user to all computer room were found
    // -We will sort the list putting closest place first
    // -send 5 closest places to the server connector and excute the task
    private void doSecondStage(){
        //set stage number and set status to notify user what we are doing
        stage =2;
        statusFragment.setDisplayMode(2);

        //get a new list because we are going to change the order
        // but want to keep original order of the origin list
        List<RoomDataItem> newList = new ArrayList<>(roomDataHelper.getItemList());
        //sort the list by walk time
        newList = sortByWalkTime(newList);

        //init server async task
        //current activity is passed in so the async task can contact the server when done
        ServerAsync serverAsync = new ServerAsync(this);
        //set the list and start the process
        serverAsync.setChosenList(newList.subList(0,5));
        serverAsync.execute();

    }

    //Third stage of operation, after the information of 5 closest location is returned
    // -we remove the one that has no empty slot in the future
    // -put markers of those location on the map and focus the map
    // -put the suggested item on top of list
    // -put the list on to display so the user can choose
    private void doThirdStage(List<RoomDataItem> itemList){
        stage =3;
        //because we are moving the status fragment to another layout, extra steps must be taken
        //removing status fragment and execute it immediately
        getSupportFragmentManager().beginTransaction().remove(statusFragment).commit();
        getSupportFragmentManager().executePendingTransactions();
        //put status fragment into a new container
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.instruction_fragment_container, statusFragment).commit();
        getSupportFragmentManager().executePendingTransactions();
        instructionLayoutFrame.setVisibility(View.VISIBLE);
        //display buttons in the status fragment so that user can do actions
        statusFragment.setDisplayMode(3);
        //removing items that have no empty computer up to 15 minutes after arriving
        itemList = removeBusy(itemList);

        //set text saying how old is this info
        statusFragment.setTextGotInfo();

        RoomDataItem suggested = null;
        //get a list of all coordinates of all 5 closest location
        //mark them on the map
        //focus the map on those markers
        mMap.clear();
        focusLatLngList.clear();
        for (RoomDataItem item: itemList) {
            //since we are traversing the list, for convenience, we also get the item that is suggested by the server
            if (item.getSuggested()) {
                suggested = item;
            }
            //add the coordinate to list and add marker to map
            LatLng latLng =((RoomDataSubMenu)item.getParent()).getLatLng();
            focusLatLngList.add(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        focusMap(focusLatLngList);
        //Move the suggested item to top of the list
        itemList.remove(suggested);
        itemList.add(0,suggested);

        //put information of these computer room to be displayed for user to choose
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.status_fragment_container, infoListFragment).commit();
        getSupportFragmentManager().executePendingTransactions();
        infoListFragment.updateList(itemList);
    }

    //Fourth stage, after the user has chosen a destination
    // -save which item user has chosen and where it is
    // -send the decision to the server
    // -display directions and all related information on to the screen
    // -show path on the map
    private void doFourthStage(RoomDataItem item){
        //record the chosen item and its destination for later use
        selectedItem = item;
        destination = ((RoomDataSubMenu)item.getParent()).getLatLng();

        //send the decision to the server
        ServerAsync serverAsync = new ServerAsync(this);
        serverAsync.setSelect(item);
        serverAsync.execute();

        //we are moving the status fragment again
        //removing status fragment from current layout
        getSupportFragmentManager().beginTransaction().remove(statusFragment).commit();
        getSupportFragmentManager().executePendingTransactions();
        //put status fragment into another layout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.status_fragment_container, statusFragment).commit();
        getSupportFragmentManager().executePendingTransactions();

        //set stage and tell status fragment to display the information of the selected item
        stage =4;
        statusFragment.setDisplayMode(4);
        statusFragment.setItem(item);

        //update the information of the selected computer room for every 15 seconds
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                ServerAsync serverAsync = new ServerAsync(mapsActivity);
                List<RoomDataItem> list = new ArrayList<>();
                list.add(selectedItem);
                serverAsync.setChosenList(list);
                serverAsync.execute();
                updateHandler.postDelayed(this, 15000);
            }
        };
        updateHandler.post(updateRunnable);

        //find the direction to the computer room
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.instruction_fragment_container, instructionFragment).commit();
        findDirectionsTo(0, destination);
    }

    //remove the selected item
    //also tell the server to remove it
    private void removeSelected(){
        if(selectedItem!=null) {
            ServerAsync serverAsync = new ServerAsync(this);
            serverAsync.setRemove(selectedItem);
            serverAsync.execute();
            selectedItem = null;
        }
    }

    //sort item by the time it take to walk to that location
    private List<RoomDataItem> sortByWalkTime(List<RoomDataItem> list){
        int size = list.size();
        RoomDataItem temp = null;
        //simple bubble sort
        for(int i=0; i < size; i++){
            for(int j=1; j < (size-i); j++){

                if(((RoomDataSubMenu)list.get(j-1).getParent()).getTravelTime() > ((RoomDataSubMenu)list.get(j).getParent()).getTravelTime()){
                    //swap the item
                    temp = list.get(j-1);
                    list.set(j-1,list.get(j));
                    list.set(j, temp);
                }

            }
        }
        return list;
    }

    //Removing computer rooms that will not be available
    private List<RoomDataItem> removeBusy(List<RoomDataItem> list){
        for(int i=0; i < list.size(); i++){
            RoomDataItem item = list.get(i);
            if (item.getPredictList().size()!=0 && item.getFirstFreeSlot()==null) {
                list.remove(i);
                i--;
            }
        }
        return list;
    }

    //find distance to all computer room
    private void findAllDistance(){
        locReturnCount = 0;

        List<RoomDataSubMenu> subMenuList = roomDataHelper.getSubMenuList();
        for(int i=0; i<subMenuList.size(); i++) {
            findDirectionsTo(i, subMenuList.get(i).getLatLng());
        }
    }

    //The server async task will use this function to contact this activity after it has retrieved the informations
    // -Then, if stage 4, we update info of the list
    // -else start stage 3 to display the list
    public void serverAsyncPost(List<RoomDataItem> chosenList, int suggestId){
        //when suggestId = -1 means there are lack of info from the server response
        //hence that means problem with the server
        if (stage==4){
            if (suggestId!=-1){
                statusFragment.setItem(chosenList.get(0));
            }
        }
        else {
            //display pop up, notify problem with the server and stop the app
            if (suggestId == -1) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Server is facing problem or no PC room near you is available. Try again later.");
                dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                //display the list for user to make decision
                doThirdStage(chosenList);
            }
        }
    }

    //The direction async task uses this function to contact this activity after directions is found
    // one location of a time
    // -if stage 1, record the directions and save it to the appropriate item
    // -if stage 4, update the directions, show it to the user
    public void directionsAsyncPost(int index, List<DirStepData> stepList) {
        if (stage==1) {
            locReturnCount++;
            //save directions to the corresponding item
            if (stepList!=null &&stepList.size() > 0) {
                roomDataHelper.getSubMenuList().get(index).setStepList(stepList);
            }
            //if the number of returned directions = list size => last item
            // =>start second stage, analyse the list of item and request for more info from the server
            if (locReturnCount == roomDataHelper.getSubMenuList().size()) {
                doSecondStage();
            }
        } else if (stage==4) {
            // if stage 4, update the instruction list and map
            if(stepList!=null) {
                //update the info on the view
                instructionFragment.updateList(stepList);
                statusFragment.updateStepList(stepList);

                //draw the path on to the map
                PolylineOptions options =
                        new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                for (int i = 0; i < stepList.size(); i++) {
                    options.addAll(stepList.get(i).getPathList());
                }
                mMap.clear();
                mMap.addPolyline(options);

                focusLatLngList.clear();
                for (DirStepData step : stepList) {
                    focusLatLngList.addAll(step.getPathList());
                }

                //move mMap. to view path if this is the first time getting directions
                if (firstTimeAutoFocus) {
                    focusMap(focusLatLngList);
                    firstTimeAutoFocus = false;
                }
            }
        }

    }

    //focus map on the list of coordinates
    private void focusMap(List<LatLng> latLngList){
        //if (latLngList.size()==0) return;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngList) {
            builder.include(latLng);
        }
        builder.include(userLoc);
        LatLngBounds bounds = builder.build();

        int padding = 30; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //moving camera
        mMap.animateCamera(cu);

    }

    //find direction to a coordinates
    // the index is used to indicate which item this belong to and will be used to identify which item will store the returned info
    private void findDirectionsTo(int index, LatLng destination){
        DirectionsAsync direction = new DirectionsAsync(this);
        //setting up the required data for directions request
        direction.setCurrLocation(userLoc);
        direction.setDestination(destination);
        direction.setIndex(index);
        //execute the task
        direction.execute();
    }

    //On start, request update on user current location
    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100f, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100f, this);
        }
    }

    //stop the update when the app is not shown, save battery
    @Override
    protected void onStop() {
        super.onStop();
        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

    }

    //when location is changed, this class is called
    //We will record the user's current location and either start the first stage or update the directions, depending on which steps
    @Override
    public void onLocationChanged(Location location) {
        userLoc = new LatLng(location.getLatitude(), location.getLongitude());
        //if the app has just started up, start the first stage of operation
        if (stage ==0 && checkClear) {
            doFirstStage();
        }
        //if at stage 4, update the directions
        if (stage==4){
            findDirectionsTo(0, destination);
        }
    }

    public void checkFinished(){
        checkClear = true;
        if (firstLocFound)
            doFirstStage();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onListFragmentInteraction(DirStepData item) {

    }

    //reset variables and restart the fincing process
    private void reset(){
        updateHandler.removeCallbacks(updateRunnable);

        checkClear = false;
        stage = 0;

        firstLocFound = true;
        firstTimeAutoFocus = true;
        removeSelected();
        instructionLayoutFrame.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().remove(instructionFragment).commit();
        getSupportFragmentManager().beginTransaction().remove(statusFragment).commit();
        getSupportFragmentManager().beginTransaction().remove(infoListFragment).commit();
        getSupportFragmentManager().executePendingTransactions();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.status_fragment_container, statusFragment).commit();

        serviceCheck.doTotalCheck();
        mMap.clear();
    }

    //This is called when a button is pressed from the status fragment
    @Override
    public void onStatusFragmentInteraction(int mode) {
        // -mode 1 means to reset the whole process
        // -mode 2 means refocus the map
        // -mode 3 means cancel user's selection and quit
        if (mode==1) {
            //if in stage 3 then just reset
            //if stage 4, ask for confirmation first
            if(stage==3) reset();
            else if (stage==4) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Cancel selection and Restart?");

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        reset();
                    }
                });
                dialog.show();
            }

        }else if (mode==2){
            //refocus the map
            focusMap(focusLatLngList);
        } else if (mode==3){
            //pop up to confirm user's cancelation
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Cancel selection and quit?");
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });

            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    //remove selection and quit
                    updateHandler.removeCallbacks(updateRunnable);
                    removeSelected();
                    finish();
                }
            });
            dialog.show();
        }
    }

    //This function is called when an item is chosen by user
    @Override
    public void onOptionListFragmentInteraction(final RoomDataItem item) {
        //pop up for confirmation of chosen, if yes start the last stage
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Confirm destination: "+item.getName());
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                doFourthStage(item);
            }
        });
        dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });

        dialog.show();
    }
}
