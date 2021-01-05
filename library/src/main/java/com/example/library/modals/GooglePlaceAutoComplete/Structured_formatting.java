
package com.example.library.modals.GooglePlaceAutoComplete;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Structured_formatting {

    @SerializedName("main_text")
    @Expose
    public String main_text;
    @SerializedName("main_text_matched_substrings")
    @Expose
    public List<Main_text_matched_substring> main_text_matched_substrings = new ArrayList<Main_text_matched_substring>();
    @SerializedName("secondary_text")
    @Expose
    public String secondary_text;

}
