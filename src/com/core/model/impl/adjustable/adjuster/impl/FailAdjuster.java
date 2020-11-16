package com.core.model.impl.adjustable.adjuster.impl;

import com.core.model.TimeRange;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.adjuster.api.RangeAdjuster;
import com.core.model.impl.adjustable.adjuster.api.ResolutionType;

public class FailAdjuster implements RangeAdjuster {

    private Side side;
    private RangeAdjuster forSideAdjuster;

    public FailAdjuster(Side side,RangeAdjuster forSideAdjuster){
        this.side = side;
        this.forSideAdjuster = forSideAdjuster;
    }

    @Override
    public TimeRange adjustTo(TimeRange stateRange, TimeRange[] startValidationRanges, TimeRange[] endValidationRanges) {
        ResolutionType type = RangeAdjuster.getResolutionType(stateRange, startValidationRanges, endValidationRanges);

        if(side == null)
            return null;

        switch (type) {
            case IMP:
            case ALL:
                return null;
            case NAN:
                return stateRange;
            case START:
                if(side.equals(Side.START) && forSideAdjuster != null){
                    return forSideAdjuster.adjustTo(stateRange,startValidationRanges,endValidationRanges);
                }else{
                    return null;
                }
            case END:
                if(side.equals(Side.END) && forSideAdjuster != null){
                    return forSideAdjuster.adjustTo(stateRange,startValidationRanges,endValidationRanges);
                }else{
                    return null;
                }
            default:
                break;
        }
        return null;
    }
}
