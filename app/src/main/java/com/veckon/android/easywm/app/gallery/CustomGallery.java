package com.veckon.android.easywm.app.gallery;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomGallery { //implements Parcelable {

    public String sdcardPath;
    public boolean isSeleted = false;

}

    /*
    public int describeContents() {
        return 0;
    }

    public void setString(String sdcardPath){

        this.sdcardPath = sdcardPath;

    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(sdcardPath);

    }

    public static final Parcelable.Creator<CustomGallery> CREATOR
            = new Parcelable.Creator<CustomGallery>()
    {
        public CustomGallery createFromParcel (Parcel in) {
            return new CustomGallery(in);
        }

        public CustomGallery[] newArray(int size){
            return new CustomGallery[size];
        }
    };

    private CustomGallery(Parcel in){
        sdcardPath = in.readString();

    }
*/

