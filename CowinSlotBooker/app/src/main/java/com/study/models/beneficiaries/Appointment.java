
package com.study.models.beneficiaries;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment implements Parcelable {

    @SerializedName("appointment_id")
    @Expose
    private String appointmentId;
    @SerializedName("center_id")
    @Expose
    private Long centerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("block_name")
    @Expose
    private String blockName;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("dose")
    @Expose
    private Long dose;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("slot")
    @Expose
    private String slot;

    protected Appointment(Parcel in) {
        appointmentId = in.readString();
        if (in.readByte() == 0) {
            centerId = null;
        } else {
            centerId = in.readLong();
        }
        name = in.readString();
        stateName = in.readString();
        districtName = in.readString();
        blockName = in.readString();
        from = in.readString();
        to = in.readString();
        if (in.readByte() == 0) {
            dose = null;
        } else {
            dose = in.readLong();
        }
        sessionId = in.readString();
        date = in.readString();
        slot = in.readString();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getCenterId() {
        return centerId;
    }

    public void setCenterId(Long centerId) {
        this.centerId = centerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getDose() {
        return dose;
    }

    public void setDose(Long dose) {
        this.dose = dose;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appointmentId);
        if (centerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(centerId);
        }
        dest.writeString(name);
        dest.writeString(stateName);
        dest.writeString(districtName);
        dest.writeString(blockName);
        dest.writeString(from);
        dest.writeString(to);
        if (dose == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(dose);
        }
        dest.writeString(sessionId);
        dest.writeString(date);
        dest.writeString(slot);
    }
}
