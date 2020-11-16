package com.core.model.impl.adjustable.dependent.constraint.impl;

import com.core.model.api.State;
import com.core.model.TimeRange;


public class DisjunctionConstraint extends ArgConstraint {

    @Override
    public boolean resolve(State[] states) {
        return true;
    }



    @Override
    public TimeRange[] resolveConstraintRange(State[] states) {
        TimeRange[] ranges = new TimeRange[states.length];
        for(int i =0;i<states.length;i++){
            ranges[i] = states[i].getRange();
        }
        return ranges;
    }



    @Override
    public void setArgs(Object[] args) {
    }



    @Override
    public Object[] getArgs() {
        return new Object[0];
    }

}
