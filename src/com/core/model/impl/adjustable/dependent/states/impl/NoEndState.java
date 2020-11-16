package com.core.model.impl.adjustable.dependent.states.impl;

import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;
import com.core.model.impl.adjustable.dependent.exc.NotSupportedDependency;
import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.impl.adjustable.dependent.states.DependencyState;
import com.core.model.api.State;
import com.core.model.TimeRange;
import com.core.model.impl.adjustable.dependent.ReversibleState;
import com.core.model.impl.adjustable.adjuster.impl.DynamicAdjuster;

/**
 * No End State abstracts a State where its end side is undefined or "infinite".
 * Its end undefinition also makes its range "infinite",
 * unlike a Range State where its "active" condition becomes in sequence 'false' then 'true' then 'false' again,
 * a No End State "active" condition becomes one time 'false' at beginning and then 'true' for an undefined time.
 */
public class NoEndState extends DependencyState implements ReversibleState {

    private Long id;

    protected NoEndState(){
    }

    private NoEndState(long start, boolean undefined){
        super(new TimeRange(start,Long.MAX_VALUE),undefined,new DynamicAdjuster());
    }



    public static NoEndState getStartTimeRange(long start, boolean undefined){
        return new NoEndState(start,undefined);
    }

    @Override
    public boolean setEndTimeConstraint(Constraint endTimeConstraint) {
        return false;
    }

    @Override
    public boolean addEndTimeDependency(State state) throws StateException {
        throw new NotSupportedDependency("NoEndState does not support end time dependencies");
    }

    @Override
    public boolean setTime(long start, long end){
        if(end != Long.MAX_VALUE){
            return false;
        }
        return super.setTime(start, end);
    }

    public boolean setStartTime(long start){
        return setTime(start,Long.MAX_VALUE);
    }


    @Override
    public boolean setStateValue(boolean value) {
        boolean result = false;
         if(result = super.setStateValue(value)){
             if(value){
                 extinct = true;
             }
        }
        return result;
    }

    @Override
    public TimeRange getRange() {
        return new TimeRange(stateRange.getStart(),Long.MAX_VALUE);
    }

    @Override
    public State getLeft() {
        return NoStartState.getEndTimeRange(getRange().getStart(),false);
    }

    @Override
    public State getRight() {
        return null;
    }
}
