package rs.elfak.mosis.stele.myplaces.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyPlacesDBAdapter {
    public static final String DATABASE_NAME = "MyPlacesDB";
    public static final String DATABASE_TABLE = "Places";
    public static final int DATABASE_VERSION = 1;

    public static final String PLACE_ID = "id";
    public static final String PLACE_NAME = "name";
    public static final String PLACE_DESCRIPTION = "description";
    public static final String PLACE_LONG = "longitude";
    public static final String PLACE_LAT = "latitude";

    private SQLiteDatabase db;
    private final Context context;
    private MyPlacesDatabaseHelper dbHelper;

    public MyPlacesDBAdapter(Context context) {
        this.context=context;
        dbHelper = new MyPlacesDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MyPlacesDBAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        //Log.e("db u open", db.toString());
        return this;
    }

    public void close()
    {
        db.close();
    }

    public long insertEntry(MyPlace place)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_NAME, place.getName());
        contentValues.put(PLACE_DESCRIPTION, place.getDescription());
        contentValues.put(PLACE_LONG, place.getLongitude());
        contentValues.put(PLACE_LAT, place.getLatitude());

        long id = -1;

        db.beginTransaction();
        try {
            id = db.insert(DATABASE_TABLE, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex)
        {
            Log.e("MyPlacesDBAdapter", ex.getMessage());
        }
        finally {
            db.endTransaction();
        }

        return id;
    }

    public boolean removeEntry(long id)
    {
        boolean success = false;
        db.beginTransaction();
        try {
            success = db.delete(DATABASE_TABLE, PLACE_ID + "=" + id, null) > 0;
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex)
        {
            Log.v("MyPlacesDBAdapter", ex.getMessage());
        }
        finally {
            db.endTransaction();
        }
        return success;
    }

    public ArrayList<MyPlace> getAllEntries()
    {
        ArrayList<MyPlace> myPlaces = null;
        Cursor cursor = null;
        db.beginTransaction();

        try {
            cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex)
        {
            Log.v("MyPlacesDBAdapter", ex.getMessage());
        }
        finally {
            db.endTransaction();
        }

        if(cursor != null)
        {
            myPlaces = new ArrayList<>();
            MyPlace myPlace = null;
            while (cursor.moveToNext())
            {
                myPlace = new MyPlace(cursor.getString(cursor.getColumnIndex(PLACE_NAME)), cursor.getString(cursor.getColumnIndex(PLACE_DESCRIPTION)));
                myPlace.setId(cursor.getLong(cursor.getColumnIndex(PLACE_ID)));
                myPlace.setLongitude(cursor.getString(cursor.getColumnIndex(PLACE_LONG)));
                myPlace.setLatitude(cursor.getString(cursor.getColumnIndex(PLACE_LAT)));

                myPlaces.add(myPlace);
            }
        }
        return myPlaces;
    }

    public MyPlace getEntry(long id)
    {
        MyPlace myPlace = null;
        Cursor cursor = null;
        db.beginTransaction();

        try {
            cursor = db.query(DATABASE_TABLE, null, PLACE_ID + "=" + id, null, null, null, null);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex)
        {
            Log.v("MyPlacesDBAdapter", ex.getMessage());
        }
        finally {
            db.endTransaction();
        }

        if(cursor != null)
        {
            if (cursor.moveToFirst())
            {
                myPlace = new MyPlace(cursor.getString(cursor.getColumnIndex(PLACE_NAME)), cursor.getString(cursor.getColumnIndex(PLACE_DESCRIPTION)));
                myPlace.setId(cursor.getLong(cursor.getColumnIndex(PLACE_ID)));
                myPlace.setLongitude(cursor.getString(cursor.getColumnIndex(PLACE_LONG)));
                myPlace.setLatitude(cursor.getString(cursor.getColumnIndex(PLACE_LAT)));
            }
        }
        return myPlace;
    }

    public int updateEntry(long id, MyPlace place)
    {
        String where = PLACE_ID + "=" + id;

        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_NAME, place.getName());
        contentValues.put(PLACE_DESCRIPTION, place.getDescription());
        contentValues.put(PLACE_LONG, place.getLongitude());
        contentValues.put(PLACE_LAT, place.getLatitude());

        return db.update(DATABASE_TABLE, contentValues, where, null);
    }
}
