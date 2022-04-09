
package com.study.models.beneficiaries;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeneficiaryResponse {

    @SerializedName("beneficiaries")
    @Expose
    private ArrayList<Beneficiary> beneficiaries = null;

    public ArrayList<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(ArrayList<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

}
