package ru.savchenko.andrey.ectrolux.main;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.savchenko.andrey.ectrolux.entities.Current;
import ru.savchenko.andrey.ectrolux.entities.Rates;
import ru.savchenko.andrey.ectrolux.network.RetrofitService;

/**
 * Created by Andrey on 14.11.2017.
 */

public class MainInterActor {
    private RetrofitService service;
    private Current topCurrent;
    private Current bottomCurrent;
    private CharSequence topValue;
    private CharSequence bottomValue;

    //для тестов
    void setTopCurrent(Current topCurrent) {
        this.topCurrent = topCurrent;
    }

    void setBottomCurrent(Current bottomCurrent) {
        this.bottomCurrent = bottomCurrent;
    }

    void setTopValue(CharSequence topValue) {
        this.topValue = topValue;
    }

    void setBottomValue(CharSequence bottomValue) {
        this.bottomValue = bottomValue;
    }

    public MainInterActor(RetrofitService service) {
        this.service = service;
    }

    Single<Current> getCurrent(String currency) {
        return service.getCurrent(currency);
    }

    Observable<String> setCurrency(Current currency, boolean isTop) {
        if (!isTop) {
            topCurrent = currency;
            if (bottomCurrent == null || topValue == null) return Observable.empty();
            Double course = runGetter(currency.getRates(), bottomCurrent.getBase());
            return Observable.fromCallable(() -> getCurrentValueByThisCourse(topValue, course));
        } else {
            bottomCurrent = currency;
            if (topCurrent == null || bottomValue == null) return Observable.empty();
            Double course = runGetter(currency.getRates(), topCurrent.getBase());
            return Observable.fromCallable(() -> getCurrentValueByThisCourse(bottomValue, course));
        }
    }

    Single<List<String>> getRatesValues() {
        return Single.fromCallable(() -> {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < Rates.class.getDeclaredFields().length; i++) {
                strings.add(Rates.class.getDeclaredFields()[i].getName().toUpperCase());
            }
            return strings;
        });
    }

    Observable<String> parseValue(boolean isTop, CharSequence currentValue) {
        if (TextUtils.isEmpty(currentValue)) return Observable.empty();
        if (isTop) {
            return Observable.fromCallable(() -> {
                topValue = currentValue;
                Double course = runGetter(topCurrent.getRates(), bottomCurrent.getBase());
                bottomValue = getCurrentValueByThisCourse(currentValue, course);
                return bottomValue.toString();
            });
        } else {
            return Observable.fromCallable(() -> {
                bottomValue = currentValue;
                Double course = runGetter(bottomCurrent.getRates(), topCurrent.getBase());
                topValue = getCurrentValueByThisCourse(currentValue, course);
                return topValue.toString();
            });
        }
    }

    private String getCurrentValueByThisCourse(CharSequence currentValue, Double course) {
        Double value = Double.parseDouble(currentValue.toString());
        if (course == null || course == 0) {
            return String.valueOf(value);
        } else
            return String.valueOf(value / course);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Double runGetter(Rates rates, String fieldName) {
        for (Method method : rates.getClass().getDeclaredMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (fieldName.length() + 3))) {
                if (method.getName().toLowerCase().endsWith(fieldName.toLowerCase())) {
                    try {
                        return (Double) method.invoke(rates);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return null;
    }
}
