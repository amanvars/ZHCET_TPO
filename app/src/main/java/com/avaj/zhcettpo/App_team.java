package com.avaj.zhcettpo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class App_team extends AppCompatActivity {

    //private TextView fb_aman;
    private ImageView fbimg_aman, ldimg_aman;
    private ImageView fbimg_aaku, ldimg_aaku;
    private ImageView img_aaku, img_aman;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       // TextView fb_aman = (TextView) findViewById(R.id.textView2);
       // fb_aman.setMovementMethod(LinkMovementMethod.getInstance());

        fbimg_aman = (ImageView)findViewById(R.id.fbimg_aman);

        fbimg_aman.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://facebook.com/amanvars"));
                startActivity(intent);
            }
        });
        ldimg_aman = (ImageView)findViewById(R.id.ldimg_aman);
        ldimg_aman.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/amanvars"));
                startActivity(intent);
            }
        });

        fbimg_aaku = (ImageView)findViewById(R.id.fbimg_aaku);
        fbimg_aaku.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/profile.php?id=100009785108637"));
                startActivity(intent);
            }
        });
        ldimg_aaku = (ImageView)findViewById(R.id.ldimg_aaku);
        ldimg_aaku.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/aakrati-jain-b86494104"));
                startActivity(intent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
