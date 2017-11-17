package ru.savchenko.andrey.ectrolux.main;

import com.arellomobile.mvp.MvpView;

import java.util.List;

/**
 * Created by Andrey on 14.11.2017.
 */

public interface MainView extends MvpView{
    void setTopCurrencySpinner(List<String> currencies);

    void etBottomSetText(String text);

    void etTopSetText(String text);

    void etTopRequestFocus();

    void etBottomRequestFocus();
}
