package com.wubydax.laughtertime;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    String resultString = null;
    CountDownLatch countDownLatch;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        countDownLatch.countDown();
    }

    public void testAsyncTask() throws InterruptedException {
        FetchJoke fetchJoke = new FetchJoke(getContext().getApplicationContext());
        fetchJoke.setListener(new FetchJoke.FetchJokeListener() {
            @Override
            public void onComplete(String result) {
                resultString = result;
            }
        }).execute();
        countDownLatch.await(10, TimeUnit.SECONDS);
        assertNotNull(resultString);

    }
}