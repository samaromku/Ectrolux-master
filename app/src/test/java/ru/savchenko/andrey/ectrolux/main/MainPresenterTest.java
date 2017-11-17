package ru.savchenko.andrey.ectrolux.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import ru.savchenko.andrey.ectrolux.entities.Current;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by Andrey on 16.11.2017.
 */
public class MainPresenterTest {
    private MainPresenter presenter;
    @Mock private MainView mainView;
    @Mock private MainInterActor interActor;
    @Mock private MainView$$State mainView$$State;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());

        presenter = new MainPresenter(interActor);
        presenter.attachView(mainView);
        presenter.setViewState(mainView$$State);
    }

    @After
    public void finish() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }


    @Test
    public void getCurrent() throws Exception {
        //given
        given(interActor.getCurrent("USD")).willReturn(Single.just(new Current()));
        given(interActor.setCurrency(new Current(), true)).willReturn(Observable.just("12"));
        //when
        presenter.getCurrent(true, "USD");
        //then
        verify(mainView$$State, atLeastOnce()).etBottomRequestFocus();
        verify(mainView$$State, atLeastOnce()).etTopSetText("12");

        //given
        given(interActor.getCurrent("RUB")).willReturn(Single.just(new Current()));
        given(interActor.setCurrency(new Current(), false)).willReturn(Observable.just("25"));
        //when
        presenter.getCurrent(false, "RUB");
        //then
        verify(mainView$$State, atLeastOnce()).etTopRequestFocus();
        verify(mainView$$State, atLeastOnce()).etBottomSetText("25");

    }

    @Test
    public void getCurValues() throws Exception {
        //given
        given(interActor.getRatesValues()).willReturn(Single.just(new ArrayList<>()));
        //when
        presenter.getCurValues();
        //then
        verify(mainView$$State, atLeastOnce()).setTopCurrencySpinner(new ArrayList<>());
    }

    @Test
    public void parseCurrency() throws Exception {
        //given
        given(interActor.parseValue(true, "100")).willReturn(Observable.just("1"));
        //when
        presenter.parseCurrency(true, "100");
        //then
        verify(mainView$$State, atLeastOnce()).etBottomSetText("1");
        //given
        given(interActor.parseValue(false, "1")).willReturn(Observable.just("200"));
        //when
        presenter.parseCurrency(false, "1");
        //then
        verify(mainView$$State, atLeastOnce()).etTopSetText("200");
    }
}