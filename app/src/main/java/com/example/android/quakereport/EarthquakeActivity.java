package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class EarthquakeActivity extends AppCompatActivity implements androidx.loader.app.LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    ListView lvEarthQuakeReport;
    List<EarthQuake> earthquakes = new ArrayList<>();
    EarthQuakeAdapter quakeAdapter;
    TextView emptyStateTextView;
    ProgressBar pbLoading_Indicator;
    ImageView ivRomantic;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        lvEarthQuakeReport = findViewById(R.id.lvQuakeReport);
        emptyStateTextView = findViewById(R.id.tvEmptyView);
        pbLoading_Indicator = findViewById(R.id.pbLoading);
        ivRomantic = findViewById(R.id.ivRomantic);

        quakeAdapter = new EarthQuakeAdapter();
        lvEarthQuakeReport.setAdapter(quakeAdapter);

//        ConnectivityManager cm =
//                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = /*activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();*/
//        boolean isConnected = true;

//        if(!isConnected){
//            pbLoading_Indicator.setVisibility(View.GONE);
//            emptyStateTextView.setText("No Internet Connection available");
          /*} else*/ {
            lvEarthQuakeReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = quakeAdapter.getItem(position).url;
                    Uri uri = Uri.parse(url);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            });

//        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
//        earthquakeAsyncTask.execute(USGS_REQUEST_URL);

            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        }

    }

    @NonNull
    @Override
    public androidx.loader.content.Loader<List<EarthQuake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthQuakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
//        data = null;
        pbLoading_Indicator.setVisibility(View.GONE);
        if(data != null && !data.isEmpty()) {
            earthquakes = data;
            quakeAdapter = new EarthQuakeAdapter();
            lvEarthQuakeReport.setAdapter(quakeAdapter);
        }
//        else {
//            emptyStateTextView.setText("No Earthquake data found!!");
//        }
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<List<EarthQuake>> loader) {
        earthquakes.clear();
    }

    public class EarthQuakeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
//            if(earthquakes == null)
//                return 0;
            return earthquakes.size();
        }

        @Override
        public EarthQuake getItem(int position) {
//            if(earthquakes == null)
//                return null;
            return earthquakes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater()
                        .inflate(
                                R.layout.layout_quake,
                                parent,
                                false);
            }

            EarthQuake earthQuake = earthquakes.get(position);

            TextView tvMagnitude = convertView.findViewById(R.id.tvMagnitude);
            TextView tvLocation = convertView.findViewById(R.id.tvLocation);
            TextView tvDate = convertView.findViewById(R.id.tvDate);
            TextView tvTime = convertView.findViewById(R.id.tvTime);
            TextView tvOffset = convertView.findViewById(R.id.tvOffset);

            tvMagnitude.setText(earthQuake.magnitude);
            tvLocation.setText(earthQuake.location);
            tvDate.setText(earthQuake.date);
            tvTime.setText(earthQuake.time);
            tvOffset.setText(earthQuake.offset);

            setColor_In_Magnitude_Circle(Double.valueOf(earthQuake.magnitude), tvMagnitude);

            return convertView;
        }
    }

    void setColor_In_Magnitude_Circle(double magnitude, TextView tvMagnitude){
        int magnitudeFloorvalue = (int) Math.floor(magnitude);
        GradientDrawable background = (GradientDrawable) tvMagnitude.getBackground();
        switch (magnitudeFloorvalue){
            case 0:
                background.setColor(getResources().getColor(R.color.magnitude1, null));
                break;
            case 1:
                background.setColor(getResources().getColor(R.color.magnitude1, null));
                break;
            case 2:
                background.setColor(getResources().getColor(R.color.magnitude2, null));
                break;
            case 3:
                background.setColor(getResources().getColor(R.color.magnitude3, null));
                break;
            case 4:
                background.setColor(getResources().getColor(R.color.magnitude4, null));
                break;
            case 5:
                background.setColor(getResources().getColor(R.color.magnitude5, null));
                break;
            case 6:
                background.setColor(getResources().getColor(R.color.magnitude6, null));
                break;
            case 7:
                background.setColor(getResources().getColor(R.color.magnitude7, null));
                break;
            case 8:
                background.setColor(getResources().getColor(R.color.magnitude8, null));
                break;
            case 9:
                background.setColor(getResources().getColor(R.color.magnitude9, null));
                break;
            case 10:
                background.setColor(getResources().getColor(R.color.magnitude10plus, null));
                break;
        }
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<EarthQuake>> {

        @Override
        protected List<EarthQuake> doInBackground(String... urls) {
            if(urls.length <1  || TextUtils.isEmpty(urls[0])){
                return null;
            }
            earthquakes = QueryUtils.fetchEarthquakeData(urls[0]);
            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<EarthQuake> data) {

            quakeAdapter = new EarthQuakeAdapter();
            lvEarthQuakeReport.setAdapter(quakeAdapter);
//            if(data != null && !data.isEmpty())
//            earthquakes.addAll(data);
            lvEarthQuakeReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = quakeAdapter.getItem(position).url;
                    Uri uri = Uri.parse(url);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            });
        }
    }
}
