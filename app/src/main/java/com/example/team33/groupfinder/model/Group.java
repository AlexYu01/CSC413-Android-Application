package com.example.team33.groupfinder.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Teng on 12/10/16.
 */


/**
 * Model class for group
 */
public class Group {

    private String mGroupId;
    private String mName;
    private String mDesc;
    private String mCity;
    private String mWho;
    private String mGroupPhotoUrl;

    public int getMemberCount() {
        return mMemberCount;
    }

    public void setMemberCount(int memberCount) {
        mMemberCount = memberCount;
    }

    private int mMemberCount;
    /**
     * <p>Class constructor</p>
     * <p>Sample Group JSONObject</p>
     * <pre>
     * {
     *  "id": "557066",
     *  "name": "Suffolk County Writers Workshop",
     *  "description": "This is a workshop for a group of Long Island fiction writers..."
     *  "city": "Bohemia",
     *  "who": "Writers",
     *  "members": 347
     *  "group_photo": {
     *      "photo_link": "http://photos4.meetupstatic.com/photos/event/a/0/3/d/600_1721021.jpeg"
     *  }
     * }
     * </pre>
     *
     * @param jsonObject {@link JSONObject} from each item in the search result
     * @throws JSONException when parser fails to parse the given JSON
     */
    private Group(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id")) this.setGroupId(jsonObject.getString("id"));
        if (jsonObject.has("name")) this.setName(jsonObject.getString("name"));
        if (jsonObject.has("description")) this.setDesc(jsonObject.getString("description"));
        if (jsonObject.has("city")) this.setCity(jsonObject.getString("city"));
        if (jsonObject.has("who")) this.setWho(jsonObject.getString("who"));
        if (jsonObject.has("members")) this.setMemberCount(jsonObject.getInt("members"));

        if (jsonObject.has("group_photo")) {
            JSONObject innerObject = jsonObject.getJSONObject("group_photo");
            if (innerObject.has("photo_link")) {
                String imgUrl = innerObject.getString("photo_link");
                this.setGroupPhotoUrl(imgUrl);
            }
        }
    }

    /**
     * @param jsonObject {@link JSONObject} response, received in Volley success listener
     * @return list of groups
     * @throws JSONException
     */
    public static List<Group> parseJson(JSONArray jsonObject) throws JSONException {
        List<Group> groups = new ArrayList<>();
        // Check if the JSONObject has object with key "Search"
        //if(jsonObject.has(":")){
        // Get JSONArray from JSONObject
        //JSONArray jsonArray = jsonObject.getJSONArray("");
        for (int i = 0; i < jsonObject.length(); i++) {
            // Create new Group object from each JSONObject in the JSONArray
            groups.add(new Group(jsonObject.getJSONObject(i)));
        }
        // }

        return groups;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String groupId) {
        this.mGroupId = groupId;
    }

    public String getWho() {
        return mWho;
    }

    public void setWho(String who) {
        this.mWho = who;
    }

    public String getGroupPhotoUrl() {
        return mGroupPhotoUrl;
    }

    public void setGroupPhotoUrl(String groupPhotoUrl) {
        this.mGroupPhotoUrl = groupPhotoUrl;
    }
}
