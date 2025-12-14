package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.junit.rules.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;


/**
 * Template class to observe live data change results
 * @param <T> the type of data we will be acting on in our change handler
 */
public class TestLiveDataObserver<T> {
    // timeout values for the latch
    public final static int LATCH_TIMEOUT = 1;
    public final static TimeUnit LATCH_TIMEOUT_UNITS = TimeUnit.SECONDS;

    /**
     * This is where we will run our tests or timeout trying.
     * @param liveData the LiveData<T> we will be acting on
     * @param condition Wait for this condition to be true to run our change handler. this protects against early intermediate changes
     * @param handler This is what will run on our LiveData wrapped data. we want to do our data testing here.
     * @return boolean if the observer times out. this will happen if our 'condition' is not met.
     * @throws InterruptedException if something interrupts the processing
     */
    boolean test(LiveData<T> liveData, java.util.function.Predicate<T> condition, LiveDataOnChangedHandler<T> handler) throws InterruptedException {
        return test(liveData, condition, handler, LATCH_TIMEOUT);
    }
    boolean test(LiveData<T> liveData, Predicate<T> condition, LiveDataOnChangedHandler<T> handler, Integer timeoutSeconds) throws InterruptedException {
        // set a latch that we can decrement once our tests are done

        try {
            Object data = getOrAwaitValue(liveData, condition, timeoutSeconds);
            handler.handle((T) data);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    T getOrAwaitValue(LiveData liveData, Predicate<T> condition, Integer timeoutSeconds) throws TimeoutException {
        final Object[] returnData = new Object[1];
        final CountDownLatch latch;
        try {
            latch = new CountDownLatch(1);
        } catch (Exception e) {
            System.out.println("Cannot make new latch "+e);
            throw new RuntimeException(e);
        }

        // the actual observer
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T data) {
                try {
                    System.out.println("TESTING LIVE DATA CHANGE " + data);
                    // don't act on our data too early! wait for preconditions
                    if (!condition.test(data)) {
                        return;
                    }
                    // handler will run the actual tests we are interested in
                    returnData[0] = data;
                    // now that we are done, decrement the latch
                    latch.countDown();

                    // without this, removeObserver happens in a background thread and that's broken.
                    // wrap it so it runs on the main thread.
                    ArchTaskExecutor.getInstance().executeOnMainThread(
                            () -> liveData.removeObserver(this)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in test observer onChanged: "+e);
                }
            }
        };
        // run the observer
        liveData.observeForever(observer);
        // wait for the latch to decrement for up to the defined timeout value. Tests should handle the return value
        // or we could end up with false positives for tests.
        if (timeoutSeconds == null) {
            timeoutSeconds = LATCH_TIMEOUT;
        }
        boolean success = false;
        try {
            success = latch.await(timeoutSeconds, LATCH_TIMEOUT_UNITS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!success) {
            throw new TimeoutException("Latch TimeOut");
        }
        return (T) returnData[0];
    }
}
