package com.core.model.api;


/**
 * A State can become the source of its events by allowing the subscription of observers that will notify those event to their listeners.
 */
public interface ObservableState {

    /**
     * Subscribes an observer to the state events.
     * @param listener The observer to subscribe.
     */
    public void addStateObserver(StateObserver listener);

    /**
     *  Removes a subscription from the state event.
     * @param listener The observer to remove.
     */
    public void removeStateObserver(StateObserver listener);
    
}
