package ru.savchenko.andrey.ectrolux;

import android.app.Application;

import ru.savchenko.andrey.ectrolux.di.ComponentManager;

/**
 * Created by Andrey on 14.11.2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ComponentManager.init();
    }
}
