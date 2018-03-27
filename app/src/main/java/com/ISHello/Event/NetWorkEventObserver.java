package com.ISHello.Event;


/**
 * Event observer.
 * <p>
 * This class implemented to make event source observer.
 */
public interface NetWorkEventObserver {
    /**
     * Handle controller events.
     *
     * @param event The actor event.
     * @return True if handled.
     */
    public boolean handleEvent(NetWorkEvent event);

}
