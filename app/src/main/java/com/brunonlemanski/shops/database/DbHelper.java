package com.brunonlemanski.shops.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.brunonlemanski.shops.database.DbAdapter.*;

public class DbHelper extends SQLiteOpenHelper {

    //------------------------------------------------------ Constants
    /**
     * Database name.
     */
    public static final String DATABASE_NAME = "shopslist.db";

    /**
     * Database version.
     */
    public static final int DATABASE_VERSION = 1;


    //------------------------------------------------------ Constructor
    /**
     * Class constructor.
     * @param context
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //------------------------------------------------------ Methods
    /**
     * Create method - creating table in database.
     * @param db Your database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_SHOPLIST_TABLE = "CREATE TABLE " +
                DbEntry.TABLE_NAME + " (" +
                DbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DbEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                DbEntry.COLUMN_RADIUS + " TEXT NOT NULL, " +
                DbEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                DbEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_SHOPLIST_TABLE);
    }

    /**
     * Update method - updating line in database.
     * @param db Your database.
     * @param oldVersion Database before changes.
     * @param newVersion Database after changes.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DbEntry.TABLE_NAME);
        onCreate(db);
    }
}
