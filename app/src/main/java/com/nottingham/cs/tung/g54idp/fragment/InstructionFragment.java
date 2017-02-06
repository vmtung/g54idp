package com.nottingham.cs.tung.g54idp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nottingham.cs.tung.g54idp.R;
import com.nottingham.cs.tung.g54idp.data.DirStepData;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of instructions.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnInstructionListFragmentInteractionListener}
 * interface.
 */
public class InstructionFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnInstructionListFragmentInteractionListener mListener;

    private RecyclerView listView;
    private List<DirStepData> listItems;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstructionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InstructionFragment newInstance(int columnCount) {
        InstructionFragment fragment = new InstructionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instruction_list, container, false);

        // get the view Set the list adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            listView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                listView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                listView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            listItems = new ArrayList<>();
            listView.setAdapter(new InstructionRecyclerViewAdapter(listItems, mListener));
        }
        return view;
    }

    //display the instruction on the list
    public void updateList(List<DirStepData> list){
        listItems.clear();
        listItems.addAll(list);
        listView.getAdapter().notifyDataSetChanged();
        listView.scrollToPosition(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInstructionListFragmentInteractionListener) {
            mListener = (OnInstructionListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOptionListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnInstructionListFragmentInteractionListener {
        void onListFragmentInteraction(DirStepData item);
    }
}
