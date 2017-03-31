package com.conuirwilliamson.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract.FavouriteMoviesTable;
import com.conuirwilliamson.popularmovies.utilities.QueryUtil;

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    public PopularMoviesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QueryUtil.getSQLCreateTable(
                FavouriteMoviesTable.TABLE_NAME,
                FavouriteMoviesTable.COLUMN_NAMES,
                FavouriteMoviesTable.COLUMN_DEFINITIONS));
        db.execSQL(QueryUtil.getSQLCreateOnUpdateUpdatedTrigger(FavouriteMoviesTable.TABLE_NAME));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(QueryUtil.getSQLDropTable(
                FavouriteMoviesTable.TABLE_NAME,
                true));
        db.execSQL(QueryUtil.getSQLCreateTable(
                FavouriteMoviesTable.TABLE_NAME,
                FavouriteMoviesTable.COLUMN_NAMES,
                FavouriteMoviesTable.COLUMN_DEFINITIONS));
    }
}
