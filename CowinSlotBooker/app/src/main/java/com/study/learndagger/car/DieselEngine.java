package com.study.learndagger.car;

import android.util.Log;

import javax.inject.Inject;

public class DieselEngine implements Engine {

    private int horsePower;

    @Inject
    public DieselEngine(int horsePower) {
        this.horsePower = horsePower;

    }

    @Override
    public void start() {
        Log.d(Car.TAG, "Diesel engine started. Horsepower: " + horsePower);
    }
}
