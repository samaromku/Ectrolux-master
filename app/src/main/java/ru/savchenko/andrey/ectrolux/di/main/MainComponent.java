package ru.savchenko.andrey.ectrolux.di.main;

import dagger.Subcomponent;
import ru.savchenko.andrey.ectrolux.main.MainPresenter;

/**
 * Created by Andrey on 25.09.2017.
 */
@MainScope
@Subcomponent(modules = {MainModule.class})
public interface MainComponent {
    void inject(MainPresenter presenter);
}
