package com.core.model.impl.adjustable.adjuster.impl;

import com.core.model.impl.adjustable.adjuster.api.ResolutionType;
import com.core.model.TimeRange;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.adjuster.api.RangeAdjuster;
import com.core.util.Constraints;

import java.io.Serializable;

/**
 * A Dynamic Adjuster behaves in two different ways, depending on the "constraint range" parameters in which the State has to be adjusted,
 * in the "static" case the adjuster will maintain the State range duration unchanged, and,
 * in the "dynamic" case the adjuster will not.
 *<p></p>
 * premise :
 *<p></p>
 * [Lets define the "opposite direction" and "forward direction" of a range side.
 * For the start side of a range its "opposite direction" is any value lower than him, the "forward direction" is any parameter greater.
 * For the end side of a range, its "opposite direction" is any value greater than him, the "forward direction" is any parameter lower.]
 *<p></p>
 * The "forward direction" of a range side includes also its opposite side, such as :
 * (start side's opposite : end side | end side's opposite : start)
 *<p></p>
 * A Dynamic Adjuster will assume a "dynamic" behavior when the "constraint range" is situated in the "opposite direction" of the side involved in the "resolution".
 * Instead when the "constraint range" is situated in the "forward direction" then they rise two other aspects :
 *<p></p>
 * If the "constraint range" is over ( > ) the opposite side of the side involved, then the the adjustment will behave in a "static" manner.
 * If the "constraint range" is not over the opposite side of the side involved, then adjustment will still behave in a "dynamic" manner.
 *<p></p>
 * The previous consideration ar valid only when one side is involved, when both side are involved in the resolution the dynamic adjuster will
 * assume always a "dynamic" behavior.
 *<p></p>
 * In both cases the adjuster will move the side involved to the first parameter available in the "constraint range".
 * For the start side, its first parameter will be the lowest value of the "constraint range" (constraint range's start).
 * For the end side, its first parameter available will be the greatest value of the "constraint range" (constraint range's end).
 * <p></p>
 * "Dynamic" behavior will never cause a "resolution" to fail, except for an impossible resolution type.
 *<p></p>
 */
public class DynamicAdjuster implements RangeAdjuster,Serializable {



    public DynamicAdjuster(){
    }



    @Override
    public TimeRange adjustTo(TimeRange stateRange, TimeRange[] startValidationRanges, TimeRange[] endValidationRanges) {
        ResolutionType type = RangeAdjuster.getResolutionType(stateRange, startValidationRanges, endValidationRanges);
        TimeRange result = null;
        switch (type){
            case IMP:
                return null;
            case NAN:
                return stateRange;
            case START:
                result = adjustForSideWithValidation(stateRange,Side.START,startValidationRanges,endValidationRanges);
                break;
            case END:
                result = adjustForSideWithValidation(stateRange,Side.END,endValidationRanges,startValidationRanges);
                break;
            case ALL:
                result = new TimeRange(TimeRange.max(startValidationRanges).getStart(),TimeRange.min(endValidationRanges).getEnd());
                break;
            default:
                break;
        }
        return result;
    }



    public static TimeRange adjustForSide(TimeRange stateRange, Side side, TimeRange[] validationRange){
        TimeRange adjustment = null;
        if(stateRange.getSide(side) < validationRange[0].getStart()){
                if(TimeRange.forwardDirectionComparison(stateRange.getSide(TimeRange.getOpposite(side)),validationRange[0].getSide(side),side)){
                    adjustment = swapRangeSides(stateRange.getSide(TimeRange.getOpposite(side)),validationRange[0],side);
                }else{
                    return StaticAdjuster.staticAdjustForSide(stateRange,validationRange,side);
                }
        }else{
            adjustment = swapRangeSides(stateRange.getSide(TimeRange.getOpposite(side)),validationRange[0],side);
        }
        return adjustment;
    }



    private TimeRange adjustForSideWithValidation(TimeRange stateRange, Side side, TimeRange[] validationRange,TimeRange[] oppositeValidationRanges){
        TimeRange result = adjustForSide(stateRange,side,validationRange);
        if(result == null){
            return null;
        }
        if(Constraints.noneMatch(result.getSide(TimeRange.getOpposite(side)),oppositeValidationRanges)){
            return null;
        }
        return result;
    }



    private static TimeRange swapRangeSides(long param,TimeRange validationRange,Side side){
        TimeRange result = null;
        switch (side){
            case START:
                result = new TimeRange(validationRange.getStart(), param);
                break;
            case END:
                result = new TimeRange(param,validationRange.getEnd());
                break;
        }
        return result;
    }

}

