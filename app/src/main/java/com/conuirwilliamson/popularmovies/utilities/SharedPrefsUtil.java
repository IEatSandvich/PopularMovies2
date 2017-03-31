package com.conuirwilliamson.popularmovies.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.activities.MainActivity;

import java.util.Map;
import java.util.Set;

/**
 * Created by conuirwilliamson on 29/03/2017.
 */

@SuppressLint("CommitPrefEdits")
public class SharedPrefsUtil {
    private SharedPrefsUtil(){}


    private static SharedPreferences.Editor getEditor(Context context){
        return getSharedPreferences(context).edit();
    }
    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void register(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener){
        getSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener);
    }
    public static void unregister(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener){
        getSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static boolean contains(Context context, String key){
        return getSharedPreferences(context).contains(key);
    }
    public static Map<String, ?> getAll(Activity context){
        return getSharedPreferences(context).getAll();
    }
    public static boolean getBoolean(Context context, String key, boolean defaultValue){
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }
    public static float getFloat(Context context, String key, float defaultValue){
        return getSharedPreferences(context).getFloat(key, defaultValue);
    }
    public static int getInt(Context context, String key, int defaultValue){
        return getSharedPreferences(context).getInt(key, defaultValue);
    }
    public static long getLong(Context context, String key, long defaultValue){
        return getSharedPreferences(context).getLong(key, defaultValue);
    }
    public static String getString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }
    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValues) {
        return getSharedPreferences(context).getStringSet(key, defaultValues);
    }

    /*************************
     * Modifying preferences *
     *************************/
    public static void clear(Context context){
        getEditor(context).clear().apply();
    }
    public static void remove(Context context, String key){
        getEditor(context).remove(key).apply();
    }
    public static SharedPreferences.Editor putBoolean(Context context, String key, boolean value){
        return getEditor(context).putBoolean(key, value);
    }
    public static SharedPreferences.Editor putFloat(Context context, String key, float value){
        return getEditor(context).putFloat(key, value);
    }
    public static SharedPreferences.Editor putInt(Context context, String key, int value){
        return getEditor(context).putInt(key, value);
    }
    public static SharedPreferences.Editor putLong(Context context, String key, long value){
        return getEditor(context).putLong(key, value);
    }
    public static SharedPreferences.Editor putString(Context context, String key, String value) {
        return getEditor(context).putString(key, value);
    }
    public static SharedPreferences.Editor putStringSet(Context context, String key, Set<String> values) {
        return getEditor(context).putStringSet(key, values);
    }

    public static MainActivity.SearchType getPreferredSearchType(Context context){
        String pref_sort = getString(context,context.getString(R.string.pref_sort_key),context.getString(R.string.pref_sort_default_value));

        if(pref_sort.equals(context.getString(R.string.pref_sort_most_popular_value))){
            return MainActivity.SearchType.MostPopular;
        }
        if(pref_sort.equals(context.getString(R.string.pref_sort_top_rated_value))){
            return MainActivity.SearchType.TopRated;
        }
        if(pref_sort.equals(context.getString(R.string.pref_sort_favorited_value))){
            return MainActivity.SearchType.Favorited;
        }

        // Check: Make sure this matches pref_sort_default_value in strings.xml
        return MainActivity.SearchType.MostPopular;
    }
}
