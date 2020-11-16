package com.core.model.impl.adjustable.adjuster.impl;

import com.core.model.impl.adjustable.adjuster.api.ResolutionType;
import com.core.model.TimeRange;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.adjuster.api.RangeAdjuster;
import com.core.util.Constraints;

import java.io.Serializable;

/**
 * A Static Adjuster during the "resolution" process maintains the state range duration unchanged,<p></p>
 * if this adjuster will not be able to maintain duration unchanged then the "resolution" will fail.<p></p>
 * The inflexibility aspect of this adjuster is that a side adjustment will always imply the opposite side to be mutated,<p></p>
 * and for that reason this kind in many case will cause the "resolution" to fail.<p></p>
 *<p></p>
 * A static adjuster will move the State range to te first parameter available for each side involved.
 */
public class StaticAdjuster implements RangeAdjuster, Serializable {

    public StaticAdjuster(){

    }


    /**
     * Produces a time range with its duration unchanged, moved to first parameter available depending on a side involved.
     * @param stateRange The current state range to be adjusted.
     * @param startValidationRanges The state start side constraint ranges in which the state start has to be consistent with.
     * @param endValidationRanges The state end side constraint ranges in which the state end has to be consistent with..
     * @return The time range supposed to replace the state current range ; null if the "resolution" fails.
     */
    @Override
    public TimeRange adjustTo(TimeRange stateRange, TimeRange[] startValidationRanges, TimeRange[] endValidationRanges) {
        ResolutionType type = RangeAdjuster.getResolutionType(stateRange, startValidationRanges, endValidationRanges);
        switch (type) {
            case IMP:
            case ALL:
                return null;
            case NAN:
                return stateRange;
            case START:
                return staticAdjustForSideWithValidation(stateRange,startValidationRanges,Side.START,endValidationRanges);
            case END:
                return staticAdjustForSideWithValidation(stateRange,endValidationRanges,Side.END,startValidationRanges);
            default:
                break;
        }
        return null;
    }



    public static TimeRange staticAdjustForSide(TimeRange stateRange, TimeRange[] validationRange, Side side){
        TimeRange result = null;
        long oppositePositionResult = validationRange[0].getSide(side) - (Side.getSideOrdinal(side) * stateRange.getDuration());
        if(oppositePositionResult > (Long.MAX_VALUE -1)){
            return null;
        }
        if(oppositePositionResult < 1){
            return null;
        }
        switch (side){
            case START:
                result = new TimeRange(validationRange[0].getStart(),oppositePositionResult);
                break;
            case END:
                result = new TimeRange(oppositePositionResult,validationRange[0].getEnd());
                break;
            default:
                break;
        }
        return result;
    }



    private TimeRange staticAdjustForSideWithValidation(TimeRange stateRange, TimeRange[] validationRange, Side side,TimeRange[] oppositeValidationRanges){
        TimeRange result = staticAdjustForSide(stateRange,validationRange,side);
        if(result == null){
            return null;
        }
        if(Constraints.noneMatch(result.getSide(side),oppositeValidationRanges)){
            return null;
        }
        return result;
    }

}
