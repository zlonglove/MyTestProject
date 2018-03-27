package com.ISHello.Event;


/**
 * Base event type.
 */
public class NetWorkEvent {
    private int id;
    private long time;
    private Object object;

    /**
     * Populate data event from system event.
     *
     * @param id   Event id.
     * @param node Associated event actor.
     * @param time Event time.
     */
    public void populate(int id, long time, Object object) {
        this.id = id;
        this.time = time;
        this.object = object;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public Object getObject() {
        return object;
    }

}
