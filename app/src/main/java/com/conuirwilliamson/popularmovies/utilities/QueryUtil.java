package com.conuirwilliamson.popularmovies.utilities;

import android.support.annotation.NonNull;

import com.conuirwilliamson.popularmovies.data.FavouriteMoviesContract.*;

/**
 * Created by conuirwilliamson on 29/03/2017.
 */

public class QueryUtil {

    public static String getSQLDropTable(@NonNull String tableName, boolean ifExists){
        return "DROP TABLE " + (ifExists ? "IF EXISTS" : "") + tableName;
    }

    public static String getSQLCreateTable(@NonNull String tableName, @NonNull String[] columnNames, @NonNull String[] columnDefinitions){
        if(columnNames.length == 0) throw new RuntimeException("At least 1 column is required to create a table");
        if(columnNames.length != columnDefinitions.length) throw new RuntimeException("Different number of column names to definitions.");

        int columnIdx = 0;
        String query = "CREATE TABLE " +
                tableName.trim() + " (";
        query += columnNames[columnIdx] + " " + columnDefinitions[columnIdx++];
        if(columnNames.length > 1){
            for(;columnIdx < columnNames.length;){
                query += ", " + columnNames[columnIdx] + " " + columnDefinitions[columnIdx++];
            }
        }
        return query + ");";
    }

    public static String getSQLCreateOnUpdateUpdatedTrigger(String tableName) {
        return  "CREATE TRIGGER [UpdateLastTime] " +
                "AFTER UPDATE " +
                "ON " + tableName + " " +
                "FOR EACH ROW " +
                "BEGIN " +
                "UPDATE " + tableName + " SET " + FavouriteMoviesTable.COLUMN_UPDATED + " = CURRENT_TIMESTAMP;" +
                "END;";
    }
}