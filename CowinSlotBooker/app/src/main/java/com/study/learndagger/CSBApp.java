package com.study.learndagger;

import android.app.Application;

import com.study.learndagger.dagger.AppComponent;
import com.study.learndagger.dagger.DaggerAppComponent;

public class CSBApp extends Application {
    public static CSBApp mCSBApp;
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        mCSBApp = this;
        component = DaggerAppComponent.create();
    }

    public AppComponent getAppComponent() {
        return component;
    }
}
