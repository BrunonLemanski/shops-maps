package com.brunonlemanski.shops.database;

import android.provider.BaseColumns;

/**
 * Connecting with SQLite Database
 *
 * @author Brunon Lemański
 */
public class DbAdapter {

    /**
     * Constructor - must be empty.
     */
    public DbAdapter() {
    }


    public static final class DbEntry implements BaseColumns {

        /**
         * Fields in database
         */
        public static final String TABLE_NAME = "shoplist";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_RADIUS = "radius";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
