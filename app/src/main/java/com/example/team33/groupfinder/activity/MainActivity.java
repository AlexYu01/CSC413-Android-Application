package com.example.team33.groupfinder.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team33.groupfinder.R;
import com.example.team33.groupfinder.adapter.RecyclerViewAdapter;
import com.example.team33.groupfinder.app.App;
import com.example.team33.groupfinder.controller.JsonController;
import com.example.team33.groupfinder.model.Group;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


import android.location.Location;


public class MainActivity extends AppCompatActivity
        implements
        SearchView.OnQueryTextListener,
        RecyclerViewAdapter.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;


    JsonController controller;
    TextView textView;
    RecyclerView recyclerView;
    private double latitude;
    private double longitude;
    private RecyclerViewAdapter adapter;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set default latitude and longitude values that don't exist in real world
        latitude = -91.0;
        longitude = -181.0;

        textView = (TextView) findViewById(R.id.tvEmptyRecyclerView);
        textView.setText("Search for groups by pressing the magnifying glass on the toolbar");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(new ArrayList<Group>());
        adapter.setListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        controller = new JsonController(
                new JsonController.OnResponseListener() {
                    @Override
                    public void onSuccess(List<Group> groups) {
                        if (groups.size() > 0) {
                            textView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.invalidate();
                            adapter.updateDataSet(groups);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Failed to retrieve data");
                        Toast.makeText(MainActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }*/

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(MainActivity.class.getSimpleName(), "Connected to google play service!");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(MainActivity.class.getSimpleName(), "Permission was granted!");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mLastLocation != null) {
                Log.i(MainActivity.class.getSimpleName(), "Location is not null!");
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    public void getLocation() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "We may not be able to provide you with the best results!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * create options from menu/menu_activity_main.xml where we have searchView as one of the menu items
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search");
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    /**
     * this method is invoked when user presses search button in soft keyboard
     *
     * @param query query text in search view
     * @return boolean
     * <p> - true  - query handled </p>
     * <p> - false - query not handled (returning false will collapse soft keyboard)</p>
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        getLocation();

        if (query.length() > 1) {
            controller.cancelAllRequests();
            //controller.sendRequest(query); //TODO modded
            controller.sendRequest(query, latitude, longitude);

            return false;
        } else {
            Toast.makeText(MainActivity.this, "Must provide more than one character", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("Must provide more than one character to search");
            return true;
        }
    }

    /**
     * this method is invoked on every key press of soft keyboard while user is typing
     *
     * @param newText newText is updated query text on every input of user from soft keyboard
     * @return boolean
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        getLocation();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        if (newText.length() > 1) {
            controller.cancelAllRequests();
            controller.sendRequest(newText, latitude, longitude);
        } else if (newText.equals("")) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    /**
     * Interface Implementation
     * <p>This method will be invoked when user press anywhere on cardview</p>
     */
    @Override
    public void onCardClick(Group group) {

        Intent intent = GroupActivity.newIntent(App.getContext(), group.getGroupId());
        startActivity(intent);
    }

}
