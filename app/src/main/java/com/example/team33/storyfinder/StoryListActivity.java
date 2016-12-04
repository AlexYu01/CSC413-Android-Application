package com.example.team33.storyfinder;

import android.support.v4.app.Fragment;


public class StoryListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new StoryListFragment();
    }
}
