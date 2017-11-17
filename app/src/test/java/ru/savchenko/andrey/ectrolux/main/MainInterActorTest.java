package ru.savchenko.andrey.ectrolux.main;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.savchenko.andrey.ectrolux.entities.Current;
import ru.savchenko.andrey.ectrolux.entities.Rates;
import ru.savchenko.andrey.ectrolux.network.RetrofitService;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

/**
 * Created by Andrey on 16.11.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class})
public class MainInterActorTest {
    private MainInterActor interActor;
    @Mock
    RetrofitService service;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        interActor = new MainInterActor(service);
    }

    @Test
    public void getCurrent() throws Exception {
        //when
        TestObserver<Current> testObserver = new TestObserver<>();
        given(service.getCurrent("1")).willReturn(Single.just(new Current()));
        //given
        interActor.getCurrent("1").subscribe(testObserver);
        //then
        testObserver.assertValue(new Current());
    }

    @Test
    public void setCurrency() throws Exception {
        Current currency = new Current();
        currency.setBase("USD");
        Rates rates = new Rates();
        rates.setRUB(125);
        rates.setUSD(1.5);

        currency.setRates(rates);
        Current bottomCurrent = new Current();
        bottomCurrent.setBase("RUB");

        interActor.setBottomCurrent(bottomCurrent);
        interActor.setTopValue("123");
        TestObserver<String>bottomObserver = new TestObserver<>();
        interActor.setCurrency(currency, false).subscribe(bottomObserver);
        bottomObserver.assertValue(String.valueOf(123.0/125.0));

        interActor.setTopCurrent(bottomCurrent);
        interActor.setBottomValue("100");
        TestObserver<String>topObserver = new TestObserver<>();
        interActor.setCurrency(currency, true).subscribe(topObserver);
        topObserver.assertValue(String.valueOf(100.0/125.0));


    }

    @Test
    public void getRatesValues() throws Exception {
        interActor.getRatesValues().subscribe(strings1 -> {
            for (int i = 0; i < strings1.size(); i++) {
                assertEquals(strings1.get(i), Rates.class.getDeclaredFields()[i].getName().toUpperCase());
            }
        });
    }

    @Test
    public void parseValue() throws Exception {
        Current topCurrent = new Current();
        Current bottomCurrent = new Current();
        Rates rates = new Rates();
        rates.setUSD(0.234);
        topCurrent.setBase("RUB");
        topCurrent.setRates(rates);
        bottomCurrent.setBase("USD");
        Rates bottomRates = new Rates();
        bottomRates.setRUB(0.12);
        bottomCurrent.setRates(bottomRates);

        interActor.setTopCurrent(topCurrent);
        interActor.setBottomCurrent(bottomCurrent);

        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(invocation -> false);

        TestObserver<String>topObserver = new TestObserver<>();
        interActor.parseValue(true, "12").subscribe(topObserver);
        topObserver.assertValue(String.valueOf(12/0.234));

        TestObserver<String>bottomObserver = new TestObserver<>();
        interActor.parseValue(false, "12").subscribe(bottomObserver);
        bottomObserver.assertValue(String.valueOf(12/0.12));
    }

}