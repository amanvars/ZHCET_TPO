package com.avaj.zhcettpo;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avaj.zhcettpo.app.Config;
import com.avaj.zhcettpo.app.Team;
import com.avaj.zhcettpo.helper.SQLiteHandler;
import com.avaj.zhcettpo.helper.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtEnrol;
    private Button btnLogout;
    private String urlProfileImg;

    JSONObject response, profile_pic_data, profile_pic_url;
    // To hold Facebook profile picture
    private ImageView profilePicture;
    private Firebase myFirebaseRef;
    private FirebaseAuth mAuth;
    private String bfs;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    private SQLiteHandler db;
    private SessionManager session;

    private View headerView;
    private ImageView drawerImage;
    private TextView drawerUsername;
    private TextView drawerEnrol;
    private String name, email, enrol, facno, uid, imageUrl;
    private ArrayList<HashMap<String, String>> feeds;
    private HashMap<String, String> user;
    private NavigationView navigationView;

    // variables for card view and recycler view
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Creates a reference for  your Firebase database
        //Add YOUR Firebase Reference URL instead of the following URL
        myFirebaseRef = new Firebase("https://zhcet-tpo.firebaseio.com/users/");

        mAuth = FirebaseAuth.getInstance();



        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.avaj.zhcettpo", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "ZHCET TPO");
                    String sAux = "ZHCET TPO\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + " \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // card view and recyler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());



      /*  if (!session.isLoggedIn() && session.isFBLoggedIn()) {
            session.setLogin(false);
            //logoutUser();
        }
      else if (!session.isFBLoggedIn() && session.isLoggedIn()) {
           session.setFBLogin(false);
                //logoutUser();
            }*/
    /*     if (!session.isFBLoggedIn() && !session.isLoggedIn()) {
       //session.setFBLogin(false);
       //session.setLogin(false);
            logoutUser();
        }
*/


        //  FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_BRANCH);


        headerView = navigationView.getHeaderView(0);
        drawerImage = (ImageView) headerView.findViewById(R.id.img_drawable);
        drawerUsername = (TextView) headerView.findViewById(R.id.name_drawable);
        drawerEnrol = (TextView) headerView.findViewById(R.id.enrol_drawable);


        try {

            if (session.isLoggedIn()) {
                session.setFBLogin(false);
                user = db.getUserDetails();

                if (user != null) {

                    name = user.get("name");
                    enrol = user.get("enrol");
                    email = user.get("email");
                    facno = user.get("facno");

                    // storing details in shared preferences
                    session.setDetails(enrol);


                    try {
                        String result = facno.substring(2, facno.length() - 3);

                        FirebaseInstanceId.getInstance().getToken();
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        if (result.equals("PEB"))
                            FirebaseMessaging.getInstance().subscribeToTopic("computer");
                        else if (result.equals("LEB"))
                            FirebaseMessaging.getInstance().subscribeToTopic("electronics");
                        else if (result.equals("CEB"))
                            FirebaseMessaging.getInstance().subscribeToTopic("civil");
                        else if (result.equals("EEB"))
                            FirebaseMessaging.getInstance().subscribeToTopic("electrical");
                        else if (result.equals("KEB"))
                            FirebaseMessaging.getInstance().subscribeToTopic("chemical");
                        else if (result.equals("MEB"))
                            FirebaseMessaging.getInstance().subscribeToTopic("mechanical");
                        else if (result.equals("PKB"))
                            FirebaseMessaging.getInstance().subscribeToTopic("petro");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        urlProfileImg = "http://tpozhcet.in/picture/" + MD5(facno) + ".jpg";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Glide.with(this).load(urlProfileImg)
                            .placeholder(R.drawable.man_team)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(drawerImage);

                    drawerUsername.setText(name);
                    feeds = db.getFeed(user.get("enrol"));


                    drawerEnrol.setText(enrol.toUpperCase() + " | " + facno);
                }

                Menu menu = navigationView.getMenu();
                menu.findItem(R.id.nav_logout).setVisible(true);


            }

            else if (session.isFBLoggedIn())

            {
                session.setLogin(false);

                user = db.getUserDetails();

                if (user != null) {

                    name = user.get("name");
                    uid = user.get("enrol");
                    email = user.get("email");
                    imageUrl = user.get("facno");

                    new ImageLoadTask(imageUrl, profilePicture).execute();

                    enrol = uid;


                    FirebaseInstanceId.getInstance().getToken();
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    FirebaseMessaging.getInstance().subscribeToTopic("computer");

                    FirebaseMessaging.getInstance().subscribeToTopic("electronics");

                    FirebaseMessaging.getInstance().subscribeToTopic("civil");
                    FirebaseMessaging.getInstance().subscribeToTopic("electrical");
                    FirebaseMessaging.getInstance().subscribeToTopic("chemical");
                    FirebaseMessaging.getInstance().subscribeToTopic("mechanical");
                    FirebaseMessaging.getInstance().subscribeToTopic("petro");

                    drawerUsername.setText(name);

                    Glide.with(this).load(imageUrl)
                            .placeholder(R.drawable.man_team)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(drawerImage);


                    drawerEnrol.setText(email);
                    feeds = db.getFeed(enrol);

                    Menu menu = navigationView.getMenu();
                    menu.findItem(R.id.nav_logout).setVisible(true);

                }




            }

            mAdapter = new MyRecyclerViewAdapter(this, feeds);
            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String MD5(String facno) throws Exception {


        String md5 = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(facno.getBytes(), 0, facno.length());
            md5 = new BigInteger(1, m.digest()).toString(16);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return md5;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
            // return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent i = new Intent(MainActivity.this, About.class);
            startActivity(i);
            // finish();
        } else if (id == R.id.nav_team) {
            Intent i = new Intent(MainActivity.this, Team.class);
            startActivity(i);
            //mAuth.signOut();
            //finish();

        } else if (id == R.id.nav_dev) {
            Intent i = new Intent(MainActivity.this, App_team.class);
            startActivity(i);

        } else if (id == R.id.nav_rate) {

            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }

        } else if (id == R.id.nav_logout) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {

        if (session.isFBLoggedIn()) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();
        }

        session.setLogin(false);

        session.setFBLogin(false);


        db.deleteUsers();
        //FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        FirebaseInstanceId.getInstance().getToken();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        // FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
        startActivity(intent);
        finish();
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            //drawerImage.setImageBitmap(result);
        }

    }


}
