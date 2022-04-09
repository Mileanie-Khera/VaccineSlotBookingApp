package com.study.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.study.learndagger.R;
import com.study.models.beneficiaries.Appointment;
import com.study.models.beneficiaries.Beneficiary;
import com.study.utils.UtilityKt;

import java.util.ArrayList;

public class BeneficiaryListAdapter extends RecyclerView.Adapter<BeneficiaryListAdapter.ViewHolder> {

    private ArrayList<Beneficiary> mBeneficiaryList;
    private Context mContext;

    public BeneficiaryListAdapter(Context context, @NonNull ArrayList<Beneficiary> cowinDaoModellist) {
        mContext = context;
        this.mBeneficiaryList = cowinDaoModellist;
    }

    public ArrayList<Beneficiary> getBeneficiaryList() {
        return mBeneficiaryList;
    }

    @Override
    @NonNull
    public BeneficiaryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_beneficiary_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiaryListAdapter.ViewHolder holder, int position) {
        Beneficiary itemSelectorModel = mBeneficiaryList.get(position);

        if (itemSelectorModel.isSelected) {
            holder.selectedIv.setVisibility(View.VISIBLE);
        } else {
            holder.selectedIv.setVisibility(View.INVISIBLE);
        }
        holder.nameTv.setText(itemSelectorModel.getName());
        holder.genderTv.setText(itemSelectorModel.getGender());
        holder.birthYearTv.setText(itemSelectorModel.getBirthYear());
        holder.vaccination_status.setText(itemSelectorModel.getVaccinationStatus());
        holder.vaccine.setText(itemSelectorModel.getVaccine());

        if (!TextUtils.isEmpty(itemSelectorModel.getDose1Date())) {
            String dose1Date = UtilityKt.parseStringToFormattedStringDate(itemSelectorModel.getDose1Date(),
                    UtilityKt.DD_MM_YYYY, UtilityKt.TARGET_FORMAT);
            if (TextUtils.isEmpty(dose1Date)) {
                dose1Date = "N/A";
            }
            holder.dose1_date.setText("Dose 1: " + dose1Date);
        } else {
            holder.dose1_date.setText("Dose 1: N/A");
        }

        if (!TextUtils.isEmpty(itemSelectorModel.getDose2Date())) {
            String dose2Date = UtilityKt.parseStringToFormattedStringDate(itemSelectorModel.getDose2Date(),
                    UtilityKt.DD_MM_YYYY, UtilityKt.TARGET_FORMAT);
            if (TextUtils.isEmpty(dose2Date)) {
                dose2Date = "N/A";
            }
            holder.dose2_date.setText("Dose 2: " + dose2Date);
        } else {
            holder.dose2_date.setText("Dose 2: N/A");
        }

        holder.appointmentsLv.removeAllViews();
        for (Appointment appointment : itemSelectorModel.getAppointments()) {
            View appointmentRow = LayoutInflater.from(holder.appointmentsLv.getContext()).inflate(R.layout.layout_appointment_row, null);

            TextView center_name = appointmentRow.findViewById(R.id.center_name);
            center_name.setText(appointment.getName() + ", " + appointment.getBlockName() + ", " + appointment.getDistrictName() + ", " + appointment.getStateName());

            TextView appointment_date = appointmentRow.findViewById(R.id.appointment_date);
            appointment_date.setText(UtilityKt.parseStringToFormattedStringDate(appointment.getDate(),
                    UtilityKt.DD_MM_YYYY, UtilityKt.TARGET_FORMAT));

            TextView appointment_slot = appointmentRow.findViewById(R.id.appointment_slot);
            appointment_slot.setText(appointment.getSlot());

            TextView doseTv = appointmentRow.findViewById(R.id.dose);
            doseTv.setText("Dose: " + appointment.getDose());


            holder.appointmentsLv.addView(appointmentRow);
        }
    }

    @Override
    public int getItemCount() {
        return mBeneficiaryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTv, genderTv, birthYearTv, vaccination_status, vaccine, dose1_date, dose2_date;
        private LinearLayout appointmentsLv;
        private ImageView selectedIv;

        private ViewHolder(View view) {
            super(view);
            selectedIv = view.findViewById(R.id.selected_iv);
            nameTv = view.findViewById(R.id.name);
            genderTv = view.findViewById(R.id.gender);
            birthYearTv = view.findViewById(R.id.birth_year);
            vaccination_status = view.findViewById(R.id.vaccination_status);
            vaccine = view.findViewById(R.id.vaccine);
            dose1_date = view.findViewById(R.id.dose1_date);
            dose2_date = view.findViewById(R.id.dose2_date);
            appointmentsLv = view.findViewById(R.id.appointment_container);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Beneficiary beneficiary = mBeneficiaryList.get(getAdapterPosition());
            ArrayList<Appointment> appointments = beneficiary.getAppointments();
            boolean selectBeneficiary = false;
            if (!appointments.isEmpty()) {
                if (appointments.size() == 1) {
                    if (TextUtils.isEmpty(beneficiary.getDose1Date())) {
                        //Appointment is made and dose is not administered
                        Snackbar.make(
                                v,
                                R.string.booking_already_present,
                                Snackbar.LENGTH_LONG).show();
                    } else {
                        selectBeneficiary = true;
                    }
                } else if (appointments.size() == 2) {
                    if (TextUtils.isEmpty(beneficiary.getDose2Date())) {
                        //Appointment is made and dose is not administered
                        Snackbar.make(
                                v,
                                R.string.booking_already_present,
                                Snackbar.LENGTH_LONG).show();
                    } else {
                        selectBeneficiary = true;
                    }
                }

            } else {
                selectBeneficiary = true;
            }
            if (selectBeneficiary) {
                for (Beneficiary item : mBeneficiaryList) {
                    item.isSelected = false;
                }

                beneficiary.isSelected = true;
                notifyDataSetChanged();
            }
        }
    }
}
