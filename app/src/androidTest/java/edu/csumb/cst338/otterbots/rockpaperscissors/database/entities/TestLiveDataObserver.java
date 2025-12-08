package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



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
    public boolean test(LiveData<T> liveData, java.util.function.Predicate<T> condition, LiveDataOnChangedHandler<T> handler) throws InterruptedException {
        // set a latch that we can decrement once our tests are done
        final CountDownLatch latch = new CountDownLatch(1);

        // the actual observer
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T data) {
                // don't act on our data too early! wait for preconditions
                if (!condition.test(data)) {
                    return;
                }
                // handler will run the actual tests we are interested in
                handler.handle(data);
                // now that we are done, decrement the latch
                latch.countDown();

                // without this, removeObserver happens in a background thread and that's broken.
                // wrap it so it runs on the main thread.
                ArchTaskExecutor.getInstance().executeOnMainThread(
                        () -> liveData.removeObserver(this)
                );
            }
        };
        // run the observer
        liveData.observeForever(observer);
        // wait for the latch to decrement for up to the defined timeout value. Tests should handle the return value
        // or we could end up with false positives for tests.
        return latch.await(LATCH_TIMEOUT, LATCH_TIMEOUT_UNITS);
    }
}
