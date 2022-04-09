
package com.study.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CowinCenterResponse {

    @SerializedName("centers")
    @Expose
    private ArrayList<Center> centers = null;

    public ArrayList<Center> getCenters() {
        return centers;
    }

    public void setCenters(ArrayList<Center> centers) {
        this.centers = centers;
    }

}
