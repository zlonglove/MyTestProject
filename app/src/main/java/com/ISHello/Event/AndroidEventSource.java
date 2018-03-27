package com.ISHello.Event;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import android.util.Log;

public class AndroidEventSource {
    private final static int OBSERVERS_CAPACITY = 10;

    private final static int MAX_EVENTS = 40;

    private ArrayList<AndroidEventObserver> observers;
    private ArrayBlockingQueue<AndroidEvent> pool;
    private ArrayBlockingQueue<AndroidEvent> events;

    private static AndroidEventSource _instance = null;

    /**
     * Singleton instance.
     *
     * @return The instance.
     */
    public synchronized static AndroidEventSource instance() {
        if (_instance == null) {
            _instance = new AndroidEventSource();
        }

        return _instance;
    }

    /**
     * Create observer list.
     */
    public AndroidEventSource() {
        observers = new ArrayList<AndroidEventObserver>(OBSERVERS_CAPACITY);

        pool = new ArrayBlockingQueue<AndroidEvent>(MAX_EVENTS);
        events = new ArrayBlockingQueue<AndroidEvent>(MAX_EVENTS);

        // Fill event pool.
        for (int index = 0; index < MAX_EVENTS; index++) {
            pool.add(new AndroidEvent());
        }
    }

    /**
     * Update any observers.
     *
     * @throws InterruptedException
     */
    public void update() {
        try {
            if (events.size() > 0) {
                AndroidEvent event = events.poll();

                if (event != null) {
                    boolean handled = false;

                    int size = observers.size();
                    for (int index = 0; index < size; index++) {
                        handled = observers.get(index).androidhandleEvent(event);

                        if (handled)
                            break;
                    }

                    // Place data event back onto pool.
                    pool.put(event);
                }
            }
        } catch (InterruptedException e) {
            // Nowt
        }
    }

    /**
     * Add observer.
     *
     * @param observer The target observer.
     */
    public synchronized void addObserver(AndroidEventObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer Target event observer.
     */
    public synchronized void removeObserver(AndroidEventObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    /**
     * Enqueue event
     *
     * @param id    The event id.
     * @param actor The associated actor.
     */
    public void sendAndroidEvent(AndroidEvents _doWhat, String _para) {
        process(_doWhat, _para);
    }

    /**
     * Map event data to pooled event.
     * <p>
     * Place pooled event on to broadcast queue.
     * <p>
     * NOTE: This uses "poll" not "take". The reason being that the events
     * source is not another thread but the main loop of the application. We
     * cannot block the loop so we try to enqueue the event if we can otherwise
     * we notify by an error message.
     *
     * @param id    The event id.
     * @param actor The associated actor.
     */
    private void process(AndroidEvents _doWhat, String _para) {
        try {
            AndroidEvent event = pool.poll();

            if (event != null) {
                // Populate our reusable event.
                event.populate(_doWhat, _para, System.currentTimeMillis());
                events.put(event);
            } else {
                Log.d("ActorEventSource",
                        "No event from pool available to service event, " + _doWhat);
            }
        } catch (InterruptedException e) {
            // Nowt
        }
    }

    /**
     * Clear structures.
     */
    public void clear() {
        observers.clear();
        events.clear();
    }

}
