
package com.example.library.modals.GooglePlaceAutoComplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term {

    @SerializedName("offset")
    @Expose
    public Integer offset;
    @SerializedName("value")
    @Expose
    public String value;

}
