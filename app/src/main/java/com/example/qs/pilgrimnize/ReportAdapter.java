package com.example.qs.pilgrimnize;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends  RecyclerView.Adapter<ReportAdapter.ReportViewHolder>{

    private Context mCtx;
    private List<Report> reports;
    private User user;
    private List<User> users;
    private ServicesClass servicesClass;
    private List<Report> finshedReports = new ArrayList<>();

    public ReportAdapter(Context mCtx, List<Report> reports,User user,ServicesClass servicesClass) {
        this.mCtx = mCtx;
        this.reports = reports;
        this.user = user;
        this.servicesClass = servicesClass;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        System.out.println("onBindViewHolder  1");
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout,null);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder reportViewHolder, final int i) {
        System.out.println("onBindViewHolder");
        if (user.userType == 1 ){
            final Report report = reports.get(i);
            reportViewHolder.descr.setText(report.getDescribe());
            reportViewHolder.locationName.setText(report.getLocationName());
            reportViewHolder.phoneNumber.setText(String.valueOf(report.getPhnoeNumber()));

            reportViewHolder.timeLabel.setText(servicesClass.getTimeAgo(report.getCreateionDate()));

            reportViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapsIntent = new Intent(mCtx.getApplicationContext(),MapsActivity.class);
                    mapsIntent.putExtra("USER",user);
                    mapsIntent.putExtra("REPORT",report);
                    mCtx.startActivity(mapsIntent);
                }
            });


        }else if(user.userType == 0) {
            users = servicesClass.fetchAllUsers();
            finshedReports = servicesClass.fetchFinshedReports();
            List<String> list = new ArrayList<>();
            list.add("Monitor");
            list.add("Finshed Reports");

            reportViewHolder.locationName.setText(list.get(i));
            reportViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(v.getId());
                    switch (i){
                        case 0:
                            Intent mapIntent = new Intent(mCtx.getApplicationContext(),MapsActivity.class);
                            mapIntent.putExtra("USER",user);
                            mapIntent.putParcelableArrayListExtra("USERS", (ArrayList<User>) users);
                            System.out.println("servicesClass.fetchAllUsers().size() "+servicesClass.fetchAllUsers().size());
                            mCtx.startActivity(mapIntent);
                            break;
                        case 1 :
                            Intent completedReports = new Intent(mCtx.getApplicationContext(),CompletedReportsActivity.class);
                            completedReports.putExtra("USER",user);
                            completedReports.putParcelableArrayListExtra("COMPLETED_REPORTS",(ArrayList<Report>) finshedReports);
                            mCtx.startActivity(completedReports);
                    }
                }
            });
        }else {
            List<String> list = new ArrayList<>();
            list.add("Report");
            reportViewHolder.locationName.setText(list.get(i));

            reportViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent reportIntent = new Intent(mCtx.getApplicationContext(),ReportActivity.class);
                    reportIntent.putExtra("USER",user);
                    mCtx.startActivity(reportIntent);
                }
            });
        }
    }
    
    @Override
    public int getItemCount() {
        if (user.userType == 0){
            return  2;
        }else if (user.userType == 2){
            return  1;
        }
        return reports.size();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView descr,phoneNumber,locationName,timeLabel;

        public RelativeLayout relativeLayout;
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            descr = (TextView) itemView.findViewById(R.id.description);
            phoneNumber = (TextView) itemView.findViewById(R.id.phone);
            locationName = (TextView) itemView.findViewById(R.id.locationName);
            timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);

        }
    }
}
