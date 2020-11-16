package com.core.model.impl.adjustable.dependent;

/**
 * A destructible dependency is a State implementation having "destroy" capability, becoming the source of a "destruction" event call-back.
 *<p></p>
 * Objects interested in this event, can subscribe one or more Destroy Observers that will notify those events.
 */
public interface DestructibleDependency {

    /**
     * Adds a Destroy Observer to the current State observers list.
     * @param observer The given Destroy Observer to be added.
     */
    public void addDestroyObserver(DestroyObserver observer);

    /**
     * Removes the giben Destroy Observer from the current State observers list.
     * @param observer The given Destroy Observer to be removed.
     */
    public void removeDestroyObserver(DestroyObserver observer);

    /**
     * This method is invoked when the user is going to remove the underlining object from its runtime.
     */
    public void destroy();
}
