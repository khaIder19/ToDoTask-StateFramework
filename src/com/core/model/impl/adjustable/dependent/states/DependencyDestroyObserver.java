package com.core.model.impl.adjustable.dependent.states;

import com.core.model.impl.adjustable.dependent.DestroyObserver;

import java.io.Serializable;

public class DependencyDestroyObserver implements DestroyObserver, Serializable {

    private Long id;

    private DependencyState dependencyState;

    public DependencyDestroyObserver(DependencyState dependencyState) {
        this.dependencyState = dependencyState;
    }

    private DependencyDestroyObserver(){

    }

    @Override
    public void onDestory(DependencyState state){
        dependencyState.forceRemoveStartTimeDependency(state);
        dependencyState.forceRemoveEndTimeDependency(state);
    }

}
