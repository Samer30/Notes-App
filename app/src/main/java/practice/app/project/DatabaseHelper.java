package practice.app.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final String TAG = "DatabaseHelper";
    //Database info:
    private static final String DATABASE_NAME = "Tasks.db";
    private static final int DATABASE_VERSION = 1;
    //Tables:
    private static final String TABLE_NAME = "tasks";
    //Columns:
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    //Create table:
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TASK + " TEXT, " + COLUMN_TIMESTAMP + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        }
    }

    public long addItem(Task task) {
        SQLiteDatabase database = getWritableDatabase();
        long ID = -1;
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TASK, task.getTask());
            values.put(COLUMN_TIMESTAMP, task.getTimestamp());
            //Add values to database w/ error handling:
            ID = database.insertOrThrow(TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Unable to add item to database");
        } finally {
            Log.d(TAG, "New Item: " + task.toString());
            database.endTransaction();
        }
        return ID;
    }


    public void updateItem(Task task) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TASK, task.getTask());
            values.put(COLUMN_TIMESTAMP, task.getTimestamp());

            database.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(task.getID())});
            database.setTransactionSuccessful();
            Log.i(TAG, "ITEM UPDATED");
        } catch (Exception e) {
            Log.e(TAG, "Unable to UPDATE item to database");
        } finally {
            Log.d(TAG, task.toString());
            database.endTransaction();
        }

    }

    public void deleteItem(Task task) {
        long ID = task.getID();
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(ID)});
            database.setTransactionSuccessful();
            Log.i(TAG, "ITEM DELETED");
            Log.d(TAG, task.toString());
        } catch (Exception e) {
            Log.d(TAG, "Unable to delete task");
        } finally {
            database.endTransaction();
        }
    }

    public List<Task> getAllItemsFromDatabase() {
        List<Task> list_items = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    task.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                    task.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                    list_items.add(task);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d(TAG, "Unable to get data from local database");
        } finally {
            Log.i(TAG, "Total rows (# of fields) = " + cursor.getCount());
            Log.d(TAG, list_items.toString());
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                Log.i(TAG, "Cursor is closed");
            }
        }
        database.close();
        return list_items;
    }

    public void deleteAllItemsFromDatabase() {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(TABLE_NAME, null, null);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Unable to delete all tasks from database");
        } finally {
            database.endTransaction();
        }
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

}
