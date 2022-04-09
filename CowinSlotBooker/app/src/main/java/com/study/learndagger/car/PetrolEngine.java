package com.study.learndagger.car;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

public class PetrolEngine implements Engine {

    private int horsePower;
    private int engineCapacity;

    @Inject
    public PetrolEngine(@Named("horse power") int horsePower, @Named("engine capacity")int engineCapacity) {
        this.horsePower = horsePower;
        this.engineCapacity = engineCapacity;
    }

    @Override
    public void start() {
        Log.d(Car.TAG, "Petrol engine started. " +
                "\nHorsepower: " + horsePower + " \nEngine capacity: " + engineCapacity);
    }
}
