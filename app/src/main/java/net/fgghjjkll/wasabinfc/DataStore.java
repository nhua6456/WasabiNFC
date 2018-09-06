package net.fgghjjkll.wasabinfc;

import java.util.ArrayList;

public class DataStore {
    private static DataStore handler;
    private ArrayList<Attendant> data;

    private DataStore(){

    }

    public static DataStore getInstance(){
        if(handler == null){
            handler = new DataStore();
        }
        return handler;
    }

    public ArrayList<Attendant> getData() {
        return data;
    }
    public void setData(ArrayList<Attendant> data){
        this.data = data;
    }
}
