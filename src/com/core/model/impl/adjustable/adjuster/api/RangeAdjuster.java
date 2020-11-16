package com.core.model.impl.adjustable.adjuster.api;

import com.core.model.TimeRange;
import com.core.model.impl.Side;
import com.core.util.Constraints;
import com.core.util.States;

/**
 * A Range Adjuster is responsible to provide a Time Range as the result of a "resolution" process or any other situation.
 *<p></p>
 * A Range Adjuster defines a function called "range adjuster function" which takes the following arguments :
 *<p></p>
 * 1- A given the state’s current range, on which the range adjuster has to base its measurements.
 * 2- The given State's start side constraint ranges, on which the range adjuster targets its adjustment in order to move the state start side if considered inconsistent.
 * 3- State end side constraint ranges. The same consideration made for the start side is equal for the end side.
 *<p></p>
 * The return of a "range adjuster" function is another range, supposed to replace and move the state from an inconsistency condition to an "consistent" condition.
 *<p></p>
 * NOTE : the third parameter or the second will be the opposite side "constraint ranges" on which a given Range Adjuster implementation can take decisions if
 * the "resolution" logic will have a successful outcome , or it will result in an "out of range" for the opposite side involved.
 *<p></p>
 * NOTE : Each range adjuster has its own character which establishes if the given state’s current range is actually invalidated,
 * and manages also a possible resolution failure if the given parameter does not satisfy its logic.
 * <p></p>
 *
 * Hint : An implementation can take advantage of the getResolutionType to understand the type and the nature of a "resolution".
 */
public interface RangeAdjuster {


    public TimeRange adjustTo(TimeRange stateRange,TimeRange[] startValidationRanges,TimeRange[] endValidationRanges);


    /**
     * Returns an enum representing the "resolution" type of a given parameter supposed to be the arguments of the "resolution" process.
     * @param stateRange
     * @param startValidationRanges
     * @param endValidationRanges
     * @return
     */
    public static ResolutionType getResolutionType(TimeRange stateRange, TimeRange[] startValidationRanges, TimeRange[] endValidationRanges) {
        boolean startInvalidLogic = Constraints.noneMatch(stateRange.getStart(), startValidationRanges);
        boolean endInvalidLogic = Constraints.noneMatch(stateRange.getEnd(), endValidationRanges);

        if(TimeRange.max(endValidationRanges).getEnd() < startValidationRanges[0].getStart()){
            return ResolutionType.IMP;
        }

        if (startInvalidLogic && endInvalidLogic) {
            return ResolutionType.ALL;
        } else if (startInvalidLogic) {
            return ResolutionType.START;
        } else if (endInvalidLogic) {
            return ResolutionType.END;
        } else {
            return ResolutionType.NAN;
        }
    }



    public static TimeRange[] getEligibleRanges(long param,Side side,TimeRange[] validationRanges,boolean clearRanges){
        TimeRange[] result = null;
        switch (side){
            case START:
                result = States.greaterThanRanges(param,validationRanges,clearRanges);
                break;
            case END:
                result = States.lowerThanRanges(param,validationRanges,clearRanges);
                break;
        }
        return result;
    }

}
