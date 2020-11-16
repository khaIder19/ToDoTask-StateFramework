package com.core.model.api;

import com.core.model.impl.adjustable.dependent.exc.StateException;

/**
 * A State Object joins different State(s) in order to become together a singe piece of activity unit.
 */
public interface StateObject {

    /**
     * Return the set of State(s) combined.
     * @return State set combined.
     */
    public State[] getStateSet();

    /**
     * Test whether the object in its complex can be considered extinct, as "extinct" property of a State
     * @see State
     * @return true if is "extinct" ; false otherwise.
     */
    public boolean isStateObjectExtinct();

    /**
     * Aims to destroy each State combined, when they are no longer need in the runtime.
     */
    public void destroy();
}
