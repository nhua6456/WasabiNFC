package net.fgghjjkll.wasabinfc;

import android.os.Parcel;
import android.os.Parcelable;

public class Attendant implements Parcelable{
    private String name;
    private String access;
    private boolean member;

    public Attendant(String name, String access, boolean member){
        this.name = name;
        this.access = access;
        this.member = member;
    }

    protected Attendant(Parcel in) {
        name = in.readString();
        access = in.readString();
        member = in.readByte() != 0;
    }

    public static final Creator<Attendant> CREATOR = new Creator<Attendant>() {
        @Override
        public Attendant createFromParcel(Parcel in) {
            return new Attendant(in);
        }

        @Override
        public Attendant[] newArray(int size) {
            return new Attendant[size];
        }
    };

    public String toString(){
        return name + " " + access + " " + member;
    }

    public String getName(){
        return name;
    }

    public String getAccess(){
        return access;
    }

    public boolean getMember(){
        return member;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(access);
        parcel.writeBooleanArray(new boolean[]{member});
    }
}
