package com.nottingham.cs.tung.g54idp.util;


import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.nottingham.cs.tung.g54idp.activity.MapsActivity;
import com.nottingham.cs.tung.g54idp.data.RoomDataItem;
import com.nottingham.cs.tung.g54idp.data.RoomDataSubMenu;
import com.nottingham.cs.tung.g54idp.data.SlotData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Tung on 14/07/2016.
 */
//This class handle connection to server
//and contact main activity after getting the response
public class ServerAsync extends AsyncTask<URL, Integer, Void> {
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private MapsActivity activity;
    private List<RoomDataItem> chosenList;
    private boolean remove=false;
    private RoomDataItem selectItem;

    private int suggestId = -1;
    private boolean getInfo = false;

    public ServerAsync(MapsActivity activity){
        this.activity = activity;
        chosenList = new ArrayList<>();
    }

    //set list of item to get info of
    public void setChosenList(List<RoomDataItem> chosenList){
        getInfo = true;
        this.chosenList.clear();
        this.chosenList.addAll(chosenList);
    }

    //set item that user has selected as destination
    public void setSelect(RoomDataItem item){
        getInfo = false;
        remove = false;
        selectItem = item;
    }

    //set item that user want remove
    public void setRemove(RoomDataItem item){
        getInfo = false;
        remove = true;
        selectItem = item;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(URL... urls) {
        try {
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

            //basic URL string, should be changed if server is on a different address
            String urlString = "http://192.168.0.7:8080/g54idp-server/RequestServlet?";

            //if this is a get info type of request
            if (getInfo) {
                //build the parameters string
                for (int i=0; i<chosenList.size(); i++) {
                    RoomDataItem item = chosenList.get(i);

                    urlString += "request=" + item.getId() + "&";
                    urlString += "arrive=" + (((RoomDataSubMenu) item.getParent()).getTravelTime()*1000 + System.currentTimeMillis());
                    if (i!=chosenList.size()-1) {
                        urlString += "&";
                    }
                }
                //send request and get the response
                GenericUrl url = new GenericUrl(urlString);
                HttpRequest request = requestFactory.buildGetRequest(url);
                request.setConnectTimeout(20000);
                request.setReadTimeout(20000);
                HttpResponse httpResponse = request.execute();

                //get the response info and parse them, store them into item objects
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }
                readJson(new JSONObject(builder.toString()));

            } else {
                //build parameters string
                if (remove) urlString +="remove="+selectItem.getId();
                else urlString +="select="+ selectItem.getId();
                urlString +="&time="+ ((RoomDataSubMenu) selectItem.getParent()).getFirstArriveTime().getTime();
                //send request to server
                GenericUrl url = new GenericUrl(urlString);
                HttpRequest request = requestFactory.buildGetRequest(url);
                HttpResponse httpResponse = request.execute();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //read json object and put data into items for returning
    private void readJson(JSONObject object) {

            try {
                suggestId = object.getInt("suggest");

                for (RoomDataItem item: chosenList){
                    item.clearPredict();
                    if (item.getId() == suggestId)
                        item.setSuggested(true);
                    //save data from json into the item
                    JSONObject roomData = object.getJSONObject("data"+item.getId());
                    int lastKnownFree = roomData.getInt("lastFree");
                    long lastKnowTime = roomData.getLong("lastFreeTime");
                    int requested = roomData.getInt("requested");
                    JSONArray predictArray = roomData.getJSONArray("predict");

                    item.setLastKnown(new SlotData(new Date(lastKnowTime), lastKnownFree));
                    item.setArriving(requested);
                    //save the prediction list into item
                    for (int i=0; i<predictArray.length(); i++){
                        JSONObject predictObject = predictArray.getJSONObject(i);

                        long time = predictObject.getLong("time");
                        int free = predictObject.getInt("free");

                        item.addPredictDataItem(new SlotData(new Date(time), free));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //return data item back to the server
        if (getInfo)
            activity.serverAsyncPost(chosenList, suggestId);
    }
}



