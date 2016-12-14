package com.example.team33.groupfinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.team33.groupfinder.R;

/**
 * Created by Teng on 12/13/2016.
 */

public class WebFragment extends Fragment {

    private static final String ARG_WEB_ID = "web_id";

    private WebView mWebView;

    private String webUrl;

    public static WebFragment newInstance(String webUrl) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEB_ID, webUrl);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webUrl = (String) getArguments().getSerializable(ARG_WEB_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_web, container, false);

        mWebView = (WebView) v.findViewById(R.id.activity_main_webview);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Use remote resource
        mWebView.loadUrl(webUrl);

        // Use local resource
        // mWebView.loadUrl("file:///android_asset/www/index.html");

        return v;
    }


}
