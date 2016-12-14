package com.example.team33.groupfinder.request;

/**
 * Created by Teng on 12/10/16.
 */

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team33.groupfinder.model.Group;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Volley request to receive JSON as response and parse it to create list of groups
 */
public class JsonRequest extends Request<List<Group>> {

    // Success listener implemented in controller
    private Response.Listener<List<Group>> successListener;

    /**
     * Class constructor
     *
     * @param method          Request method
     * @param url             url to API
     * @param successListener success listener
     * @param errorListener   failure listener
     */
    public JsonRequest(int method,
                       String url,
                       Response.Listener<List<Group>> successListener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.successListener = successListener;
    }

    @Override
    protected Response<List<Group>> parseNetworkResponse(NetworkResponse response) {
        // Convert byte[] data received in the response to String
        String jsonString = new String(response.data);
        List<Group> movies;

        JSONArray jsonArray;
        Log.i(this.getClass().getName(), jsonString);
        // Try to convert JsonString to list of movies
        try {
            // Convert JsonString to JSONArray
            jsonArray = new JSONArray(jsonString);
            // Get list of movies from received JSON
            movies = Group.parseJson(jsonArray);
        }
        // in case of exception, return volley error
        catch (JSONException e) {
            e.printStackTrace();
            // return new volley error with message
            return Response.error(new VolleyError("Failed to process the request"));
        }
        // return list of movies
        return Response.success(movies, getCacheEntry());
    }

    @Override
    protected void deliverResponse(List<Group> movies) {
        successListener.onResponse(movies);
    }
}
