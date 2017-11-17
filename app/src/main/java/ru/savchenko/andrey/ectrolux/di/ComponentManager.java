package ru.savchenko.andrey.ectrolux.di;

import ru.savchenko.andrey.ectrolux.di.main.MainComponent;
import ru.savchenko.andrey.ectrolux.di.main.MainModule;

/**
 * Created by Andrey on 25.09.2017.
 */

public class ComponentManager {
    private static AppComponent appComponent;
    private static MainComponent mainComponent;

    public static MainComponent mainComponent(){
        if(mainComponent==null){
            mainComponent = appComponent.mainComponent(new MainModule());
        }
        return mainComponent;
    }

    public static void destroyMainComponent(){
        mainComponent = null;
    }

    public static void init(){
        appComponent = DaggerAppComponent
                .builder()
                .build();
    }


}
