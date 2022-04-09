package com.study.learndagger.car;

import android.util.Log;

import com.study.learndagger.dagger.PerActivity;

import javax.inject.Inject;

@PerActivity
public class Car {
    public static final String TAG = "Car";
    private Wheels mWheels;
    private Engine mEngine;
    private Driver mDriver;

    @Inject
    Car(Wheels wheels, Engine engine, Driver driver) {
        this.mWheels = wheels;
        this.mEngine = engine;
        this.mDriver = driver;
    }

    @Inject
    public void enableRemote(Remote remote) {
        remote.setListener(this);
    }

    public void drive() {
        mEngine.start();
        Log.d(TAG, mDriver + " drives " + this);
    }
}
