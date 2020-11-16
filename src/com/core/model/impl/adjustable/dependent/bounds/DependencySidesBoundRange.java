package com.core.model.impl.adjustable.dependent.bounds;

import com.core.model.api.BoundedRange;
import com.core.model.TimeRange;

import java.io.Serializable;

/**
 * A DependencySidesBoundRange is an implementation of BoundedRange in which the "constraint ranges" are the result of a dependency relationship to a set of State(s).
 * @see DependencyPoint
 */
public class DependencySidesBoundRange implements BoundedRange, Serializable {

    private Long id;

    private StateDependencyPoint startSide;

    private StateDependencyPoint endSide;

    protected DependencySidesBoundRange(){

    }

    public DependencySidesBoundRange(StateDependencyPoint start,StateDependencyPoint end){
        startSide = start;
        endSide = end;
    }

    @Override
    public TimeRange[] getStartSideValidationRanges() {
        return startSide.getValidationRanges();
    }

    @Override
    public TimeRange[] getEndSideValidationRanges() {
        return endSide.getValidationRanges();
    }

    public StateDependencyPoint getStartSide() {
        return startSide;
    }

    public StateDependencyPoint getEndSide() {
        return endSide;
    }
}
