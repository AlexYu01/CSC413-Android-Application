package com.example.team33.groupfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.team33.groupfinder.fragment.WebFragment;

/**
 * Created by Teng on 12/13/2016.
 */

public class WebActivity extends SingleFragmentActivity {
    private static final String EXTRA_WEB_ID = "web_id";

    /**
     * @param packageContext Context of app
     * @param webUrl         UUID id associated with the card
     * @return intent
     */

    public static Intent newIntent(Context packageContext, String webUrl) {
        Log.i(MainActivity.class.getSimpleName(), "creating intent in web activity");
        Intent intent = new Intent(packageContext, WebActivity.class);
        intent.putExtra(EXTRA_WEB_ID, webUrl);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String webUrl = (String) getIntent().getSerializableExtra(EXTRA_WEB_ID);
        Log.i(MainActivity.class.getSimpleName(), "create fragment");
        return WebFragment.newInstance(webUrl);
    }
}
