package ru.savchenko.andrey.ectrolux.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.savchenko.andrey.ectrolux.di.main.MainComponent;
import ru.savchenko.andrey.ectrolux.di.main.MainModule;

/**
 * Created by Andrey on 14.11.2017.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    MainComponent mainComponent(MainModule mainModule);
}
