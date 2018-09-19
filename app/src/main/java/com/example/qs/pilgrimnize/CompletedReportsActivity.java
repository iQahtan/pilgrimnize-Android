package com.example.qs.pilgrimnize;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CompletedReportsActivity extends AppCompatActivity {

    private List<Report> reports = new ArrayList<>();
    private FinshedReportsAdapter adapter;
    private RecyclerView recyclerView;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_completed_reports);

        recyclerView = findViewById(R.id.finshedReportsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reports = getIntent().getParcelableArrayListExtra("COMPLETED_REPORTS");
        user = getIntent().getParcelableExtra("USER");
        System.out.println("reports.get(0) "+ reports.get(0).endDate +" "+reports.get(0).createionDate+" "+reports.get(0).getEndDate()+" "+reports.get(0).getEndDate());
        System.out.println("reports.get(0) "+ reports.get(0).describe +" "+reports.get(0).getDescribe()+" "+reports.get(0).getLocationName()+" "+reports.get(0).reportedUser);
        System.out.println("Enter here "+reports);

        adapter = new FinshedReportsAdapter(this,reports);
        recyclerView.setAdapter(adapter);
        System.out.println(" fkfkfkf"+reports);
    }
}
