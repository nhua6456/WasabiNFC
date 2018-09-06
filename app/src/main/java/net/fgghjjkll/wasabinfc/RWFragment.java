package net.fgghjjkll.wasabinfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;

public class RWFragment extends Fragment {

    TextView tv;
    String read_id;

    Switch sw;
    boolean state;
    public RWFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_readwrite, container, false);

        if(savedInstanceState == null || !savedInstanceState.containsKey("read_id") || !savedInstanceState.containsKey("state")){
            read_id = "";
            state = false;
        } else {
            read_id = savedInstanceState.getString("read_id");
            state = savedInstanceState.getBoolean("state");
        }

        return rootView;
    }

    public void setText(String str){
        tv = getView().findViewById(R.id.rw_text);

        read_id = str;

        tv.setText(read_id);
    }

    public String getText(){
        tv = getView().findViewById(R.id.rw_text);

        read_id = tv.getText().toString();

        return read_id;
    }

    public boolean getRW(){
        sw = getView().findViewById(R.id.rw_switch);

        state = sw.isChecked();

        return state;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString("read_id", read_id);
        outState.putBoolean("state", state);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey(read_id) || !savedInstanceState.containsKey("state")){
            read_id = "";
            state = false;
        } else {
            read_id = savedInstanceState.getString("read_id");
            state = savedInstanceState.getBoolean("state");
        }
    }
}
