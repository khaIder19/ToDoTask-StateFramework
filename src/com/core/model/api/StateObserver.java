package com.core.model.api;

import com.core.model.TimeRange;

/**
 * This interface is used as Listener for State mutations, respectively for State "active" property change,
 * and State time-range change.
 */
public interface StateObserver {

    /**
     * This method is invoked whenever a State "active" property is mutated.
     * @param value true if State is "active"; false if it is no longer considered "active".
     */
    public void onStateChanged(boolean value);

    /**
     * This method is invoked whenever a State time-range is changed.
     * @param timeRange
     * @return true if the Listener implementation has successfully done its adjustment according to time-range passed; false if not.
     */
    public boolean onRangeChanged(TimeRange timeRange, State state);
}
