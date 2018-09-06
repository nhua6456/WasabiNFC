package net.fgghjjkll.wasabinfc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

public class AttendantsAdapter extends RecyclerView.Adapter<AttendantsAdapter.ViewHolder> {

    private ArrayList<Attendant> attendants;

    public AttendantsAdapter(ArrayList<Attendant> attendants){
        this.attendants = attendants;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView attendant_textView;
        public View layout;

        public ViewHolder(View v){
            super(v);
            layout = v;
            attendant_textView = v.findViewById(R.id.attendant_textview);
        }
    }

    public void add(int position, Attendant person){
        attendants.add(position, person);
        notifyItemInserted(position);
    }

    public void add(Attendant person){
        attendants.add(person);
        notifyItemInserted(attendants.size());
    }

    public void remove(int position){
        attendants.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public AttendantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.attendant_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendantsAdapter.ViewHolder viewHolder, int i) {

        final Attendant person = attendants.get(i);

        viewHolder.attendant_textView.setText(person.toString());
    }

    @Override
    public int getItemCount() {
        return attendants.size();
    }

}
