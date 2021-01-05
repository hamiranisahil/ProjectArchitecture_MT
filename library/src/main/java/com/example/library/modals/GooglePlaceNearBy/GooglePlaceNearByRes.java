
package com.example.library.modals.GooglePlaceNearBy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GooglePlaceNearByRes {

    @SerializedName("html_attributions")
    @Expose
    public List<Object> html_attributions = new ArrayList<Object>();
    @SerializedName("results")
    @Expose
    public List<Result> results = new ArrayList<Result>();
    @SerializedName("status")
    @Expose
    public String status;

}
