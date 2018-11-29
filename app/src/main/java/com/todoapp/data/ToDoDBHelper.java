package com.todoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by neerajaabraham on 29/11/18.
 */

public class ToDoDBHelper extends SQLiteOpenHelper {

    private static final String TAG = ToDoDBHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String TODO_LIST_TABLE = "todo_entries";
    private static final String DATABASE_NAME = "todolist";

    public static final String KEY_ID = "_id";
    public static final String KEY_TODO = "todo";
    public static final String KEY_COMPLETED = "isCompleted";


    // Build the SQL query that creates the table.
    private static final String TODO_LIST_TABLE_CREATE =
            "CREATE TABLE " + TODO_LIST_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " + // will auto-increment if no value passed
                    KEY_TODO + " TEXT, " +
                    KEY_COMPLETED + " TEXT);";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public ToDoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Construct ToDoDBHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TODO_LIST_TABLE_CREATE);
        fillDatabaseWithData(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w(ToDoDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TODO_LIST_TABLE);
        onCreate(sqLiteDatabase);

    }


    public void fillDatabaseWithData(SQLiteDatabase db) {

        String[] words = {"Call Mom", "Pray"};

        // Create a container for the data.
        ContentValues values = new ContentValues();

        for (int i = 0; i < words.length; i++) {
            // Put column/value pairs into the container. put() overwrites existing values.
            values.put(KEY_TODO, words[i]);
            db.insert(TODO_LIST_TABLE, null, values);
        }
    }


    public ToDoItem query(int position) {
        String query = "SELECT  * FROM " + TODO_LIST_TABLE +
                " LIMIT " + position + ",1";

        Cursor cursor = null;
        ToDoItem entry = new ToDoItem();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setmId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setmToDo(cursor.getString(cursor.getColumnIndex(KEY_TODO)));
            entry.setChecked(cursor.getInt(cursor.getColumnIndex(KEY_COMPLETED)));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return entry;
        }
    }

    /**
     * Method to insert new to do lists
     *
     * @param word
     * @return
     */
    public long insert(String word) {
        long newId = 0;

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, word);
        values.put(KEY_COMPLETED, 0); //checkbox not checked
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(TODO_LIST_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return newId;
    }


    /**
     * Updates the statement with the supplied id to the supplied value.
     *
     * @param id   Id of the word to update.
     * @param todo The new value of the word.
     * @return The number of rows affected or -1 of nothing was updated.
     */
    public int update(int id, String todo) {
        int mNumberOfRowsUpdated = -1;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put(KEY_TODO, todo);


            mNumberOfRowsUpdated = mWritableDB.update(TODO_LIST_TABLE, //table to change
                    values, // new values to insert
                    KEY_ID + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(id)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d(TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }

    //updating checkbox state

    public int update(int id, int isChecked) {
        int mNumberOfRowsUpdated = -1;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put(KEY_COMPLETED, isChecked);


            mNumberOfRowsUpdated = mWritableDB.update(TODO_LIST_TABLE, //table to change
                    values, // new values to insert
                    KEY_ID + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(id)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d(TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }


    /**
     * Deletes one entry identified by its id.
     *
     * @param id ID of the entry to delete.
     * @return The number of rows deleted. Since we are deleting by id, this should be 0 or 1.
     */
    public int delete(int id) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            deleted = mWritableDB.delete(TODO_LIST_TABLE, //table name
                    KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d(TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return deleted;
    }


    public long count() {
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, TODO_LIST_TABLE);
    }
}
