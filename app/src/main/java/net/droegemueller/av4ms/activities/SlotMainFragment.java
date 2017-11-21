package net.droegemueller.av4ms.activities;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import net.droegemueller.av4ms.R;
import net.droegemueller.av4ms.Av4msBasicReadData;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlotMainFragment extends Fragment {
    @BindView(R.id.slot_image) ImageView viewImage;
    @BindView(R.id.slot_header) TextView viewHeader;
    @BindView(R.id.slot_state_text) TextView viewStateText;
//    @BindView(R.id.slot_charge_text) TextView chargeText;
//    @BindView(R.id.slot_discharge_text) TextView dischargeText;
    @BindView(R.id.slot_data_html) WebView htmlTable;

    private Date lastDate = null;
    private Av4msBasicReadData lastData = null;

    private OnSlotInteractionListener mListener;

    public static final String PARAM_SLOT_NO = "SLOT_NO";
    private static final String STATE_LAST_DATE = "STATE_LAST_DATE";
    private static final String STATE_LAST_DATA = "STATE_LAST_DATA";

    private int slotNo = 0;

    private Integer oldImageKey = null;

    public SlotMainFragment() {
    }

    public static SlotMainFragment newInstance(int slotNo) {
        SlotMainFragment fragment = new SlotMainFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_SLOT_NO, slotNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            slotNo = getArguments().getInt(PARAM_SLOT_NO);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            long readLastDate = savedInstanceState.getLong(STATE_LAST_DATE);
            lastDate = readLastDate == Long.MIN_VALUE ? null : new Date(readLastDate);
            lastData = savedInstanceState.getParcelable(STATE_LAST_DATA);
            updateFromReceivedData();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_LAST_DATE, lastDate == null ? Long.MIN_VALUE : lastDate.getTime());
        outState.putParcelable(STATE_LAST_DATA, lastData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_slot_main, container, false);
        ButterKnife.bind(this, v);
        String fmt = getResources().getString(R.string.slot_header_text);
        viewHeader.setText(String.format(fmt, slotNo));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSlotInteractionListener) {
            mListener = (OnSlotInteractionListener) context;
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
    public interface OnSlotInteractionListener {
        void requestUpdate();
    }

    public static int stateImageKey(Av4msBasicReadData.ChargerSlotStandardData s) {
        int key;
        if (s.connected) {
            switch (s.slotState) {
                case Charging:
                    key = R.drawable.batt_charge_anim2;
                    break;
                case Discharging:
                    key = R.drawable.batt_discharge_anim2;
                    break;
                case EmptySlot:
                    key = R.drawable.batt_none;
                    break;
                case Full:
                    key = R.drawable.batt_full;
                    break;
                case Error:
                    key = R.drawable.batt_attn;
                    break;
                default:
                    key = R.drawable.batt_qu;
                    break;
            }
        } else {
            key = R.drawable.batt_qu;
        }
        return key;
    }

    public void updateSlotData(Av4msBasicReadData d) {
        lastData = d;
        lastDate = new Date();
        updateFromReceivedData();
    }

    private void updateFromReceivedData() {
        Av4msBasicReadData.ChargerSlotStandardData slotData = lastData.slots[slotNo - 1];
        Av4msBasicReadData.SlotState slotState = slotData.slotState;

        int imageKey = stateImageKey(slotData);
        if (oldImageKey == null || imageKey != oldImageKey) {
            Drawable res = ResourcesCompat.getDrawable(getResources(), imageKey, null);
            viewImage.setImageDrawable(res);
            if (res instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) res;
                animatedVectorDrawable.start();
            }
            oldImageKey = imageKey;
        }
        viewStateText.setText(ConnectionTestActivity.stateText(getResources(), slotData));
//        chargeText.setText(String.format(getResources().getString(R.string.slot_charge_data_fmt),
//                slotData.chargeTime / 3600, (slotData.chargeTime % 3600) / 60, slotData.chargeTime % 60,
//                slotData.chargingVoltage / 1000.0, slotData.chargingAverageVoltage / 1000.0, slotData.chargingCurrent,
//                slotData.chargeCapacity));
//        dischargeText.setText(String.format(getResources().getString(R.string.slot_discharge_data_fmt),
//                slotData.dischargeTime / 3600, (slotData.dischargeTime % 3600) / 60, slotData.dischargeTime % 60,
//                slotData.dischargingVoltage / 1000.0, slotData.dischargingAverageVoltage / 1000.0, slotData.dischargingCurrent,
//                slotData.dischargeCapacity));
        String html = String.format(getResources().getString(R.string.slot_html_data_fmt),
                slotData.chargeTime / 3600, (slotData.chargeTime % 3600) / 60, slotData.chargeTime % 60,
                slotData.chargingVoltage / 1000.0, slotData.chargingAverageVoltage / 1000.0, slotData.chargingCurrent,
                slotData.chargeCapacity,
                slotData.dischargeTime / 3600, (slotData.dischargeTime % 3600) / 60, slotData.dischargeTime % 60,
                slotData.dischargingVoltage / 1000.0, slotData.dischargingAverageVoltage / 1000.0, slotData.dischargingCurrent,
                slotData.dischargeCapacity);
        htmlTable.setBackgroundColor(0);
        htmlTable.loadData(html, "text/html; charset=utf-8", "utf-8");
    }
}
