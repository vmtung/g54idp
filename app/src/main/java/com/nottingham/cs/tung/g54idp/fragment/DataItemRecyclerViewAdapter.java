package com.nottingham.cs.tung.g54idp.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nottingham.cs.tung.g54idp.R;
import com.nottingham.cs.tung.g54idp.data.RoomDataItem;
import com.nottingham.cs.tung.g54idp.data.RoomDataSubMenu;
import com.nottingham.cs.tung.g54idp.fragment.InfoListFragment.OnOptionListFragmentInteractionListener;
import com.nottingham.cs.tung.g54idp.data.SlotData;


import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class DataItemRecyclerViewAdapter extends RecyclerView.Adapter<DataItemRecyclerViewAdapter.ViewHolder> {

    private final List<RoomDataItem> mValues;
    private final OnOptionListFragmentInteractionListener mListener;

    public DataItemRecyclerViewAdapter(List<RoomDataItem> items, OnOptionListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_option_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //displaying:
        // -Name
        // -is it suggested or not
        // -walk time
        // -last known number of free computer, when
        // -predict number of available computer
        // -how many people also coming
        if (mValues.get(position).getSuggested())
            holder.mNameView.setText( "(SUGGEST) "+mValues.get(position).getName()+", "+(((RoomDataSubMenu)mValues.get(position).getParent()).getTravelTime()/60)+" mins walk");
        else holder.mNameView.setText(mValues.get(position).getName()+", "+(((RoomDataSubMenu)mValues.get(position).getParent()).getTravelTime()/60)+" mins walk");

        //determine what text will be used to represent the passed time
        long minAgo =(System.currentTimeMillis() - mValues.get(position).getLastKnown().getTime().getTime())/1000/60;
        String textAgo = "";
        if (minAgo==0) textAgo +="now";
        else if (minAgo/60>0) {
            if (minAgo/60/24>0)
                textAgo += minAgo/60/24+" days ago";
            else textAgo += minAgo/60+" hours ago";
        }
        else textAgo += minAgo+" mins ago";

        Date arrivalTime = new Date(((RoomDataSubMenu)mValues.get(position).getParent()).getTravelTime()*1000 + System.currentTimeMillis());

        SlotData firstFree = mValues.get(position).getFirstFreeSlotAfter(arrivalTime);
        holder.mCurrentFreeView.setText("Free PCs: "+mValues.get(position).getLastKnown().getFree()+" ("+textAgo+") | "+firstFree.getFree()+" upon arrival (Estimated)");

        holder.mRequestedView.setText(mValues.get(position).getArriving()+" clients heading here.");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onOptionListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mCurrentFreeView;
        public final TextView mRequestedView;
        public RoomDataItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mCurrentFreeView = (TextView) view.findViewById(R.id.current_free);
            mRequestedView = (TextView) view.findViewById(R.id.arriving);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
