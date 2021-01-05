
package com.example.library.modals.GooglePlaceAutoComplete;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction {

    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("matched_substrings")
    @Expose
    public List<Matched_substring> matched_substrings = new ArrayList<Matched_substring>();
    @SerializedName("place_id")
    @Expose
    public String place_id;
    @SerializedName("reference")
    @Expose
    public String reference;
    @SerializedName("structured_formatting")
    @Expose
    public Structured_formatting structured_formatting;
    @SerializedName("terms")
    @Expose
    public List<Term> terms = new ArrayList<Term>();
    @SerializedName("types")
    @Expose
    public List<String> types = new ArrayList<String>();

}
