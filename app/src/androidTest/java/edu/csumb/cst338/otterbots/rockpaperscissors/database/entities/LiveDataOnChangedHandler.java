package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

/**
 * Handler function template for LiveData Observer
 * @param <T> the type we will be operating on.
 */
@FunctionalInterface
interface LiveDataOnChangedHandler<T> {
    /**
     * Handle the onChange for livedata observer
     * @param data the data we will handle.
     */
    abstract void handle(T data) throws InterruptedException;
}
