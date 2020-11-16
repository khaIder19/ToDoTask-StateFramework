package com.core.model.impl.adjustable.dependent;

import com.core.model.impl.adjustable.dependent.exc.DependencyException;
import com.core.model.impl.adjustable.dependent.states.DependencyState;


/**
 * This interface is used a Listener for State "destruction" event call-back.
 */
public interface DestroyObserver {

    /**
     * This method is invoked when a State is going to be removed from the runtime,
     * allowing the interested object in the same environment, to react accordingly.
     * @param state The given State to be "destroyed".
     * @throws DependencyException
     */
    public void onDestory(DependencyState state);

}
