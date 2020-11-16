package com.core.model.impl.adjustable.dependent;

import com.core.model.api.State;

/**
 * A Reversible State returns it negative ranges : a negative range is the interval of time in which a State "active" condition is considered 'false'.
 *<p></p>
 * Therefore a Range State will have two negative ranges the 'left' and the 'right',
 * not all State implementation will have one ore more negative ranges, lets consider an undefined range which is always "active",
 * or else an No End State which has only one negative range (the 'left').
 *<p></p>
 */
public interface ReversibleState {

    /**
     * Returns the 'left' side range in which te current State is considered not "active".
     * @return null if the current State can not express its 'left' side.
     */
    public State getLeft();

    /**
     * Returns the 'right' side range in which te current State is considered not "active".
     * @return null if the current State can not express its 'right' side.
     */
    public State getRight();

}
