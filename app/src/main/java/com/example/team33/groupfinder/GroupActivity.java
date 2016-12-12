package com.example.team33.groupfinder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class GroupActivity extends SingleFragmentActivity{

    private static final String EXTRA_STORY_ID = "story_id";

    public static Intent newIntent(Context packageContext, String groupId) {
        Intent intent = new Intent(packageContext, GroupActivity.class);
        intent.putExtra(EXTRA_STORY_ID, groupId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String storyId = (String) getIntent().getSerializableExtra(EXTRA_STORY_ID);
        return GroupFragment.newInstance(storyId);
    }
}
