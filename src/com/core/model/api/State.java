package com.core.model.api;

import com.core.model.TimeRange;

/**
 * A state represents a generic condition that assumes a value between true and false in during a precise interval of time, when its condition is true the state will be considered "active".
 * State has a start-time and it has an end-time, the state condition can be "active" during all the interval of its range time or partially.
 * <p></p>
 * The State is considered "Defined" if: the state becomes totally "active" during all its interval of time.
 * <p></p>
 * The State is considered "Undefined" if: the state becomes partially "active" during its interval of time until its end-time (it can also be not "active" totally during that interval).
 * <p></p>
 */
public interface State{

    /**
     * Test whether a State is considered "active" or not.
     * @return true if this state is "active"; false if not.
     */
    public boolean getStateValue();

    /**
     * This method produces the interval time in which the state is "active", or interval time in which this state can be "active".
     * @return interval time as {@code TimeRange}.
     */
    public TimeRange getRange();

    /**
     * Test whether this State is considered "Undefined" or not.
     * @return true if this state is "Undefined"; false if not.
     */
    public boolean isUndefined();

}
