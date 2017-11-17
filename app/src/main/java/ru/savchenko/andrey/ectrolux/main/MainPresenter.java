package ru.savchenko.andrey.ectrolux.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.savchenko.andrey.ectrolux.di.ComponentManager;

import static ru.savchenko.andrey.ectrolux.storage.Const.IO;
import static ru.savchenko.andrey.ectrolux.storage.Const.MAIN;

/**
 * Created by Andrey on 14.11.2017.
 */
@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private static final String TAG = "MainPresenter";
    @Inject
    MainInterActor interActor;

    MainPresenter() {
        ComponentManager.mainComponent().inject(this);
    }

    MainPresenter(MainInterActor mainInterActor){
        this.interActor = mainInterActor;
    }

    void getCurrent(boolean isTop, String currency) {
        interActor.getCurrent(currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(current -> interActor.setCurrency(current, isTop)
                                .subscribe(s -> {
                                    if (isTop) {
                                        getViewState().etBottomRequestFocus();
                                        getViewState().etTopSetText(s);
                                    } else {
                                        getViewState().etTopRequestFocus();
                                        getViewState().etBottomSetText(s);
                                    }
                                }),
                        Throwable::printStackTrace);
    }

    void getCurValues() {
        interActor.getRatesValues()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> getViewState().setTopCurrencySpinner(strings));
    }

    void parseCurrency(boolean isTop, CharSequence currencyValue) {
        if (isTop) {
            interActor.parseValue(true, currencyValue)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .subscribe(s -> getViewState().etBottomSetText(s));
        } else {
            interActor.parseValue(false, currencyValue)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .subscribe(s -> getViewState().etTopSetText(s));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ComponentManager.destroyMainComponent();
    }
}
