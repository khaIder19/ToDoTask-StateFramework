package com.core.model.impl.adjustable.dependent.bounds;

import com.core.model.api.BoundedRange;
import com.core.model.TimeRange;

public class BoundRangeWrapper implements BoundedRange {

    private TimeRange[] startRanges;
    private TimeRange[] endRanges;

    public BoundRangeWrapper(TimeRange[] start, TimeRange[] end){
        this.startRanges = start;
        this.endRanges = end;
    }

    @Override
    public TimeRange[] getStartSideValidationRanges() {
        return startRanges;
    }

    @Override
    public TimeRange[] getEndSideValidationRanges() {
        return endRanges;
    }

}
