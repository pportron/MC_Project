package com.mobilecomputing.mc_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.database.Cursor;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "ReminderDB";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "Reminder";

    // below variable is for our message column.
    private static final String MSG_COL = "Message";

    // below variable is for our rmd time column.
    private static final String TIME_COL = "reminder_time";

    // below variable is for our creation time column
    private static final String CREATION_COL = "creation_time";

    // below variable id for our creator id column.
    private static final String CREATOR_COL = "creator_id";

    // below variable for our reminder seen column.
    private static final String SEEN_COL = "reminder_seen";

    // below variable is for our x location column.
    private static final String LOCX_COL = "location_x";

    // below variable is for our y location column.
    private static final String LOCY_COL = "location_y";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + MSG_COL + " TEXT PRIMARY KEY, "
                + TIME_COL + " TEXT,"
                + CREATION_COL + " TEXT,"
                + CREATOR_COL + " TEXT,"
                + SEEN_COL + " int,"
                + LOCX_COL + " double,"
                + LOCY_COL + " double)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewReminder(String message, String reminder_time, String creation_time, String creator_id, int reminder_seen, double location_x, double location_y) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(MSG_COL, message);
        values.put(TIME_COL, reminder_time);
        values.put(CREATION_COL, creation_time);
        values.put(CREATOR_COL, creator_id);
        values.put(SEEN_COL, reminder_seen);
        values.put(LOCX_COL, location_x);
        values.put(LOCY_COL, location_y);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public void deleteReminder(String message) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "name=?", new String[]{message});
        db.close();
    }

    public void updateReminder(String originalmessage, String message, String reminder_time, String creation_time, String creator_id, int reminder_seen, double location_x, double location_y) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(MSG_COL, message);
        values.put(TIME_COL, reminder_time);
        values.put(CREATION_COL, creation_time);
        values.put(CREATOR_COL, creator_id);
        values.put(SEEN_COL, reminder_seen);
        values.put(LOCX_COL, location_x);
        values.put(LOCY_COL, location_y);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE_NAME, values, "name=?", new String[]{originalmessage});
        db.close();
    }
    public String[] GetDataString(String colname) {
        String[] result = new String[100];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,new String[]{MSG_COL,TIME_COL,CREATION_COL,CREATOR_COL,SEEN_COL,LOCX_COL,LOCY_COL},colname,null,null,null,null);

        int icol = c.getColumnIndex(colname);
        int i = 0;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            result[i] = c.getString(icol);
            i++;
        }

        return result;
    }

    public int[] GetDataInt(String colname) {
        int result[] = new int[100];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,new String[]{MSG_COL,TIME_COL,CREATION_COL,CREATOR_COL,SEEN_COL,LOCX_COL,LOCY_COL},colname,null,null,null,null);

        int icol = c.getColumnIndex(colname);
        int i = 0;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            result[i] = c.getInt(icol);
            i++;
        }

        return result;
    }

    public double[] GetDataDouble(String colname) {
        double result[] = new double[100];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,new String[]{MSG_COL,TIME_COL,CREATION_COL,CREATOR_COL,SEEN_COL,LOCX_COL,LOCY_COL},colname,null,null,null,null);

        int icol = c.getColumnIndex(colname);
        int i = 0;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            result[i] = c.getDouble(icol);
            i++;
        }

        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
