package com.example.qs.pilgrimnize;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class FinshedReportsAdapter extends RecyclerView.Adapter<FinshedReportsAdapter.FinshedReportViewHolder> {

    private Context mCtx;
    private List<Report> reports;
    private ServicesClass servicesClass;
    public FinshedReportsAdapter(Context mCtx, List<Report> reports) {
        this.mCtx = mCtx;
        this.reports = reports;
    }


    @Override
    public void onBindViewHolder(@NonNull FinshedReportViewHolder finshedReportViewHolder, int i) {
        Report report = reports.get(i);
        servicesClass = new ServicesClass();
        System.out.println("HHHHKHHKHKH"+ report.endDate);
        finshedReportViewHolder.descr.setText(report.getDescribe());
        finshedReportViewHolder.locationName.setText(report.getLocationName());
        finshedReportViewHolder.phoneNumber.setText(String.valueOf(report.getPhnoeNumber()));
        finshedReportViewHolder.timeLabel.setText(String.valueOf(servicesClass.getTimeAgo(report.getEndDate())));
    }
    @NonNull
    @Override
    public FinshedReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        System.out.println("onBindViewHolder  1");
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout,null);
        return new FinshedReportViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class FinshedReportViewHolder extends RecyclerView.ViewHolder {

        TextView descr,phoneNumber,locationName,timeLabel;

        public RelativeLayout relativeLayout;

        public FinshedReportViewHolder(@NonNull View itemView) {
            super(itemView);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
                descr = (TextView) itemView.findViewById(R.id.description);
                phoneNumber = (TextView) itemView.findViewById(R.id.phone);
                locationName = (TextView) itemView.findViewById(R.id.locationName);
                timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);


        }
    }
}
