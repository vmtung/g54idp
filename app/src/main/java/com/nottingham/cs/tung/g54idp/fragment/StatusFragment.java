package com.nottingham.cs.tung.g54idp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nottingham.cs.tung.g54idp.R;
import com.nottingham.cs.tung.g54idp.data.RoomDataItem;
import com.nottingham.cs.tung.g54idp.data.DirStepData;
import com.nottingham.cs.tung.g54idp.data.SlotData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusFragment.OnStatusFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnStatusFragmentInteractionListener mListener;

    private Button redoButton;
    private Button focusButton;
    private Button cancelButton;

    private RoomDataItem item;
    private Handler handler;
    private Runnable r;

    private List<TextView> slotList;
    private List<DirStepData> stepList;

    private Date infoAt;

    public StatusFragment() {
        // Required empty public constructor
        infoAt = new Date();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler =  new Handler();
        slotList = new ArrayList<>();
        stepList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_status, container, false);
        // get buttons and set listener for each of them
        redoButton = (Button) view.findViewById(R.id.button);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStatusFragmentInteraction(1);
            }
        });

        focusButton = (Button) view.findViewById(R.id.buttonFocus);
        focusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStatusFragmentInteraction(2);
            }
        });

        cancelButton = (Button) view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStatusFragmentInteraction(3);
            }
        });

        //add text view into a list for easier manage
        slotList.add((TextView)view.findViewById(R.id.slot1));
        slotList.add((TextView)view.findViewById(R.id.slot2));
        slotList.add((TextView)view.findViewById(R.id.slot3));
        slotList.add((TextView)view.findViewById(R.id.slot4));
        for (TextView slot: slotList){
            slot.setVisibility(View.GONE);
        }

        return view;
    }

    //update direction list and display the walk time
    public void updateStepList(List<DirStepData> list){
        stepList.clear();
        this.stepList.addAll(list);

        slotList.get(1).setText("Walking time: "+getTotalStepTime()/60+" mins");
    }

    //get total walk time from the step list
    public int getTotalStepTime(){
        int time=0;
        for (DirStepData step: stepList){
            time+= step.getDuration();
        }
        return time;
    }

    //set selected item to be displayed, show
    // - name
    // -how many free pc last known, how many free pc will be available
    // -how many people also coming
    public void setItem(RoomDataItem item1){
        handler.removeCallbacks(r);
        this.item = item1;

        slotList.get(0).setText("Destination: "+item.getName());
        slotList.get(3).setText((item.getArriving()-1<0 ? 0 : item.getArriving()-1)+" other clients heading to this.");

        long minAgo =(System.currentTimeMillis() - item.getLastKnown().getTime().getTime())/1000/60;
        String textAgo = "";
        if (minAgo==0) textAgo +="now";
        else if (minAgo/60>0) {
            if (minAgo/60/24>0)
                textAgo += minAgo/60/24+" days ago";
            else textAgo += minAgo/60+" hours ago";
        }
        else textAgo += minAgo+" mins ago";

        Date arrivalTime = new Date(getTotalStepTime()*1000 + System.currentTimeMillis());
        SlotData firstFree = item.getFirstFreeSlotAfter(arrivalTime);
        slotList.get(2).setText("Free PCs: "+item.getLastKnown().getFree()+" ("+textAgo+") | "+"Upon arrival: " + firstFree.getFree() + " free PCs (Estimated)");

    }

    //set text saying how old was the information
    public void setTextGotInfo(){
        infoAt.setTime(System.currentTimeMillis());

        r = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 60000);
                slotList.get(0).setText("Info get from server from "+(System.currentTimeMillis() - infoAt.getTime())/60/1000+" mins ago");
            }
        };
        handler.post(r);

    }

    //set which kind of text will be put on display
    public void setDisplayMode(int mode){
        //hide all views and will make each one show again if needed
        focusButton.setVisibility(View.GONE);
        redoButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        for (TextView slot: slotList){
            slot.setVisibility(View.GONE);
        }

        //set display text
        if (mode==1){
            slotList.get(0).setVisibility(View.VISIBLE);
            slotList.get(0).setText("Finding distance to all lab");
        } else if (mode==2){
            slotList.get(0).setVisibility(View.VISIBLE);
            slotList.get(0).setText("Geting server predictions");
        } else if (mode==3){
            slotList.get(0).setVisibility(View.VISIBLE);
            slotList.get(0).setText("");
            redoButton.setVisibility(View.VISIBLE);
            redoButton.setText("Restart");
            focusButton.setVisibility(View.VISIBLE);
            focusButton.setText("Refocus Map");

        } else if (mode==4){
            slotList.get(0).setText("");

            redoButton.setVisibility(View.VISIBLE);
            redoButton.setText("Restart");
            focusButton.setVisibility(View.VISIBLE);
            focusButton.setText("Refocus Map");
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setText("Cancel & Quit");

            for (TextView slot: slotList){
                slot.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStatusFragmentInteractionListener) {
            mListener = (OnStatusFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStatusFragmentInteractionListener {
        // TODO: Update argument type and name
        void onStatusFragmentInteraction(int status);
    }
}
