package com.conuirwilliamson.popularmovies.utilities;

import android.util.DisplayMetrics;

/**
 * Created by conuirwilliamson on 31/03/2017.
 */

public class UIUtil {

    public static double getDpFromPx(DisplayMetrics metrics, double pixels){
        return pixels / metrics.density;
    }

    // If the width / height ratio is greater than 1.5, (long enough to show a few movies side by side, and ultimately being wider than is it tall),
    // and also with a height less than 500 so images are stretched too tall (i.e. there's just not enough room to show stacked images), then
    // return true
    public static boolean displayLinearLayoutMovies(DisplayMetrics metrics){
        double dpHeight;
        return (getDpFromPx(metrics, metrics.widthPixels) / (dpHeight = getDpFromPx(metrics, metrics.heightPixels))) > 1.5 && dpHeight < 500;
    }

    // Returns true if the screen is wider than it is tall
    public static boolean isWiderThanIsTall(DisplayMetrics metrics){
        return getDpFromPx(metrics, metrics.widthPixels) > getDpFromPx(metrics, metrics.heightPixels);
    }

    // TODO: 01/04/2017 - Priority: 0 - Appropriately size Trailer thumbnails based on the size of the screen, be it via qualified dimens.xml files or by a method in this class

}
