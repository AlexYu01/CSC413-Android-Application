package com.example.team33.storyfinder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class StoryActivity extends SingleFragmentActivity{

    private static final String EXTRA_STORY_ID = "story_id";

    public static Intent newIntent(Context packageContext, UUID storyId) {
        Intent intent = new Intent(packageContext, StoryActivity.class);
        intent.putExtra(EXTRA_STORY_ID, storyId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID storyId = (UUID) getIntent().getSerializableExtra(EXTRA_STORY_ID);
        return StoryFragment.newInstance(storyId);
    }
}
