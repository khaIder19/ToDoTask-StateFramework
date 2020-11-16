package com.core.model.impl.adjustable.dependent.constraint.impl;

import com.core.model.api.State;
import com.core.model.TimeRange;
import com.core.model.impl.adjustable.dependent.ReversibleState;
import com.core.model.impl.adjustable.dependent.constraint.ConstraintException;
import com.core.util.Constraints;
import com.core.util.States;

/**
 * A No End Constraint targets the 'extinct' range of a given set of dependencies.
 * So given a dependency set it will produce a constraint range representing the interval in which the last element of the set (greatest end side) becomes extinct,
 * so the constraint range’s start will be equal to the last element’s end side value, however the end of constraint range will be undefined/infinite.
 *<p></p>
 * A No End Constraint supports only Reversible State, due to fact of providing their undefined end side.
 * @see ReversibleState
 */
public class NoEndConstraint extends ArgConstraint {

    public NoEndConstraint(){

    }

    /**
     * The constraint will be considered "valid" if all State(s) of a given dependency set are considered "extinct" (or "active").
     * @param states State set to evaluate.
     * @return true if this constraint is "valid" ; false if not.
     */
    @Override
    public boolean resolve(State[] states) {
        return Constraints.applyAllNoEnd(states);
    }


    /**
     * This "range constraint" function will produce a constraint range representing the interval in which the last element of the set (greatest end side) becomes extinct,
     * so the constraint range’s start will be equal to the last element’s end side value, however the end of constraint range will be undefined/infinite.
     * @param states State set to evaluate.
     * @return an array containing one singe interval, as with the above description.
     * @throws ConstraintException if one of the given State(s) is not an ReversibleState:
     */
    @Override
    public TimeRange[] resolveConstraintRange(State[] states) throws ConstraintException {
        for(State s : states){
            if(!(s instanceof ReversibleState)){
               throw new ConstraintException("Only ReversibleState(s) are supported");
            }
        }
        TimeRange[] constraintRange = new TimeRange[1];
        long max = States.maxStartTime(toEvalutate(states));
        constraintRange[0] = new TimeRange(max,Long.MAX_VALUE);
        return constraintRange;
    }

    /**
     * For each Reversible State of a given State(s) set, this methods returns its "right" side.
     * @param raw the given State(s) set.
     * @return "right" sides.
     */
    private State[] toEvalutate(State[] raw) {
        State[] states = new State[raw.length];
        for (int i = 0; i < raw.length; i++) {
            if (((ReversibleState) raw[i]).getRight() != null) {
                states[i] = ((ReversibleState) raw[i]).getRight();
            } else {
                states[i] = raw[i];
            }
        }
        return states;
    }

    @Override
    public void setArgs(Object[] args) {
    }

    @Override
    public Object[] getArgs() {
        return new Object[0];
    }
}
