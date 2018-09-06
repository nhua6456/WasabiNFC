package net.fgghjjkll.wasabinfc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;

import com.mongodb.*;

import java.util.ArrayList;
import java.util.Set;

public class EventsFragment extends Fragment {

    ArrayList<Attendant> attendants;

    public EventsFragment() {

    }

    private RecyclerView attendantsView;
    private RecyclerView.Adapter attendantsAdapter;
    private RecyclerView.LayoutManager attendantsLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("EventsFragment", "This method onCreateView() has been called.");
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        final EditText name_field = rootView.findViewById(R.id.Name);
        final EditText access_field = rootView.findViewById(R.id.Access);
        final CheckBox member_box = rootView.findViewById(R.id.member_check);
        Button add_button = rootView.findViewById(R.id.add_button);

        attendantsView = rootView.findViewById(R.id.attendants);
        attendantsLayoutManager = new LinearLayoutManager(getContext());
        attendantsView.setLayoutManager(attendantsLayoutManager);
//        final ArrayList<Attendant> attendants = new ArrayList<>();

        if(attendants == null && DataStore.getInstance().getData() == null){
            attendants = new ArrayList<Attendant>();
        }
        else if(attendants == null){
            Log.d("EventsFragment", "This method onCreateView() has been called. Attendants have been restored");
            attendants = DataStore.getInstance().getData();
        }

//        if(attendants == null){
//            attendants = new ArrayList<Attendant>();
//        }

//        if(savedInstanceState == null || !savedInstanceState.containsKey("attendants")){
//            Log.d("EventsFragment", "This method onCreateView() has been called. The savedInstanceState is null");
//            attendants = new ArrayList<>();
//
//            attendants.add(new Attendant("Nolan Huang", "1545935", true));
//            attendants.add(new Attendant("Holan Nuang", "5395451", true));
//            attendants.add(new Attendant("Nalon Gnauh", "1234567", false));
//            attendants.add(new Attendant("Huang Nolan", "2656046", true));
//
//        }
//        else{
//            attendants = savedInstanceState.getParcelableArrayList("attendants");
//            Log.d("EventsFragment", "This method onCreateView() has been called. Attendants have been restored");
//        }
        attendantsAdapter = new AttendantsAdapter(attendants);
        attendantsView.setAdapter(attendantsAdapter);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_field.getText().toString();
                String access = access_field.getText().toString();
                boolean member = member_box.isChecked();

                ((AttendantsAdapter) attendantsAdapter).add(new Attendant(name, access, member));

            }
        });
        return rootView;
    }

//    public void lookup{
//
//    }

    private class LookupDatabase extends AsyncTask<Integer, Void, DBObject> {

        @Override
        protected DBObject doInBackground(Integer... ints) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            return lookup(ints);
        }
    }

    private DBObject lookup(Integer[] ids){
        DBObject result = null;
        try{
            for(int i : ids){
                MongoClient mongoClient = new MongoClient("104.223.65.236", 27017);
                DB db = mongoClient.getDB("mydb");
                Set<String> collectionNames = db.getCollectionNames();
                result = db.getCollection("things").findOne(new BasicDBObject("id", i));
                Log.d("lookup", result.toString());
                Log.d("lookup","id: " +i);
            }
        } catch (Exception e){
            return null;
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("EventsFragment", "Saving instance states.");
        outState.putParcelableArrayList("attendants", attendants);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);

//        Log.d("EventsFragment", "This method onActivityCreated() has been called.");
//        if(savedInstanceState == null || !savedInstanceState.containsKey("attendants")){
//            Log.d("EventsFragment", "This method onActivityCreated() has been called. The savedInstanceState is null");
//            attendants = new ArrayList<>();
//
//            attendants.add(new Attendant("Nolan Huang", "1545935", true));
//            attendants.add(new Attendant("Holan Nuang", "5395451", true));
//            attendants.add(new Attendant("Nalon Gnauh", "1234567", false));
//            attendants.add(new Attendant("Huang Nolan", "2656046", true));
//
//        }
//        else{
//            Log.d("EventsFragment", "This method onCreateView() has been called. Attendants have been restored");
//            attendants = savedInstanceState.getParcelableArrayList("attendants");
//        }


//    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("EventsFragment", "This method onPause() has been called.");
        DataStore dataStore = DataStore.getInstance();
        dataStore.setData(attendants);
    }
}
