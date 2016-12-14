package com.example.team33.groupfinder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.team33.groupfinder.R;
import com.example.team33.groupfinder.activity.WebActivity;
import com.example.team33.groupfinder.adapter.RecyclerViewAdapter;
import com.example.team33.groupfinder.app.App;
import com.example.team33.groupfinder.model.Group;
import com.example.team33.groupfinder.volley.VolleySingleton;

import java.util.UUID;

/**
 * Created by Teng on 12/11/16.
 */

public class GroupFragment extends Fragment {

    private static final String ARG_GROUP_ID = "group_id";

    private Group mGroup;

    private NetworkImageView mGroupPhoto;
    private TextView mGroupName;
    private TextView mMemberCount;
    private TextView mGroupId;
    private TextView mGroupLocation;
    private TextView mGroupDesc;
    private Button mWebButton;


    public static GroupFragment newInstance(UUID groupUuid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP_ID, groupUuid);
        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID groupUuid = (UUID) getArguments().getSerializable(ARG_GROUP_ID);
        for (Group group : RecyclerViewAdapter.getGroupList()) {
            if (group.getUuid().equals(groupUuid)) {
                mGroup = group;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_group, container, false);

        mGroupPhoto = (NetworkImageView) v.findViewById(R.id.groupPhoto);
        mGroupName = (TextView) v.findViewById((R.id.groupName));
        mMemberCount = (TextView) v.findViewById((R.id.groupMemberCount));
        mGroupId = (TextView) v.findViewById((R.id.groupId));
        mGroupLocation = (TextView) v.findViewById((R.id.groupLocation));
        mGroupDesc = (TextView) v.findViewById((R.id.groupDesc));

        setPhotoUrl(mGroup.getGroupPhotoUrl());
        mGroupName.setText(mGroup.getName());
        setMemberCount(mGroup.getMemberCount(), mGroup.getWho());
        setGroupId(mGroup.getGroupId());
        setGroupLocation(mGroup.getCity(), mGroup.getState());
        mGroupDesc.setText(mGroup.getDesc());
        mGroupDesc.setMovementMethod(new ScrollingMovementMethod());


        mWebButton = (Button) v.findViewById(R.id.groupWebButton);
        mWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = WebActivity.newIntent(App.getContext(), mGroup.getGroupWebUrl());
                startActivity(intent);
            }
        });

        return v;
    }

    void setPhotoUrl(String imageUrl) {
        ImageLoader imageLoader = VolleySingleton.getInstance(App.getContext()).getImageLoader();
        this.mGroupPhoto.setImageUrl(imageUrl, imageLoader);
    }

    private void setMemberCount(int memberCount, String who) {
        String m = "We're " + memberCount + " " + who;
        this.mMemberCount.setText(m);
    }

    private void setGroupId(String id) {
        String i = "Group ID: " + id;
        this.mGroupId.setText(i);
    }

    private void setGroupLocation(String city, String state) {
        String l = city + ", " + state;
        this.mGroupLocation.setText(l);
    }


}
