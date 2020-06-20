package restaurent.menu.demo.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import restaurent.menu.demo.helper.MenuItem;


public class SQLiteDatabaseHelper extends SQLiteOpenHelper
{

    Context context;

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "RestaurentDemo";


    public static final String TABLE_ITEM = "item";


    // Common column names
    private static final String KEY_SYNC_STATUS = "sync_status";


    // Complain table column names
    public static final String KEY_ID = "id";
    private static final String KEY_ITEM_TYPE = "type";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";



    // Complain table create statements
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
            + TABLE_ITEM + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_TYPE + " TEXT," + KEY_NAME
            + " TEXT," + KEY_PRICE + " REAL)";



    public SQLiteDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase database)
    {

        database.execSQL(CREATE_TABLE_ITEM);
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version)
    {

        database.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ITEM);

        // create new tables
        onCreate(database);
    }


    // Insert user information
    public boolean insertItem(MenuItem item)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, item.getId());
        values.put(KEY_ITEM_TYPE, item.getItemType());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_PRICE, item.getPrice());

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_ITEM, null, values) > 0;

        // Closing database connection
        database.close();

        return createSuccessful;
    }


    // Get All Item Types
    public ArrayList<MenuItem> getAllItemType()
    {

        ArrayList<MenuItem> itemList = new ArrayList<MenuItem>();

        String selectQuery = "SELECT DISTINCT " + KEY_ITEM_TYPE + " FROM " + TABLE_ITEM + " ORDER BY " + KEY_ITEM_TYPE + " ASC";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);


        if (cursor.moveToFirst())
        {

            do
            {

                MenuItem item = new MenuItem();

                item.setItemType(cursor.getString(0));

                itemList.add(item);
            }

            while (cursor.moveToNext());
        }

        database.close();

        return itemList;
    }


    // Get All Complains
    public ArrayList<MenuItem> getAllItem(String item_type)
    {

        ArrayList<MenuItem> itemList = new ArrayList<MenuItem>();

        String selectQuery = "SELECT " + KEY_ID + "," + KEY_NAME + "," + KEY_PRICE + " FROM " + TABLE_ITEM + " WHERE "
                            + KEY_ITEM_TYPE + "='" + item_type + "'" + " ORDER BY " + KEY_ITEM_TYPE + " ASC";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);


        if (cursor.moveToFirst())
        {

            do
            {

                MenuItem item = new MenuItem();

                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setPrice(Float.valueOf(cursor.getFloat(2)));

                itemList.add(item);
            }

            while (cursor.moveToNext());
        }

        database.close();

        return itemList;
    }


    // Compose JSON from Market Survey table
    /*public String composeJSONfromSQLiteComplain()
    {

        ArrayList<HashMap<String, String>> wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT " + KEY_COMPLAIN_ID + ", " +  KEY_REPORT + ", " + KEY_STATUS+ ", "
                + KEY_STATUS_DATETIME + " FROM " + TABLE_COMPLAIN + " WHERE " + KEY_SYNC_STATUS + " = 'no'";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);


        if (cursor.moveToFirst())
        {

            do
            {

                HashMap<String, String> map = new HashMap<String, String>();

                map.put(KEY_COMPLAIN_ID, cursor.getString(0));
                map.put(KEY_REPORT, cursor.getString(1));
                map.put(KEY_STATUS, cursor.getString(2));
                map.put(KEY_STATUS_DATETIME, cursor.getString(3));

                wordList.add(map);
            }

            while (cursor.moveToNext());
        }

        database.close();
        Gson gson = new GsonBuilder().create();

        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }*/



    // Sync All Complain Images
    /*public ArrayList<Complain> syncComplainImages()
    {

        ArrayList<Complain> complainList = new ArrayList<Complain>();

        String selectQuery = "SELECT * FROM " + TABLE_COMPLAIN_IMAGES + " WHERE " + KEY_SYNC_STATUS + " = '" + "no" + "'";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);


        if (cursor.moveToFirst())
        {

            do
            {

                Complain complain = new Complain();

                complain.setComplainId(cursor.getString(0));
                complain.setImagePath(cursor.getString(1));

                complainList.add(complain);
            }

            while (cursor.moveToNext());
        }

        database.close();

        return complainList;
    }*/



    /*public void updateComplaint(Complain complain)
    {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_COMPLAIN + " SET " + KEY_REPORT + " = '" + complain.getReport() + "', "
                + KEY_STATUS + " = '" + complain.getStatus() + "', " + KEY_STATUS_DATETIME + " = '" + complain.getStatusDatetime()  + "', "
                + KEY_SYNC_STATUS + " ='no' WHERE " + KEY_COMPLAIN_ID + " = '" + complain.getComplainId() + "'";

        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }*/



    // Count no of rows to be synchronize
    /*public int dbSyncCount(String TABLE_NAME)
    {

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_SYNC_STATUS + " = '" + "no" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        database.close();

        return count;
    }*/



    // Count no of rows
    public int dbRowCount(String TABLE_NAME)
    {

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        database.close();

        return count;
    }


    // Update synchronization status
    /*public void updateSyncStatus(String id, String status, String TABLE_NAME, String COLUMN_NAME)
    {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_NAME + " SET " + KEY_SYNC_STATUS + " = '" + status + "' WHERE " + COLUMN_NAME + " = '" + id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }*/


    // Delete all records from table
    /*public void deleteRecord(String TABLE_NAME)
    {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "DELETE FROM " + TABLE_NAME;
        Log.d("query",updateQuery);
        database.execSQL("PRAGMA foreign_keys=ON");
        database.execSQL(updateQuery);
        database.close();
    }*/
}