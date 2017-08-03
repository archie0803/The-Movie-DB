package com.example.android.mymoviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archita on 04-08-2017.
 */

public class AddFavOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "favourites_db";
    private static final String TABLE_FAVOURITES = "favourites_table";

    private static final String _ID = "id";
    private static final String COLUMN_MOVIE_ID = "movie_id";

    public AddFavOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_FAVOURITES + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MOVIE_ID + " REAL UNIQUE NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        onCreate(sqLiteDatabase);
    }

    public void addToFavourites(String id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_ID, id);

        db.insert(TABLE_FAVOURITES, null, contentValues);
        db.close();
    }

    public boolean isFavourite(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITES, new String[]{COLUMN_MOVIE_ID}, COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() == 1)
            return true;
        else
            return false;
    }

    public void removeFromFavourites(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FAVOURITES, COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<String> getAllFavourites() {
        ArrayList<String> favouritesList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITES;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                favouritesList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return favouritesList;
    }
}
