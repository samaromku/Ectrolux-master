package ru.savchenko.andrey.ectrolux;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.savchenko.andrey.ectrolux.main.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity>mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void spinnerClickTest(){
        onView(withId(R.id.etBottomCurrency)).perform(typeText("test"));


        onView(withId(R.id.etBottomCurrency)).perform(typeText("123"));

        onView(withId(R.id.etBottomCurrency)).check(matches(ViewMatchers.hasFocus()));

        onView(withId(R.id.spBottomCurrency)).perform(click());

        onData(allOf(is(instanceOf(String.class)), is("USD"))).perform(click());

        onView(withId(R.id.spBottomCurrency)).check(matches(withSpinnerText(containsString("USD"))));

//        .check(matches(withText("test")))

    }



}
