package com.avaj.zhcettpo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.avaj.zhcettpo.helper.SQLiteHandler;
import com.avaj.zhcettpo.helper.SessionManager;

import java.util.HashMap;

public class View_feed extends AppCompatActivity {
    private SQLiteHandler db;
    private SessionManager session;
    private TextView title,msg,date ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title = (TextView)findViewById(R.id.feed_msg1);
        msg = (TextView)findViewById(R.id.feed_msg2);
        date = (TextView)findViewById(R.id.feed_msg3);

        try {
            Bundle extras = getIntent().getExtras();
            if(extras != null)
            {
                String id = extras.getString("rowid");


                // session manager
                session = new SessionManager(getApplicationContext());
                String enrol = session.session_enrol();

                if(session.isFBLoggedIn())
                    enrol = session.session_uid();


                // SQLite database handler
                db = new SQLiteHandler(getApplicationContext());
                HashMap<String,String> feed = db.getparticularFeed(enrol,id);

                title.setText(feed.get("feed_title"));
                msg.setText(feed.get("feed_message"));
                date.setText(feed.get("feed_date"));



            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
