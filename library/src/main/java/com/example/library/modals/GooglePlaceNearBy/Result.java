
package com.example.library.modals.GooglePlaceNearBy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("geometry")
    @Expose
    public Geometry geometry;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("place_id")
    @Expose
    public String place_id;
    @SerializedName("vicinity")
    @Expose
    public String vicinity;

}
