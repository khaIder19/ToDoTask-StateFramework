package com.core.model;

import com.core.model.impl.Side;

import java.io.Serializable;
import java.util.*;

/**
 * TimeRange abstracts a one dimensional time interval between a start-time parameter and an end-time parameter.
 *<p></p>
 * TimeRange is an immutable object, and uses long type as relative time unit representation.
 */
public final class TimeRange implements Serializable,Comparable<TimeRange> {

    private long start;
    private long end;

    public static final TimeRange UNDEF_TIME_RANGE = new TimeRange(Long.MIN_VALUE,Long.MAX_VALUE);

    private TimeRange(){
    }

    /**
     * Creates a new range with given parameters.
     * @param start start parameter.
     * @param end end parameter.
     * @throws IllegalArgumentException if end < start;
     */
    public TimeRange(long start,long end){
        if(end < start){
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
    }

    /**
     * Get the start-time parameter.
     * @return
     */
    public long getStart(){
        return start;
    }

    /**
     * Get the end-time parameter.
     * @return
     */
    public long getEnd(){
        return end;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeRange timeRange = (TimeRange) o;
        return start == timeRange.start &&
                end == timeRange.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    /**
     * Returns a side parameter given its corresponding enum value.
     * @param side Side enum (START : END).
     * @return value of the given side.
     */
    public long getSide(Side side){
       long toReturn = Long.MIN_VALUE;
       switch (side){
            case START:
                toReturn = getStart();
                break;
            case END:
                toReturn = getEnd();
                break;
        }
        return toReturn;
    }

    /**
     * Returns the opposite side enum value of another given side enum value.
     * @param side the given side.
     * @return START is the opposite of END ; and vice versa.
     */
    public static Side getOpposite(Side side){
        Side result = null;
        switch (side){
            case START:
                result = Side.END;
                break;
            case END:
                result = Side.START;
                break;
        }
        return result;
    }

    /**
     *
     * @param x
     * @param y
     * @param side
     * @return
     */
    public static boolean forwardDirectionComparison(long x,long y,Side side) {
        boolean result = false;
        switch (side) {
            case START:
                result = x > y;
                break;
            case END:
                result = x < y;
                break;
        }
        return result;
    }

    /**
     * Compares the given set of intervals and returns the greatest.
     * @param set
     * @return
     */
    public static TimeRange max(TimeRange[] set){
        List list = Arrays.asList(set);
        return (TimeRange) Collections.max(list);
    }

    /**
     * Compares the given set of intervals and returns the lowest.
     * @param set
     * @return
     */
    public static TimeRange min(TimeRange[] set){
        List list = Arrays.asList(set);
        return (TimeRange) Collections.min(list);
    }

    /**
     * Get the duration of the current range.
     * @return
     */
    public long getDuration(){
        long start = getStart();
        long end = getEnd();
        if(!(start == Long.MIN_VALUE || end == Long.MAX_VALUE)){
            return end - start;
        }
        return Long.MAX_VALUE;
    }

    /**
     * Compares a time range to another.
     *
     * returns 0: if this range is equal to the given range.
     *         1: if this range is greater.
     *         -1: if this range is lower.
     * @param o The range to be compared with this range.
     * @return
     */
    @Override
    public int compareTo(TimeRange o) {
        int c = Long.compare(getStart(),o.getStart());
        if(c == 0){
            if(getEnd() > getEnd()){
                return 1;
            }else if (getEnd() < o.getEnd()){
                return -1;
            }
        }
        return c;
    }
}
