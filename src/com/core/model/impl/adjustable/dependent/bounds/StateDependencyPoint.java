package com.core.model.impl.adjustable.dependent.bounds;

import com.core.model.impl.adjustable.dependent.states.DependencyState;
import com.core.model.impl.adjustable.dependent.states.DependencyStateObserver;

/**
 * This extension of a dependency point allows to insert an Observer to listen mutation / variations events coming from the underlining dependency set.
 */
public class StateDependencyPoint extends DependencyPoint{

    private Long id;

    private DependencyStateObserver sideObserver;

    protected StateDependencyPoint(){

    }

    public StateDependencyPoint(int arg){
        super(arg);
        sideObserver = null;
    }

    public DependencyStateObserver getSideObserver(DependencyState state) {
        if(sideObserver == null){
            sideObserver = new DependencyStateObserver(this,state);
        }
        return sideObserver;
    }
}
