package com.core.model.impl.adjustable.dependent.states.impl;

import com.core.model.TimeRange;
import com.core.model.api.State;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.adjuster.impl.DynamicAdjuster;
import com.core.model.impl.adjustable.adjuster.impl.FailAdjuster;
import com.core.model.impl.adjustable.dependent.ReversibleState;
import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;
import com.core.model.impl.adjustable.dependent.exc.NotSupportedDependency;
import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.impl.adjustable.dependent.states.DependencyState;

/**
 * No Start State abstracts a State where its start side is undefined or "infinite".
 * Its end undefinition also makes its range "infinite".
 */
public class NoStartState extends DependencyState implements ReversibleState {

    private Long id;

    protected NoStartState(){
    }

    private NoStartState(long end, boolean undefined){
        super(new TimeRange(Long.MAX_VALUE,end),undefined,new FailAdjuster(Side.END,new DynamicAdjuster()));
    }



    public static NoStartState getEndTimeRange(long end, boolean undefined){
        return new NoStartState(end,undefined);
    }


    @Override
    public boolean setTime(long start, long end){
        if(start != Long.MIN_VALUE){
            return false;
        }
        return super.setTime(start, end);
    }

    public boolean setStartTime(long end){
        return setTime(Long.MIN_VALUE,end);
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
    public boolean addStartTimeDependency(State state) throws StateException {
        if(!isUndefined()){
            throw new NotSupportedDependency("This NoStartState is not considered 'undefined'");
        }
        return super.addStartTimeDependency(state);
    }

    @Override
    public boolean setStartTimeConstraint(Constraint startTimeConstraint) {
        if (!isUndefined()) {
            return false;
        }
        return super.setStartTimeConstraint(startTimeConstraint);
    }

    @Override
    public TimeRange getRange() {
        return new TimeRange(Long.MAX_VALUE,getRange().getEnd());
    }

    @Override
    public State getLeft() {
       return null;
    }

    @Override
    public State getRight() {
        return NoEndState.getStartTimeRange(getRange().getStart(),false);
    }

}
