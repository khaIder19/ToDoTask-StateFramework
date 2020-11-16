package com.core.model.impl.adjustable.adjuster.api;

/**
 * Indicates the "resolution" type.
 */
public enum ResolutionType {
    /**
     * Both start and end side are invalid.
     */
    ALL,
    /**
     * Only the start side is invalid.
     */
    START,
    /**
     * Only the end side is invalid.
     */
    END,
    /**
     * Both side are already "consistent" with their constraint ranges.
     */
    NAN
    /**
     * Will not be possible to perform the "resolution" due to constraint ranges inversion (end constraint range < start constraint range).
     */
    ,IMP;
}
