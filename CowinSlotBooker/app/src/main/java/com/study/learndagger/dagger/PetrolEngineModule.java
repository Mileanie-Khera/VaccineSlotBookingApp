package com.study.learndagger.dagger;

import com.study.learndagger.car.Engine;
import com.study.learndagger.car.PetrolEngine;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PetrolEngineModule {

//    @Provides
//    Engine providePetrolEngine(PetrolEngine engine) {
//        return engine;
//    }

    //Same as provides methods, more concise, less code
    @Binds
    abstract Engine bindsPetrolEngine(PetrolEngine engine);
}
