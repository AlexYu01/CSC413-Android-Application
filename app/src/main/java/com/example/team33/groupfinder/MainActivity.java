package com.example.team33.groupfinder;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        SearchView.OnQueryTextListener,
        RecyclerViewAdapter.OnClickListener {

    private RecyclerViewAdapter adapter;
    JsonController controller;

    TextView textView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.tvEmptyRecyclerView);
        textView.setText("Search for movies using SearchView in toolbar");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(new ArrayList<Movie>());
        adapter.setListener(this);

        controller = new JsonController(
            new JsonController.OnResponseListener() {
                @Override
                public void onSuccess(List<Movie> movies) {
                    if(movies.size() > 0) {
                        textView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.invalidate();
                        adapter.updateDataSet(movies);
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

    /**
     * create options from menu/menu_activity_main.xml where we have searchView as one of the menu items
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
     * @param query query text in search view
     * @return  boolean
     *                 <p> - true  - query handled </p>
     *                 <p> - false - query not handled (returning false will collapse soft keyboard)</p>
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() > 1) {
            controller.cancelAllRequests();
            controller.sendRequest(query);
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
     * @param newText newText is updated query text on every input of user from soft keyboard
     * @return boolean
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() > 1) {
            controller.cancelAllRequests();
            controller.sendRequest(newText);
        } else if(newText.equals("")) {
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
    public void onCardClick(Movie movie) {
        Toast.makeText(this, movie.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
    }

    /**
     * Interface Implementation
     * <p>This method will be invoked when user press on poster of the movie</p>
     */
    @Override
    public void onPosterClick(Movie movie) {
        Toast.makeText(this, movie.getTitle() + " poster clicked", Toast.LENGTH_SHORT).show();
    }
}
