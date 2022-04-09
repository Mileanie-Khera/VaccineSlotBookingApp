
package com.study.models.beneficiaries;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Beneficiary implements Parcelable {

    public boolean isSelected;
    @SerializedName("beneficiary_reference_id")
    @Expose
    private String beneficiaryReferenceId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("birth_year")
    @Expose
    private String birthYear;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("photo_id_type")
    @Expose
    private String photoIdType;
    @SerializedName("photo_id_number")
    @Expose
    private String photoIdNumber;
    @SerializedName("comorbidity_ind")
    @Expose
    private String comorbidityInd;
    @SerializedName("vaccination_status")
    @Expose
    private String vaccinationStatus;
    @SerializedName("vaccine")
    @Expose
    private String vaccine;
    @SerializedName("dose1_date")
    @Expose
    private String dose1Date;
    @SerializedName("dose2_date")
    @Expose
    private String dose2Date;
    @SerializedName("appointments")
    @Expose
    private ArrayList<Appointment> appointments = null;

    protected Beneficiary(Parcel in) {
        isSelected = in.readByte() != 0;
        beneficiaryReferenceId = in.readString();
        name = in.readString();
        birthYear = in.readString();
        gender = in.readString();
        mobileNumber = in.readString();
        photoIdType = in.readString();
        photoIdNumber = in.readString();
        comorbidityInd = in.readString();
        vaccinationStatus = in.readString();
        vaccine = in.readString();
        dose1Date = in.readString();
        dose2Date = in.readString();
        appointments = in.createTypedArrayList(Appointment.CREATOR);
    }

    public static final Creator<Beneficiary> CREATOR = new Creator<Beneficiary>() {
        @Override
        public Beneficiary createFromParcel(Parcel in) {
            return new Beneficiary(in);
        }

        @Override
        public Beneficiary[] newArray(int size) {
            return new Beneficiary[size];
        }
    };

    public String getBeneficiaryReferenceId() {
        return beneficiaryReferenceId;
    }

    public void setBeneficiaryReferenceId(String beneficiaryReferenceId) {
        this.beneficiaryReferenceId = beneficiaryReferenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhotoIdType() {
        return photoIdType;
    }

    public void setPhotoIdType(String photoIdType) {
        this.photoIdType = photoIdType;
    }

    public String getPhotoIdNumber() {
        return photoIdNumber;
    }

    public void setPhotoIdNumber(String photoIdNumber) {
        this.photoIdNumber = photoIdNumber;
    }

    public String getComorbidityInd() {
        return comorbidityInd;
    }

    public void setComorbidityInd(String comorbidityInd) {
        this.comorbidityInd = comorbidityInd;
    }

    public String getVaccinationStatus() {
        return vaccinationStatus;
    }

    public void setVaccinationStatus(String vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getDose1Date() {
        return dose1Date;
    }

    public void setDose1Date(String dose1Date) {
        this.dose1Date = dose1Date;
    }

    public String getDose2Date() {
        return dose2Date;
    }

    public void setDose2Date(String dose2Date) {
        this.dose2Date = dose2Date;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(beneficiaryReferenceId);
        dest.writeString(name);
        dest.writeString(birthYear);
        dest.writeString(gender);
        dest.writeString(mobileNumber);
        dest.writeString(photoIdType);
        dest.writeString(photoIdNumber);
        dest.writeString(comorbidityInd);
        dest.writeString(vaccinationStatus);
        dest.writeString(vaccine);
        dest.writeString(dose1Date);
        dest.writeString(dose2Date);
        dest.writeTypedList(appointments);
    }
}
