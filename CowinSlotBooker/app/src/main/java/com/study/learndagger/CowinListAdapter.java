package com.study.learndagger;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.database.CowinDaoModel;
import com.study.utils.UtilityKt;

import java.util.ArrayList;

public class CowinListAdapter extends RecyclerView.Adapter<CowinListAdapter.ViewHolder> {

    private final ArrayList<CowinDaoModel> mCowinDaoModellist;

    public CowinListAdapter(@NonNull ArrayList<CowinDaoModel> cowinDaoModellist) {
        this.mCowinDaoModellist = cowinDaoModellist;
    }

    @Override
    @NonNull
    public CowinListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cowin_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CowinListAdapter.ViewHolder holder, int position) {
        CowinDaoModel itemSelectorModel = mCowinDaoModellist.get(position);
        holder.timestamp.setText("Time : " + UtilityKt.parseStringToFormattedStringDate(itemSelectorModel.timestamp, UtilityKt.MMM_DD_YYYY_HH_MM_SS, UtilityKt.TARGET_FORMAT_TIMESTAMP));
        if (itemSelectorModel.error != null && itemSelectorModel.error.contains("Unauth")) {
            holder.error.setTextColor(holder.error.getContext().getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.error.setTextColor(holder.error.getContext().getResources().getColor(android.R.color.black));
        }
        holder.error.setText("Status : " + itemSelectorModel.error);

        if (!TextUtils.isEmpty(itemSelectorModel.confirmationNo)) {
            holder.confirmationNo.setVisibility(View.VISIBLE);
            holder.error.setText("Appointment Booked");
        } else {
            holder.confirmationNo.setVisibility(View.GONE);
        }
        holder.confirmationNo.setText(itemSelectorModel.confirmationNo);
    }

    @Override
    public int getItemCount() {
        return mCowinDaoModellist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView timestamp, error, confirmationNo;

        private ViewHolder(View view) {
            super(view);
            timestamp = view.findViewById(R.id.timestamp);
            error = view.findViewById(R.id.error);
            confirmationNo = view.findViewById(R.id.confirmation_no);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
