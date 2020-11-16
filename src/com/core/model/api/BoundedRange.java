package com.core.model.api;

import com.core.model.TimeRange;

/**
 * A bounded range abstracts one or more intervals of time in which a parameter has to be restricted in.
 *<p></p>
 * A bound range defines for each side (start ; end) one ore more intervals.
 */
public interface BoundedRange{

    /**
     * Returns the start side intervals allowed.
     * @return
     */
    public TimeRange[] getStartSideValidationRanges();

    /**
     * Returns the end side intervals allowed.
     * @return
     */
    public TimeRange[] getEndSideValidationRanges();
}
