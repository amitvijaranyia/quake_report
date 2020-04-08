package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */

public class QueryUtils {


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils(){}

    /**
     * Query the USGS dataset and return a list of {@link EarthQuake} objects.
     */
    public static List<EarthQuake> fetchEarthquakeData(String requestUrl){
        if(TextUtils.isEmpty(requestUrl)){
            return null;
        }

        // Create URL object
        String jsonResponse = null;

        // Perform HTTP request to the URL and receive a JSON response back
        URL url = createUrl(requestUrl);

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making http request: ", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link EarthQuake}
        List<EarthQuake> earthQuakeArrayList = extractEarthQuakes(jsonResponse);

        return earthQuakeArrayList;
    }

    /**
     * Return a list of {@link EarthQuake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<EarthQuake> extractEarthQuakes(String rawJSON_String){
        // If the JSON string is empty or null, then return early.
        if(TextUtils.isEmpty(rawJSON_String)){
            return null;
        }

        List<EarthQuake> earthQuakes = new ArrayList<>();

        // Try to parse the rawJSON_String. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject rawJSON = new JSONObject(rawJSON_String);
            JSONArray featuresArray = rawJSON.getJSONArray("features");

            for(int i = 0; i < featuresArray.length(); i++){
                JSONObject jsonObject = featuresArray.getJSONObject(i);
                JSONObject properties = jsonObject.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long timeInMilliSeconds = properties.getLong("time");
                String url = properties.getString("url");

                earthQuakes.add(new EarthQuake(
                        magnitudeToBeDisplayed(magnitude),
                        OffsetOrLocationToBeDisplayed(location, false),
                        dateToBeDisplayed(timeInMilliSeconds),
                        timeToBeDisplayed(timeInMilliSeconds),
                        OffsetOrLocationToBeDisplayed(location, true),
                        url));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ExtractEarthQuakes", "Error happened while parsing JSON", e);
        }
        return earthQuakes;
    }

    private static String magnitudeToBeDisplayed(double mag){
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(mag);
        return output;
    }

    private static String dateToBeDisplayed(long timeInMilliSeconds){
        Date date1 = new Date(timeInMilliSeconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return simpleDateFormat.format(date1);
    }

    private static String timeToBeDisplayed(long timeInMilliSeconds){
        Date date1 = new Date(timeInMilliSeconds);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("h:mm a");
        return simpleTimeFormat.format(date1);
    }

    private static String OffsetOrLocationToBeDisplayed(String location, boolean offsetOrLocation){
        String offset = "";
        int fPos = 0;
        for(int i = 0; i < location.length(); i++) {
            if(i != 0 && i < location.length()-2 && location.charAt(i) == 'o' && location.charAt(i-1) == ' ' && location.charAt(i+1) == 'f' && location.charAt(i+2) == ' ')
                fPos = i+2;
        }
        if(fPos != 0){
            if(offsetOrLocation)
                return location.substring(0, fPos);
            else
                return location.substring(fPos+1);
        }
        else {
            if(offsetOrLocation)
                return "Near by";
            else
                return location;
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl){
        if(stringUrl == null || TextUtils.isEmpty(stringUrl)){
            return null;
        }
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Url_Error", "Error happened while creating the url", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        //if url is null then no need to make http request
        if(url == null){
            return null;
        }

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error Response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                /** Closing the input stream could throw an IOException, which is why
                    the makeHttpRequest(URL url) method signature specifies than an IOException
                    could be thrown.***/
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(streamReader);
            String currentLine = br.readLine();
            while(currentLine != null){
                output.append(currentLine);
                currentLine = br.readLine();
            }
        }
        return output.toString();
    }
}
