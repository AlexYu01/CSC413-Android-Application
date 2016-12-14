package com.example.team33.groupfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.team33.groupfinder.fragment.GroupFragment;

import java.util.UUID;

/**
 * Created by Teng on 12/10/16.
 */

public class GroupActivity extends SingleFragmentActivity {

    private static final String EXTRA_GROUP_ID = "group_id";

    /**
     * @param packageContext Context of app
     * @param groupId        UUID id associated with the card
     * @return intent
     */

    public static Intent newIntent(Context packageContext, UUID groupId) {
        Intent intent = new Intent(packageContext, GroupActivity.class);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID storyId = (UUID) getIntent().getSerializableExtra(EXTRA_GROUP_ID);
        return GroupFragment.newInstance(storyId);
    }
}
