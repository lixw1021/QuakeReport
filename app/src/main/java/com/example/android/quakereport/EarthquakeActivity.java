/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
//private static final String USGS_REQUEST_URL =
//        "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<com.example.android.quakereport.Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private EarthquakeAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView emptyTextView;
    private ProgressBar progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        progressBarView = (ProgressBar) findViewById(R.id.progressbar_view);
        emptyTextView = (TextView) findViewById(R.id.empty_view);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakeListView.setEmptyView(emptyTextView);

        //check network connectivity
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            //Define a new loader
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);

        } else {
            progressBarView.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet);
        }

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websitIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websitIntent);
            }
        } );
    }

    //create a new loader when there is no loader in the loadermanager
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i("xianwei", "onCreateLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");
        Log.i("xianwei li", uriBuilder.toString());

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    // called to update UI after loaderInBackground finished
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i("xianwei", "onLoadFinished");
        emptyTextView.setText(R.string.no_earthquakes);
        mAdapter.clear();
        if(earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
        progressBarView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i("xianwei", "onLoaderReset");
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
