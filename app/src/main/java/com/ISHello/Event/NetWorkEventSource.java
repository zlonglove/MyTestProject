package com.ISHello.Event;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import android.util.Log;


public class NetWorkEventSource {
    private final static int OBSERVERS_CAPACITY = 10;

    private final static int MAX_EVENTS = 40;

    private ArrayList<NetWorkEventObserver> observers;
    private ArrayBlockingQueue<NetWorkEvent> pool;
    private ArrayBlockingQueue<NetWorkEvent> events;

    private static NetWorkEventSource _instance = null;

    /**
     * Singleton instance.
     *
     * @return The instance.
     */
    public synchronized static NetWorkEventSource instance() {
        if (_instance == null) {
            _instance = new NetWorkEventSource();
        }

        return _instance;
    }

    /**
     * Create observer list.
     */
    public NetWorkEventSource() {
        observers = new ArrayList<NetWorkEventObserver>(OBSERVERS_CAPACITY);

        pool = new ArrayBlockingQueue<NetWorkEvent>(MAX_EVENTS);
        events = new ArrayBlockingQueue<NetWorkEvent>(MAX_EVENTS);

        // Fill event pool.
        for (int index = 0; index < MAX_EVENTS; index++) {
            pool.add(new NetWorkEvent());
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
                NetWorkEvent event = events.poll();

                if (event != null) {
                    boolean handled = false;

                    int size = observers.size();
                    for (int index = 0; index < size; index++) {
                        handled = observers.get(index).handleEvent(event);

                        if (handled)
                            break;
                    }

                    // Place data event back onto pool.
                    pool.put(event);
                }
            }
        } catch (InterruptedException e) {

        }
    }

    /**
     * Add observer.
     *
     * @param observer The target observer.
     */
    public synchronized void addObserver(NetWorkEventObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer.
     *
     * @param observer Target event observer.
     */
    public synchronized void removeObserver(NetWorkEventObserver observer) {
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
    public void sendEvent(int id, Object object) {
        process(id, object);
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
    private void process(int id, Object object) {
        try {
            NetWorkEvent event = pool.poll();

            if (event != null) {
                // Populate our reusable event.
                event.populate(id, System.currentTimeMillis(), object);

                events.put(event);
            } else {
                Log.i("ActorEventSource",
                        "No event from pool available to service event, " + id);
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
