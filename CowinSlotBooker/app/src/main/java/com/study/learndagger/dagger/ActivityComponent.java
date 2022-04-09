package com.study.learndagger.dagger;

//Backbone of car object class

import com.study.learndagger.MainActivity;
import com.study.learndagger.car.Car;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Subcomponent;

/*@PerActivity
@Component(dependencies = AppComponent.class, modules = {WheelsModule.class, PetrolEngineModule.class})
public interface ActivityComponent {
    Car getCar();

    void inject(MainActivity mainActivity);

    @Component.Builder
    interface Builder {
        ActivityComponent build();

        @BindsInstance
        Builder horsePower(@Named("horse power") int horsePower);

        Builder appComponent(AppComponent component);

        @BindsInstance
        Builder engineCapacity(@Named("engine capacity") int engineCapacity);
    }
}*/

@PerActivity
@Subcomponent(modules = {WheelsModule.class, PetrolEngineModule.class})
public interface ActivityComponent {
    Car getCar();

    void inject(MainActivity mainActivity);

    @Subcomponent.Builder
    interface Builder {
        ActivityComponent build();

        @BindsInstance
        Builder horsePower(@Named("horse power") int horsePower);

        @BindsInstance
        Builder engineCapacity(@Named("engine capacity") int engineCapacity);
    }
}

/*@PerActivity
@Subcomponent(modules = {WheelsModule.class, DieselEngineModule.class})
public interface ActivityComponent {
    Car getCar();

    void inject(MainActivity mainActivity);
}*/
