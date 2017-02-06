package com.nottingham.cs.tung.g54idp.util;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.maps.android.PolyUtil;
import com.nottingham.cs.tung.g54idp.R;
import com.nottingham.cs.tung.g54idp.activity.MapsActivity;
import com.nottingham.cs.tung.g54idp.data.DirStepData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tung on 14/07/2016.
 */
//this class contact the Google Directions API and get the directins from the user to a location
//when result is in, it will contact the main activity
public class DirectionsAsync extends AsyncTask<URL, Integer, Void> {
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    private String key;
    private LatLng currLocation;
    private LatLng destination;
    private List<DirStepData> stepList;
    private MapsActivity activity;
    private int index = 0;

    public DirectionsAsync(MapsActivity activity){
        //this.map = googleMap;
        //the key is stored in res/values/strings.xml and should be changed
        this.key = activity.getResources().getString(R.string.google_maps_key);
        this.activity = activity;
    }

    public void setIndex(int index){this.index = index;}

    //set user location
    public void setCurrLocation(LatLng currLocation){
        this.currLocation = currLocation;
    }
    //set destination
    public void setDestination(LatLng destination){
        this.destination = destination;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(URL... urls) {
        try {
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
            //prepare the url and the parameters to be sent to the google directions api
            GenericUrl url = new GenericUrl("https://maps.googleapis.com/maps/api/directions/json");
            url.put("origin", currLocation.latitude+","+currLocation.longitude);
            url.put("destination", destination.latitude+","+destination.longitude);
            url.put("mode", "walking");
            url.put("sensor",false);
            url.put("units", "metric");
            url.put("key",key);

            //send request, get response
            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            JSONObject mainObject = new JSONObject(httpResponse.parseAsString());
            //parse json objecct into step list
            stepList = parse(mainObject);


        } catch (Exception ex) {
            //tell activity to display a pop up requiring internet connection
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //pass the information back to the main activity
        activity.directionsAsyncPost(index, stepList);
    }

    //parse the json to list of step data
    private List<DirStepData> parse(JSONObject jObject){

        List<DirStepData> finalList = new ArrayList<DirStepData>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");
            jLegs = jRoutes.getJSONObject(0).getJSONArray("legs");
            jSteps = jLegs.getJSONObject(0).getJSONArray("steps");

            /** Traversing all steps */
            for(int k=0;k<jSteps.length();k++){


                String html_instructions = jSteps.getJSONObject(k).getString("html_instructions");
                String travel_mode = jSteps.getJSONObject(k).getString("travel_mode");
                //String maneuver = jSteps.getJSONObject(k).getString("maneuver");

                String distance_text = jSteps.getJSONObject(k).getJSONObject("distance").getString("text");
                String distance_value = jSteps.getJSONObject(k).getJSONObject("distance").getString("value");

                String duration_text = jSteps.getJSONObject(k).getJSONObject("duration").getString("text");
                String duration_value = jSteps.getJSONObject(k).getJSONObject("duration").getString("value");

                String start_lat = jSteps.getJSONObject(k).getJSONObject("start_location").getString("lat");
                String start_lon = jSteps.getJSONObject(k).getJSONObject("start_location").getString("lng");

                String end_lat = jSteps.getJSONObject(k).getJSONObject("end_location").getString("lat");
                String end_lon = jSteps.getJSONObject(k).getJSONObject("end_location").getString("lng");

                String polyline = "";
                polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                List<LatLng> list = PolyUtil.decode(polyline);

                DirStepData currStep = new DirStepData(html_instructions, Float.valueOf(distance_value), Integer.valueOf(duration_value), list);
                finalList.add(currStep);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }


        return finalList;
    }

}



