package com.study.learndagger.dagger;

import com.study.learndagger.car.DieselEngine;
import com.study.learndagger.car.Engine;

import dagger.Module;
import dagger.Provides;

@Module
public class DieselEngineModule {

    private int horsePower;

    @Provides
    Engine provideEngine(DieselEngine dieselEngine) {
        return dieselEngine;
    }

    public DieselEngineModule(int horsePower) {
        this.horsePower = horsePower;
    }

    @Provides
    int providesHorsePower() {
        return horsePower;
    }
    //Same as provides methods, more concise, less code
    //@Binds
    //abstract Engine bindsDieselEngine(DieselEngine engine);
}
