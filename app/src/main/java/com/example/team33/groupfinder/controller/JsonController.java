package com.example.team33.groupfinder.controller;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team33.groupfinder.activity.MainActivity;
import com.example.team33.groupfinder.app.App;
import com.example.team33.groupfinder.model.Group;
import com.example.team33.groupfinder.request.JsonRequest;
import com.example.team33.groupfinder.volley.VolleySingleton;

import java.util.List;

/*
 * Created by abhijit on 12/2/16.
 * Modified by Teng on 12/10/16.
 */

/**
 * <p> Provides interface between {@link android.app.Activity} and {@link com.android.volley.toolbox.Volley} </p>
 */
public class JsonController {

    private final int TAG = 100;

    private final static String API_KEY = "5b3c326e1d47136f257c3d2c6828711e";

    private OnResponseListener responseListener;

    /**
     *
     * @param responseListener  {@link OnResponseListener}
     */
    public JsonController(OnResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    /**
     * Adds request to volley request queue
     * @param query query term for search
     */
    public void sendRequest(String query, double latitude, double longitude){

        // Request Method
        int method = Request.Method.GET;

        // Url with GET parameters
        System.out.println(latitude + " " + longitude);
        String url;
        if(latitude != -91.0 || longitude != -181.0) {
            url = "https://api.meetup.com/find/groups?key=" + API_KEY + "&&sign=true&photo-host=public&lon="+ longitude +"&text=" + Uri.encode(query) + "&radius=5&lat="+latitude+"&order=distance&page=20";
            System.out.println("with location url");
        } else {
            url = "https://api.meetup.com/find/groups?key=" + API_KEY + "&&sign=true&photo-host=public&text=" + Uri.encode(query) + "&radius=5&page=20";
            System.out.println("locationless url");

        }

        // Create new request using JsonRequest
        JsonRequest request
            = new JsonRequest(
                method,
                url,
                new Response.Listener<List<Group>>() {
                    @Override
                    public void onResponse(List<Group> groups) {
                        responseListener.onSuccess(groups);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseListener.onFailure(error.getMessage());
                    }
                }
        );

        // Add tag to request
        request.setTag(TAG);

        // Get RequestQueue from VolleySingleton
        VolleySingleton.getInstance(App.getContext()).addToRequestQueue(request);
    }

    /**
     * <p>Cancels all request pending in request queue,</p>
     * <p> There is no way to control the request already processed</p>
     */
    public void cancelAllRequests() {
        VolleySingleton.getInstance(App.getContext()).cancelAllRequests(TAG);
    }

    /**
     *  Interface to communicate between {@link android.app.Activity} and {@link JsonRequest}
     *  <p>Object available in {@link JsonRequest} and implemented in {@link MainActivity}</p>
     */
    public interface OnResponseListener {
        void onSuccess(List<Group> groups);
        void onFailure(String errorMessage);
    }

}