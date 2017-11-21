package net.droegemueller.av4ms.activities;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.droegemueller.av4ms.R;
import net.droegemueller.av4ms.Av4msBasicReadData;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMainFragment extends Fragment {
    private static final String STATE_LAST_DATE = "STATE_LAST_DATE";
    private static final String STATE_LAST_DATA = "STATE_LAST_DATA"
            ;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.home_text) TextView homeText;

    private Date lastDate = null;
    private Av4msBasicReadData lastData = null;


    public HomeMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeMainFragment newInstance() {
        HomeMainFragment fragment = new HomeMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSlotInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateData(Av4msBasicReadData d) {
        lastDate = new Date();
        lastData = d;
        updateFromReceivedData();
    }

    private void updateFromReceivedData() {
        String s;
        if (lastDate == null || lastData == null) {
            s = getResources().getString(R.string.home_connection_header_init);
        } else if (lastData.slots[0].connected) {
            String fmt = getResources().getString(R.string.home_connection_header_data_time);
            s = String.format(fmt, lastDate);
        }
        else { // Not connected
            String fmt = getResources().getString(R.string.home_connection_header_no_conn);
            s = String.format(fmt, lastDate);
        }
        homeText.setText(s);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            long readLastDate = savedInstanceState.getLong(STATE_LAST_DATE);
            lastDate = readLastDate == Long.MIN_VALUE ? null : new Date(readLastDate);
            lastData = savedInstanceState.getParcelable(STATE_LAST_DATA);
        }
        updateFromReceivedData();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_LAST_DATE, lastDate == null ? Long.MIN_VALUE : lastDate.getTime());
        outState.putParcelable(STATE_LAST_DATA, lastData);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
