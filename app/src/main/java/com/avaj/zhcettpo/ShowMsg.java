package com.avaj.zhcettpo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.avaj.zhcettpo.helper.SQLiteHandler;
import com.avaj.zhcettpo.helper.SessionManager;

import org.json.JSONObject;

public class ShowMsg extends AppCompatActivity {

    private static final String TAG = ShowMsg.class.getSimpleName();

    private TextView title,msg,date;
    private JSONObject data;
    private SQLiteHandler db;
    private SessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView)findViewById(R.id.shw_msg1);
        msg = (TextView)findViewById(R.id.shw_msg2);
        date = (TextView)findViewById(R.id.shw_msg3);


        try {
            Bundle extras = getIntent().getExtras();
            if(extras != null)
            {
                String t = extras.getString("title");
                String m = extras.getString("msg");
                String d = extras.getString("date_time");
                title.setText(t);
                msg.setText(m);
                date.setText(d);

                // session manager
                session = new SessionManager(getApplicationContext());
                db = new SQLiteHandler(getApplicationContext());


                if(session.isFBLoggedIn()) {
                    String enrol = session.session_uid();
                  //  db = new SQLiteHandler(getApplicationContext());

                    db.addFeed(enrol, t, m, d);
                }
                else if(session.isLoggedIn())
                {
                    String enrol = session.session_enrol();

                    db.addFeed(enrol,t,m,d);
                }
                // SQLite database handler
                Toast.makeText(getApplicationContext(),
                        "News Saved", Toast.LENGTH_LONG).show();

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
