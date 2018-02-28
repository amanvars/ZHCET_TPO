
package com.avaj.zhcettpo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "2374576_tpo.db";

	// Login table name
	private static final String TABLE_USER = "user_info";

	// Login Table Columns names
	private static final String KEY_ROWID="id";
	private static final String KEY_NAME = "name";
	private static final String KEY_ENROL = "enrol";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_FAC_NO = "faculty_no";
	private static final String KEY_TITLE = "title";
	private static final String KEY_FEED = "feed";
	private static final String KEY_DATE = "date";
    String  enrol_no;

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_NAME + " TEXT,"
				+ KEY_ENROL + " TEXT," + KEY_EMAIL + " TEXT,"
				+KEY_FAC_NO + " TEXT" +")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + KEY_ENROL);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
    public void AddUserTable(String TableName){
    /*At first you will need a Database object.Lets create it.*/
    //TableName = "gf1266";
        SQLiteDatabase ourDatabase=this.getWritableDatabase();
        String feed_table = "CREATE TABLE IF NOT EXISTS " + TableName+ " ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " TEXT , "
                + KEY_FEED + " TEXT,"
                + KEY_DATE + " TEXT"
                +")";

    /*then call 'execSQL()' on it. Don't forget about using TableName Variable as tablename.*/
        ourDatabase.execSQL(feed_table);
    }

    public void AddFbUserTable(String TableName){
    /*At first you will need a Database object.Lets create it.*/

        SQLiteDatabase ourDatabase=this.getWritableDatabase();
        String feed_table = "CREATE TABLE IF NOT EXISTS " + TableName+ " ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " TEXT , "
                + KEY_FEED + " TEXT,"
                + KEY_DATE + " TEXT"
                +")";

    /*then call 'execSQL()' on it. Don't forget about using TableName Variable as tablename.*/
        ourDatabase.execSQL(feed_table);
    }

	public void addUser(String name, String enrol,String email,String facno) {
		SQLiteDatabase db = this.getWritableDatabase();

      //  enrol_no = enrol;

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_ENROL,enrol); // Enrol
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_FAC_NO, facno); // Email
	//	values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**Add user feeds**/

    public void addFeed( String enrol, String title, String feed, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title); // Name
        values.put(KEY_FEED,feed); // Enrol
        values.put(KEY_DATE,date); // Enrol
        //values.put(KEY_EMAIL, email); // Email
        //	values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(enrol, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New feed inserted into sqlite: " + id);
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {

            user.put("name", cursor.getString(0));
            user.put("enrol", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("facno", cursor.getString(3));
            //	user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    /**
     * Getting user data from database
     * */
    public ArrayList<HashMap<String, String>> getFeed(String enrol_no) {
        ArrayList<HashMap<String,String>> feedList = new ArrayList<HashMap<String,String>>();
       // HashMap<String, String> feed = new HashMap<String, String>();

        String selectQuery = "SELECT  * FROM " + enrol_no;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
       // cursor.moveToFirst();

        try {
            while (cursor.moveToNext()) {
                HashMap<String, String> feed = new HashMap<String, String>();
                feed.put("feed_rowid", cursor.getString(0));
                feed.put("feed_title", cursor.getString(1));
                feed.put("feed_message", cursor.getString(2));
                feed.put("feed_date", cursor.getString(3));

                feedList.add(feed);
            }
        }
        finally {
                cursor.close();
            }
       // cursor.close();
        db.close();
        // return user
        //Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return feedList;
    }

    /**
     * Getting user's particular feed from database
     * */
    public HashMap<String, String> getparticularFeed(String enrol_no,String rowid) {
        HashMap<String,String> feed = new HashMap<String,String>();
        int id = Integer.parseInt(rowid);
        String selectQuery = "SELECT  * FROM " + enrol_no + " WHERE "
                + KEY_ROWID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        // cursor.moveToFirst();

        try {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                feed.put("feed_rowid", cursor.getString(0));
                feed.put("feed_title", cursor.getString(1));
                feed.put("feed_message", cursor.getString(2));
                feed.put("feed_date", cursor.getString(3));
            }

        }finally {
            cursor.close();
        }
        // cursor.close();
        db.close();
        // return user
        //Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return feed;
    }

    /*remove particular feed*/
    public void removeSingleFeed(String rowid,String enrol) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        int id = Integer.parseInt(rowid);
        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + enrol + " WHERE " + KEY_ROWID + "=" + id );

        //Close the database
        database.close();
    }

    /**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
