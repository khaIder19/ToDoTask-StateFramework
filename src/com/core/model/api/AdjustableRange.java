package com.core.model.api;

import com.core.model.TimeRange;


/**
 * A bound range represents a range of time where its sides (start ; end) are constrained to an interval of time called constraint range.
 * A range side in order to be "consistent" with its constraint range, it has to be equal or greater than start of constraint range, and,
 * equal or lower than end of constraint range, otherwise will be considered "out of range".
 *<p></p>
 * A constraint range in case of mutation can lead the constrained side to go "out of range", the process in which a side is brought back to consistency
 * is called "resolution".
 */
public interface AdjustableRange {

    /**
     * This method performs a "resolution" process in which a state range responds to a variation of one of its constraint range by adjusting itself in order to
     * be "consistent".
     * The if the stat range will not be able to adjust its range for a reason or another, then the "resolution" fails.
     * @param startValidationRange The start side constraint ranges in which the bound range has to be consistent with (Not null)
     * @param endValidationRange The end side constraint ranges in which the bound range has to be consistent with. (Not null)
     * @return Start ; End parameters supposed to replace the state involved in the resolution process ; null if the "resolution" process fails.
     */
    public TimeRange adjust(TimeRange[] startValidationRange,TimeRange[] endValidationRange);


    /**
     * Sets a constraint range of a specific side between (start ; end) with a given range parameter.
     *
     * note: The responsibility to perform "resolution" process (if needed) is given to the underlying implementation after invoking this method,
     *      also handling an eventually failure of the "resolution".
     * @param boundedRange
     * @return true if the constraint range has been changed successfully ; false if not.
     */
    public boolean setValidationRange(BoundedRange boundedRange);
}
