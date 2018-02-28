package com.avaj.zhcettpo.app;

/**
 * Created by aman_AV on 30-07-2017.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avaj.zhcettpo.R;
import com.avaj.zhcettpo.adapter.TeamAdapter;
import com.avaj.zhcettpo.model.ListDetails;
import com.avaj.zhcettpo.model.Model;

import java.util.ArrayList;

/**
 * Created by aakra on 7/26/2017.
 */

public class Team extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Model> models;
    private TeamAdapter teamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Our Team");

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=(ListView) findViewById(R.id.list_view);
        models= ListDetails.getList();

        teamAdapter=new TeamAdapter(Team.this,models);
        listView.setAdapter(teamAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Model model=models.get(position);
                Toast.makeText(Team.this, model.getName() + ": " + model.getBranch(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}