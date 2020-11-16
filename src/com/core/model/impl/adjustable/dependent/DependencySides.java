package com.core.model.impl.adjustable.dependent;

import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;
import com.core.model.api.State;

/**
 * Dependency Sides abstracts an entity containing two dependency points : start side ; end side .
 */
public interface DependencySides {



    /**
     * Returns the Constraint implementation which establishes the logic between start side and its "dependency set".
     * @return Constraint implementation; null if this State does not define a start-time constraint.
     */
    public Constraint getStartTimeConstraint();



    /**
     * Returns the Constraint implementation which establishes the logic between end side and its "dependency set".
     * @return Constraint implementation; null if this State does not define a start-time constraint.
     */
    public Constraint getEndTimeConstraint();



    /**
     * Returns the "dependency set" on which the underlying entity start side is depending.
     * @return array of State objects ; array returned can be 0 length if there is no dependency.
     */
    public State[] getEndTimeDepedencySet();



    /**
     *  Returns the "dependency set" on which the underlying entity start side is depending.
     * @return array of State objects ; array returned can be 0 length if there is no dependency.
     */
    public State[] getStartTimeDepedencySet();

}
