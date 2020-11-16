package com.core.model.impl.adjustable.dependent.bounds;

import com.core.model.impl.adjustable.dependent.constraint.ConstraintException;
import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;
import com.core.model.impl.adjustable.dependent.exc.DependencyException;
import com.core.model.impl.adjustable.dependent.exc.NotSupportedDependency;
import com.core.model.api.State;
import com.core.model.TimeRange;
import com.core.util.States;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A dependency point is a restricted variable parameter, which can assume a certain value between a limited interval called "constraint range".
 *<p></p>
 * If dependency point parameter is equal or greater then constraint range's start AND lower or equal then constraint range's end, the can be considered "valid".
 *<p></p>
 * A "constraint range" is the result of a dependency relationship between an object's variable property which this dependency point represent,
 * and a set of State(s) entities.
 * The responsibility to provide a "constraint range" is given to a "constraint function",
 * which defines its own logic returning different or equal result based on the State(s) entity set.
 * A dependency point allows the insertion and removal of State(s) set elements, only after "constraint function" has been assigned to it.
 *<p></p>
 * The object dependent can have multiple properties eligible for be a dependency point, for that reason is possible to set a numeric argument to make distinctions between them.
 *<p></p>
 * Note on "dependency set" variation :
 *<p></p>
 * [For each variation of State range contained in the "dependency set" will invalidate the "constraint range" a recalculation of the same is needed.
 * responsibility to perform this operation is left to the user.]
 *
 */
public class DependencyPoint implements Serializable {

    private Long id;

    private Constraint constraint;

    private Set<State> dependencySet;

    private int arg;

    protected DependencyPoint(){
    }


    public DependencyPoint(int arg){
        this.dependencySet = new HashSet<>();
        this.arg = arg;
    }


    /**
     * Adds a State object to the current dependency set.
     * @param state State object to add.
     * @return true if the State object is successfully been added ; false if not.
     * @throws DependencyException if the "constraint function" does not support the given State.
     * @throws IllegalStateException if a "constraint function" is not present.
     */
    public boolean addDependency(State state) throws DependencyException {

        if(getConstraint() == null)
            throw new IllegalStateException("Constraint is not present");

        if(dependencySet.contains(state))
            return false;

        try {
            getConstraint().resolveConstraintRange(States.tempDependencySet(getDependecySet(),new State[]{state}));
        } catch (ConstraintException e) {
            throw new NotSupportedDependency(e.getMessage());
        }

        return dependencySet.add(state);
    }


    /**
     * Set the "constraint function" implementation which will define the constraint logic on the current "dependency set".
     * @param constraint The "constraint function" implementation.
     * @return true if it has been successfully added ; false if a "constraint function" is already present.
     */
    public boolean setConstraint(Constraint constraint){
        if(this.constraint == null){
            this.constraint = constraint;
            return true;
        }else {
            return false;
        }
    }

    /**
     * Tests whether the "constraint condition" can be considered valid.
     * @return
     */
    public boolean isResolvable(){

        if(getConstraint() == null || getDependecySet().length == 0)
            return true;

        boolean result = false;
        try {
            result = getConstraint().resolve(getDependecySet());
        } catch (ConstraintException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     *  Removes a State object from the current dependency set.
     * @param state The State object to remove.
     * @return true if the State has been removed successfully ; false if not.
     */
    public boolean removeDependency(State state){
        return dependencySet.remove(state);
    }


    /**
     * Returns the elements of the current "dependency set".
     * @return array of State objects.
     */
    public State[] getDependecySet(){
        State[] set = new State[dependencySet.size()];
        return dependencySet.toArray(set);
    }

    /**
     * Test whether a State object is contained in the "dependency set".
     * @param state State object to test.
     * @return true if is contained ; false if not.
     */
    public boolean containsState(State state){
        return dependencySet.contains(state);
    }


    /**
     * Tests whether the given parameter can be considered "valid" inside one of the current "constraint range" intervals.
     * @param n The parameter to validate.
     * @return true if the given parameter is considered "valid" ; false if not.
     */
    public boolean validateConstraint(long n){
        if(constraint != null && getDependecySet().length > 0) {
            TimeRange[] validationRanges = new TimeRange[0];
            try {
                validationRanges = constraint.resolveConstraintRange(getDependecySet());
            } catch (ConstraintException e) {
                e.printStackTrace();
            }
            return anyMatch(n,validationRanges);
        }
        return true;
    }

    /**
     *
     * @param n
     * @param ranges
     * @return
     */
    private boolean anyMatch(long n,TimeRange[] ranges){
        for(int i = 0; i < ranges.length;i++){
            if(n < ranges[i].getEnd() & n > ranges[i].getStart()){
                return true;
            }
        }
        return false;
    }

    /**
     *  Return the intervals of the "constraint range".
     * @return
     */
    public TimeRange[] getValidationRanges() {
        if(getDependecySet().length > 0){
            try {
                return getConstraint().resolveConstraintRange(getDependecySet());
            } catch (ConstraintException e) {
                e.printStackTrace();
            }
        }
        return new TimeRange[]{new TimeRange(Long.MIN_VALUE,Long.MAX_VALUE)};
    }


    /**
     * Gets the current implementation of "constraint function".
     * @return The "constraint function" implementation instance.
     */
    public Constraint getConstraint(){
        return constraint;
    }


    /**
     * Bring back this side to initial state, without the "constraint function" and without any element in the "dependency set".
     */
    public void reset(){
        constraint = null;
        dependencySet.clear();
    }

    /**
     * Gets the argument of this dependency point, in order to make distinction between multiple points.
     * @return the argument as int number.
     */
    public int getArg(){
        return arg;
    }
}
