package com.example.team33.groupfinder;

import android.app.Application;
import android.content.Context;

/**
 *  Created by abhijit on 12/2/16.
 *  Check AndroidManifest.xml under application
 *  android:name=".app.App"
 *  will setup this class as Singleton and will be instantiated when application starts
 */
public class App extends Application {

    private static App instance;

    /**
     * Return application context anywhere in the application
     * @return  Application context
     */
    public static Context getContext(){
         return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
