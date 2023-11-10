package com.example.debuggingdemonsapp.model;

import android.graphics.Bitmap;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * Photograph object which is has a bitmap and the uri of the image corresponding to
 *      its database uri
 */
public class Photograph implements Parcelable {
    private Bitmap photo;
    private String uri = "";

    /**
     *
     * @param savedPhoto
     */
    public Photograph(Bitmap savedPhoto){
        this.photo = savedPhoto;
    }

    /**
     *
     * @param in
     */
    protected Photograph(Parcel in) {
        photo = in.readParcelable(Bitmap.class.getClassLoader());
        uri = in.readString();
    }

    /**
     * This method returns the photo's bitmap
     * @return Bitmap photo  The bitmap that is associated with a specific Photograph object
     */
    public Bitmap photoBitmap(){
        return this.photo;
    }

    /**
     * This method returns the photo's Uri
     * @return String uri  The uri as a string which is associated with a specific Photograph object
     */
    public String storageURI(){
        return this.uri;
    }

    /**
     * This method sets the Uri of the photo based on a database uri that is passed in
     * @param databaseURI A Firebase Storage Uri as a string which is passed into the method
     */
    public void setUri(String databaseURI){
        this.uri = databaseURI;
    }

    /**
     * Method is a part of the Parcelable interface
     * @return int
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * This method is used to allow Photograph objects to be passed between fragments in a bundle
     * @param parcel Parcel object from the interface
     * @param i Integer object from by the interface
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeArray(new Object[] {this.uri, this.photo});
    }

    /**
     * Creator method defined by the Parcelable interface
     */
    public static final Creator<Photograph> CREATOR = new Creator<Photograph>() {
        @Override
        public Photograph createFromParcel(Parcel in) {
            return new Photograph(in);
        }

        @Override
        public Photograph[] newArray(int size) {
            return new Photograph[size];
        }
    };
}