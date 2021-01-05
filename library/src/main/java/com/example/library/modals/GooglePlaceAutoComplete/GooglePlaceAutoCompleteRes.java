
package com.example.library.modals.GooglePlaceAutoComplete;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GooglePlaceAutoCompleteRes {

    @SerializedName("predictions")
    @Expose
    public List<Prediction> predictions = new ArrayList<Prediction>();
    @SerializedName("status")
    @Expose
    public String status;

}
