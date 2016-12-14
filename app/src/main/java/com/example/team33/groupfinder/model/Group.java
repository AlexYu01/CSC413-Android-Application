package com.example.team33.groupfinder.model;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Created by Teng on 12/10/16.
 */


/**
 * Model class for group
 */
public class Group {

    private UUID mId;
    private String mGroupId;
    private String mName;
    private String mDesc;
    private String mCity;
    private String mState;
    private String mWho;
    private String mGroupPhotoUrl;
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
        mId = UUID.randomUUID();
        if (jsonObject.has("id")) this.setGroupId(jsonObject.getString("id"));
        if (jsonObject.has("name")) this.setName(jsonObject.getString("name"));
        if (jsonObject.has("description")) this.setDesc(jsonObject.getString("description"));
        if (jsonObject.has("city")) this.setCity(jsonObject.getString("city"));
        if (jsonObject.has("state")) this.setState(jsonObject.getString("state"));
        if (jsonObject.has("who")) this.setWho(jsonObject.getString("who"));
        if (jsonObject.has("members")) this.setMemberCount(jsonObject.getInt("members"));

        String imgUrl = null;
        if (jsonObject.has("group_photo")) {
            JSONObject innerObject = jsonObject.getJSONObject("group_photo");
            if (innerObject.has("highres_link")) {
                imgUrl = innerObject.getString("photo_link");
                this.setGroupPhotoUrl(imgUrl);
            }
        } else if (imgUrl == null) {
            if (jsonObject.has("key_photo")) {
                JSONObject innerObject = jsonObject.getJSONObject("key_photo");
                if (innerObject.has("highres_link")) {
                    imgUrl = innerObject.getString("photo_link");
                    this.setGroupPhotoUrl(imgUrl);
                }
            }
        }
    }

    /**
     * @param jsonArray {@link JSONArray} response, received in Volley success listener
     * @return list of groups
     * @throws JSONException
     */
    public static List<Group> parseJson(JSONArray jsonArray) throws JSONException {
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            // Create new Group object from each JSONObject in the JSONArray
            groups.add(new Group(jsonArray.getJSONObject(i)));
        }

        return groups;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public int getMemberCount() {
        return mMemberCount;
    }

    public void setMemberCount(int memberCount) {
        mMemberCount = memberCount;
    }

    public UUID getUuid() {
        return mId;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = stripHtml(desc);
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

    /**
     * Removes html tags from description string
     *
     * @param html String a string with html tags
     * @return String without html tags
     */

    private String stripHtml(String html) {
        String s;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            s = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            s = Html.fromHtml(html).toString();
        }

        return s;
    }
}
