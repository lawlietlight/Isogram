package com.efficientsciences.cowsandbulls.wordwars.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlStorageHelper extends SQLiteOpenHelper {

	    

		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "WORDWARS.db";
	    private static final String USER_TABLE_NAME = "USER";
	    private static final String FRIENDS_TABLE_NAME = "FRIENDS";
	    private static final String TOPICS_SUBSCRIBED_TABLE_NAME = "TOPICSSUBSCRIBED";
	    private static final String TOPICS_USER_CREATED_TABLE_NAME = "TOPICCREATED";

	    private static final String USER_TABLE_CREATE =
	                "CREATE TABLE " + USER_TABLE_NAME + " (" +
	                "userkey" + " INTEGER PRIMARY KEY, " +
	                "username" + " TEXT, " +
	                "soundmuted" + " TEXT, " +
	                "musicmuted" + " TEXT);";

	    SqlStorageHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(USER_TABLE_CREATE);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
	}