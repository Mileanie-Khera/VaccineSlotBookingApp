
package com.study.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleResponse {

    @SerializedName("appointment_confirmation_no")
    @Expose
    private String appointmentConfirmationNo;

    @SerializedName("errorCode")
    @Expose
    private String errorCode;

    @SerializedName("error")
    @Expose
    private String error;

    public String getAppointmentConfirmationNo() {
        return appointmentConfirmationNo;
    }

    public void setAppointmentConfirmationNo(String appointmentConfirmationNo) {
        this.appointmentConfirmationNo = appointmentConfirmationNo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
