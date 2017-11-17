package ru.savchenko.andrey.ectrolux.di.main;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.savchenko.andrey.ectrolux.main.MainInterActor;
import ru.savchenko.andrey.ectrolux.network.RetrofitService;

import static ru.savchenko.andrey.ectrolux.storage.Const.IO;
import static ru.savchenko.andrey.ectrolux.storage.Const.MAIN;

/**
 * Created by Andrey on 01.11.2017.
 */
@Module
public class MainModule {
    @MainScope
    @Provides
    MainInterActor interActor(RetrofitService retrofitService){
        return new MainInterActor(retrofitService);
    }

    @MainScope
    @Provides
    @Named(MAIN)
    Scheduler mainThread(){
        return AndroidSchedulers.mainThread();
    }

    @MainScope
    @Provides
    @Named(IO)
    Scheduler ioThread(){
        return AndroidSchedulers.mainThread();
    }

}
