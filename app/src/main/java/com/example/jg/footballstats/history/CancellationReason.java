package com.example.jg.footballstats.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancellationReason {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("details")
    @Expose
    private Details details;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

}
