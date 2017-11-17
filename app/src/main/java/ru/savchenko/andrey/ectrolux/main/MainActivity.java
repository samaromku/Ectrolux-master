package ru.savchenko.andrey.ectrolux.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.savchenko.andrey.ectrolux.R;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    public static final String TAG = "MainActivity";
    @BindView(R.id.spTopCurrency)
    Spinner spTopCurrency;
    @BindView(R.id.etTopCurrency)
    EditText etTopCurrency;
    @BindView(R.id.spBottomCurrency)
    Spinner spBottomCurrency;
    @BindView(R.id.etBottomCurrency)
    EditText etBottomCurrency;

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter.getCurValues();

        spTopCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.getCurrent(true, spTopCurrency.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spBottomCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.getCurrent(false, spBottomCurrency.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RxTextView.textChanges(etTopCurrency)
                .subscribe(charSequence -> {
                    if (etTopCurrency.isFocused()) {
                        Observable.fromCallable(() -> charSequence)
                                .debounce(1000, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(charSequence1 -> presenter.parseCurrency(true, charSequence1),
                                        Throwable::printStackTrace);
                    }
                });

        RxTextView.textChanges(etBottomCurrency)
                .subscribe(charSequence -> {
                    if (etBottomCurrency.isFocused()) {
                        Observable.fromCallable(() -> charSequence)
                                .debounce(1000, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(charSequence1 -> presenter.parseCurrency(false, charSequence1),
                                        Throwable::printStackTrace);
                    }
                });
    }

    @Override
    public void setTopCurrencySpinner(List<String> currencies) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        spTopCurrency.setAdapter(adapter);
        spBottomCurrency.setAdapter(adapter);
    }

    @Override
    public void etBottomSetText(String text) {
        etBottomCurrency.setText(text);
    }

    @Override
    public void etTopSetText(String text) {
        etTopCurrency.setText(text);
    }

    @Override
    public void etTopRequestFocus() {
        etTopCurrency.requestFocus();
    }

    @Override
    public void etBottomRequestFocus() {
        etBottomCurrency.requestFocus();
    }
}
