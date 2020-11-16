package com.core.model.impl.adjustable.dependent.states.impl;

import com.core.model.impl.adjustable.dependent.states.DependencyState;
import com.core.model.api.State;
import com.core.model.TimeRange;
import com.core.model.impl.adjustable.dependent.ReversibleState;
import com.core.model.impl.adjustable.adjuster.api.RangeAdjuster;


public class RangeState extends DependencyState implements ReversibleState {

    private Long id;

    protected RangeState(){
    }

    protected RangeState(long start, long end, RangeAdjuster adjuster,boolean isUndefined){
        super(new TimeRange(start, end),isUndefined,adjuster);
    }


    public static RangeState getDefinedRange(long start, long end, RangeAdjuster adjuster){
        return new RangeState(start, end,adjuster,false);
    }

    public static RangeState getUndefinedRange(long start, long end, RangeAdjuster adjuster){
        return new RangeState(start, end,adjuster,true);
    }

    @Override
    public State getLeft() {
        return NoStartState.getEndTimeRange(stateRange.getStart(),false);
    }

    @Override
    public State getRight() {
        return NoEndState.getStartTimeRange(stateRange.getEnd(),false);
    }
}
