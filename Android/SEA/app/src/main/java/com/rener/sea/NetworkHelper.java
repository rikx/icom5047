package com.rener.sea;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Performs network operations related to connectivity
 */
public class NetworkHelper {

    public static final String SEA_HOST_ADDRESS = "136.145.116.231";
    public static final String TEST_URL = "http://www.google.com";
    boolean internetAvailable;
    private Context context;

    /**
     * COnstructor that receives a context to perform operations on
     * @param context the application context
     */
    public NetworkHelper(Context context) {
        this.context = context;
        internetAvailable = false;
    }

    /**
     * Checks whether there is an active network connection
     * @return true if there is an active network connection
     */
    public boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Checks whether an Internet connection is available by connection to Google
     * @return true if a connection Google could be established
     */
    public boolean isInternetAvailable() {
        boolean networkAvailable = isNetworkConnected();
        new CheckInternetTask().execute(TEST_URL);
        return networkAvailable ? internetAvailable : false;
    }

    /**
     * Checks whether the SEA server is available by establishing a connection to it
     * @return true if a connection to the SEA server could be established
     */
    public boolean isServerAvailable() {
        //TODO: establish connection to SEA
        return false;
    }

    private class CheckInternetTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(strings[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("200")) internetAvailable = true;
        }

        private String downloadUrl(String myurl) throws IOException {
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "test");
                conn.setRequestProperty("Connection", "close");
                conn.setConnectTimeout(1000); // mTimeout is in seconds
                conn.connect();
                int response = conn.getResponseCode();
                return String.valueOf(response);

            } catch (IOException e) {
                Log.i(this.toString(), "error on http connection", e);
                return "ERROR";
            }
        }
    }

}