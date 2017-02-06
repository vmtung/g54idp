package com.nottingham.cs.tung.g54idp.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nottingham.cs.tung.g54idp.R;
import com.nottingham.cs.tung.g54idp.data.DirStepData;
import com.nottingham.cs.tung.g54idp.fragment.InstructionFragment.OnInstructionListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link InstructionFragment.OnInstructionListFragmentInteractionListener}.
 */
public class InstructionRecyclerViewAdapter extends RecyclerView.Adapter<InstructionRecyclerViewAdapter.ViewHolder> {

    private final List<DirStepData> mValues;
    private final OnInstructionListFragmentInteractionListener mListener;

    public InstructionRecyclerViewAdapter(List<DirStepData> items, OnInstructionListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_instruction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //display the list of directions
        if (position==0){
            holder.mItem = null;
            holder.mContentView.setText("DIRECTION: ");
        } else {
            holder.mItem = mValues.get(position - 1);
            holder.mContentView.setText(Html.fromHtml(mValues.get(position - 1).getInstructions()) + " (" + mValues.get(position).getDistance() + "m)");
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mContentView;
        public DirStepData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
