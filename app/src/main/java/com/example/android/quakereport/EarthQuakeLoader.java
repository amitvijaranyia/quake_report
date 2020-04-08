package com.example.android.quakereport;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    private String mUrl;
    public EarthQuakeLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<EarthQuake> loadInBackground() {
        if(mUrl == null)
            return null;
        List<EarthQuake> earthQuakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthQuakes;
    }
}
