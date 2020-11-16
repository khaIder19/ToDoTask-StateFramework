package com.core.model.impl.adjustable.dependent.states;

import com.core.model.TimeRange;
import com.core.model.api.State;
import com.core.model.api.StateObserver;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.dependent.bounds.DependencyPoint;

import java.io.Serializable;

public class DependencyStateObserver implements StateObserver, Serializable {

    private Long id;

    private DependencyPoint stateSide;
    private DependencyState dependencyState;

    public DependencyStateObserver(DependencyPoint depPoint,DependencyState state){
        this.dependencyState = state;
        this.stateSide = depPoint;
    }

    private DependencyStateObserver(){
    }


    @Override
    public void onStateChanged(boolean value) {
        dependencyState.setResolvableNotification();
    }

    @Override
    public boolean onRangeChanged(TimeRange timeRange, State state) {

        if(dependencyState.isExtinct()){
            return true;
        }

        if(!dependencyState.setValidationRange(dependencyState.getBoundedRange())){

            switch (Side.valueOf(stateSide.getArg())) {
                case START:
                    dependencyState.forceRemoveStartTimeDependency(state);
                    break;
                case END:
                    dependencyState.forceRemoveEndTimeDependency(state);
                    break;
            }

            return false;
        }
        return true;
    }

    public DependencyState getDependencyState() {
        return dependencyState;
    }

}
