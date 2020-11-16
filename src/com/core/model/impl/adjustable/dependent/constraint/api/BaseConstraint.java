package com.core.model.impl.adjustable.dependent.constraint.api;

import com.core.model.api.State;
import com.core.model.impl.adjustable.dependent.constraint.ConstraintException;

public interface BaseConstraint {

    /**
     * Tests whether the "constraint condition" can be considered valid.
     * For a given set of State(s), this method defines the logic on which an dependent object property value depends and according to it can effectively be mutated.
     * @param states State set to evaluate.
     * @return true if the "constraint condition" is considered valid allowing a the properties dependent to act accordingly ; false if not.
     * @throws ConstraintException if the State set given is not supported by this Constraint implementation.
     */
    public boolean resolve(State[] states) throws ConstraintException;
}
