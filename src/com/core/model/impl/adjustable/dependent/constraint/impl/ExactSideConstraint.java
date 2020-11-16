package com.core.model.impl.adjustable.dependent.constraint.impl;

import com.core.model.api.State;
import com.core.model.TimeRange;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.dependent.constraint.ConstraintException;
import com.core.util.States;


/**
 * This constraint targets exactly one side of a dependency,
 * it will return a single constraint range with a start and end equal to the targeted side (start = end).
 *<p></p>
 * An Exact Side Constraint support one or >1 number of State(s), by default just 1.
 *<p></p>
 * If specified it can be used for multiple State(s), and it will return : the lowest value, if the targeted side is START ;
 * the greatest value, if the targeted side is END.
 *
 */
public class ExactSideConstraint extends ArgConstraint {

    private Side side;
    private boolean onlyOneLimit = true;
    private boolean freeApply = true;

    public ExactSideConstraint(){
    }

    public ExactSideConstraint(Side progressSide){
        this(progressSide,false);
    }


    public ExactSideConstraint(Side progressSide,boolean oneOnly){
        this.side = progressSide;
        this.onlyOneLimit = oneOnly;
    }


    /**
     * Supposed to be used for evaluate "undefined state" dependencies.
     *
     * The Constraint is "valid" if : the State of the targeted START side is considered "active" ;
     * the State of the targeted" END side is considered not "active".
     * @param states State set to evaluate.
     * @return true if "valid" ; false if not.
     * @throws ConstraintException if this type supports only one State.
     */
    @Override
    public boolean resolve(State[] states) throws ConstraintException {
        if(states.length == 0){
            throw new ConstraintException("At leas one state is supported");
        }
        if(onlyOneLimit && states.length > 1){
            throw new ConstraintException("Only one state is supported");
        }
        if(freeApply){
            return true;
        }
        switch (side){
            case START:
                return States.minStartTimeState(states).getStateValue();
            case END:
               return States.maxEndTimeState(states).getStateValue();
        }
        return false;
    }


    /**
     * This "constraint range" function returns a TimeRange with duration = 0 (start side = end side),
     * due to the fact of targeting a side of a State which is only one digit.
     * @param states State set to evaluate.
     * @return An array containing just one interval, having its start and end parameter equal to the side targeted value.
     * @throws ConstraintException if this type supports only one State.
     */
    @Override
    public TimeRange[] resolveConstraintRange(State[] states) throws ConstraintException {
        TimeRange[] constraintRange = new TimeRange[1];
        if(states.length == 0){
            throw new ConstraintException("At leas one state is supported");
        }
        if(onlyOneLimit && states.length > 1){
            throw new ConstraintException("Only one state is supported");
        }
            switch (side) {
                case END:
                    long max = States.maxEndTime(states);
                    constraintRange[0] = new TimeRange(max, max);
                    if(max == Long.MAX_VALUE){
                        constraintRange[0] = returnDefaultRange();
                    }
                    break;
                case START:
                    long min = States.minStartTime(states);
                    constraintRange[0] = new TimeRange(min, min);
                    if(min == Long.MIN_VALUE){
                        constraintRange[0] = returnDefaultRange();
                    }
                    break;
            }
            return constraintRange;
    }

    private TimeRange returnDefaultRange(){
        return new TimeRange(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     *
     */
    public void setFreeApply(boolean freeApply){
        this.freeApply = freeApply;
    }

    @Override
    public String toString() {
        return "{" +
                "arg0=" + side +
                ",arg1=" + onlyOneLimit +
                ",arg2=" + freeApply +
                '}';
    }

    @Override
    public void setArgs(Object[] args) {
        this.side = Side.valueOf((String) args[0]);
        this.onlyOneLimit = Boolean.valueOf((String) args[1]);
        this.freeApply = Boolean.valueOf((String) args[2]);
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{side,onlyOneLimit,freeApply};
    }
}
