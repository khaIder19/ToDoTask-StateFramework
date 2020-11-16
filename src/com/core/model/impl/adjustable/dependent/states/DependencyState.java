package com.core.model.impl.adjustable.dependent.states;

import com.core.model.api.BoundedRange;
import com.core.model.impl.adjustable.dependent.DependencySides;
import com.core.model.impl.adjustable.dependent.DestroyObserver;
import com.core.model.impl.adjustable.dependent.DestructibleDependency;
import com.core.model.impl.adjustable.dependent.bounds.BoundRangeWrapper;
import com.core.model.impl.adjustable.dependent.bounds.DependencySidesBoundRange;
import com.core.model.impl.adjustable.dependent.constraint.ConstraintException;
import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;
import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.api.State;
import com.core.model.TimeRange;
import com.core.model.api.ObservableState;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.StateAdjuster;
import com.core.model.impl.adjustable.dependent.bounds.StateDependencyPoint;
import com.core.model.impl.adjustable.adjuster.api.RangeAdjuster;
import com.core.model.impl.adjustable.dependent.exc.LoopDependencyException;
import com.core.model.impl.adjustable.dependent.exc.NotAdjustableException;
import com.core.util.States;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A dependency state is an extension of state adjuster,
 * which can express the willingness to make its range side (start ; end) and its behavior of "active" condition property,
 * dependent on one or more different State(s) objects.
 * <p></p>
 * The State sides are the two points in which express its dependency. Each point defines its "dependency set", and gives to a Constraint implementation
 * the responsibility to provide a "constraint range" in which the relative side has to be restricted with, based on a specific logic.
 * <p></p>
 * In addition the "constraint condition" defined by the Constraint will handle dependencies to "undefined" states which require and imply particular implementations
 * regarding the setting of state "active" condition.
 * <p></p>
 * When adding a dependency to one of the sides, it can lead to a "resolution" process depending on the Constraint logic and on the Range Adjuster in use,
 * the same happens when a dependency is removed.
 * <p></p>
 * Note on life-cycle and state destruction :
 * <p></p>
 * In a runtime environment many Dependency State(s) can depend on each other.
 * When A dependency is no longer needed in the runtime environment, the user has to remove manually from all of its dependents,
 * but a State Dependency does not provide the set of State(s) which are currently depending on it,
 * that will make difficult the remotion (by the user) of that dependency.
 * For that reason a Dependency State emulate its destruction through the invocation of a corresponding method,
 * that will generate a call-back event handled by its dependents in order act accordingly.
 * <b></b>
 * Note on "dependency set" variation :
 * [Each side of a Dependency State will react to its "dependency set" range variations, performing a "resolution" process if needed,
 * if the "resolution" fails then the entire "dependency set" will be cleared, due to impossibility to keep the State in a "inconsistency" condition.
 * This process is called "force remotion".
 *<p></p>
 * One this State is "extinct" will not receive other variation notifications anymore.
 * ]
 *<p></p>
 * Note on concurrency: Adding and removing dependencies is thread-safe.
 *
 */
public class DependencyState extends StateAdjuster implements DependencySides, DestructibleDependency {

    public Long id;

    private DependencySidesBoundRange sides;

    /**
     * This flag is used to deny state "active" setting when a State has a one or more dependencies.
     * When the "constraint condition" of the start side dependency point can be considered "valid", then this flag has to be set to 'true', to 'false' otherwise.
     */
    private boolean activeAllowed = true;

    private List<DestroyObserver> destroyObserverList;

    private DestroyObserver singleDestroyObserver;


    protected DependencyState(){
    }

    public DependencyState(TimeRange range, boolean isUndefined, RangeAdjuster adjuster) {
        super(range, isUndefined,adjuster);
        sides = new DependencySidesBoundRange(new StateDependencyPoint(0),new StateDependencyPoint(1));
        setValidationRange(sides);
        destroyObserverList = new CopyOnWriteArrayList<>();
    }


    /**
     * Adds to the start side "dependency set" the given dependency State object.
     *<p></p>
     * This method will create a relationship between the current State and the given State,
     * that will result in a restriction of its side parameter to in interval of time provided by its assigned Constraint logic.
     * The restriction of the start side will condition the state "active" property, restricting its setting of 'true' value.
     *<p></p>
     * The dependency is validated before, to avoid "loop dependencies".
     *<p></p>
     * The insertion of an element into a "dependency set" will lead to "resolution" process,
     * and depending on the Constraint and the Range Adjuster currently in use, the "resolution" can succeed or can fails,
     * if its fails the dependency will not be added.
     * A lot of thing can go wrong during the invocation of this method, in that case the method will throw a Dependency Exception.
     *<p></p>
     * Note: A dependency will not be added if the current state is considered "extinct".
     *
     * @param state the given State to be added.
     * @return true if the given State is successfully added ; false if the "resolution" fails ; the current stat is "extinct" ; an exception is thrown, or, an any other reason.
     * @throws StateException if the "resolution fails" ; if a loop dependency occurs ; if the State is not supported by the the side Constraint set.
     */
    public boolean addStartTimeDependency(State state) throws StateException {
        return addTimeDependency(sides.getStartSide(),state);
    }


    /**
     * Adds to the end side "dependency set" the given dependency State object.
     *<p></p>
     * This method will create a relationship between the current State and the given State,
     * that will result in a restriction of its side parameter to in interval of time provided by its assigned Constraint logic.
     * The restriction of the end side will condition the state "active" property, restricting its setting of 'false' value.
     *<p></p>
     * The dependency is validated before, to avoid "loop dependencies".
     *<p></p>
     * The insertion of an element into a "dependency set" will lead to "resolution" process,
     * and depending on the Constraint and the Range Adjuster currently in use, the "resolution" can succeed or can fails,
     * if its fails the dependency will not be added.
     * A lot of thing can go wrong during the invocation of this method, in that case the method will throw a Dependency Exception.
     *<p></p>
     * @param state the given State to be added.
     * @return true if the given State is successfully added ; false if the "resolution" fail , or, an exception is thrown, or, an any other reason.
     * @throws StateException if the "resolution fails" ; if a loop dependency occurs ; if the State is not supported by the the side Constraint set.
     */
    public boolean addEndTimeDependency(State state) throws StateException {
        return addTimeDependency(sides.getEndSide(),state);
    }


    /**
     *  Adds a dependency as State object, to the given Dependency point (state side).
     *  The dependency is validated before, to avoid "loop dependencies".
     *<p></p>
     *  The insertion of a dependency into a Dependency point can lead to "resolution" process,
     *  in case of "resolution" failure or any problem occurs during the operation, the dependency will not be added.
     *<p></p>
     *  When a dependency is successfully added and is "observable" capable, a State Observer will be subscribed to that dependency along with and a Destruction Observer if "destroy" capable.
     *  in order to react to their events properly :
     *<p></p>
     *  For "dependency set" range variation : see the notes above regarding this aspect.
     *  Notice also that"dependency set" variation from one side will not involve the opposite side.
     *<p></p>
     *  For a "dependency destruction" : the dependency will be removed from "dependency set" (performing a "force romotion" if needed).
     *<p></p>
     *
     * @param stateSide dependency point in which the given State will be added.
     * @param state the given State to be added.
     * @return true if the given State is successfully added ; false if the "resolution" fail , or, an exception is thrown, or, an any other reason.
     * @throws StateException if the "resolution fails" ; if a loop dependency occurs ; if the State is not supported by the the side Constraint set.
     */
    private boolean addTimeDependency(StateDependencyPoint stateSide, State state) throws StateException {
        if(isExtinct()){
            return false;
        }

        validateLoopDependency(state);
        boolean result = false;

            result = stateSide.addDependency(state);
            if(!result)
                return false;


            if(!setValidationRange(sides)){

                stateSide.removeDependency(state);
                throw new NotAdjustableException("State range can not be adjusted for this dependency");

            }else{

                if(state instanceof ObservableState){
                    ((ObservableState)state).addStateObserver(stateSide.getSideObserver(this));
                }

                if(state instanceof DestructibleDependency){
                    ((DestructibleDependency)state).addDestroyObserver(getDestroyObserver());
                }

                setResolvableNotification();
            }

            return result;
        }

    /**
     *
     * @param dependency
     */
    private void addDestroyObserverToDependency(State dependency){
        if(dependency instanceof DestructibleDependency){
            ((DestructibleDependency)dependency).addDestroyObserver(getDestroyObserver());
        }
    }

    /**
     * Removes all observers subscribed to the given dependency point's state(s).
     * @param side
     */
    private void removeAllObservers(StateDependencyPoint side){
        for(State depState : side.getDependecySet()) {
            if(depState instanceof ObservableState){
                ((ObservableState)depState).removeStateObserver(side.getSideObserver(this));
            }
            if (depState instanceof DestroyObserver) {
                ((DestructibleDependency)depState).removeDestroyObserver(singleDestroyObserver);
            }
        }
    }


    /**
     * Tests whether the start dependency point Constraint implementation can be considered "valid",
     * If so, then the state "active" property can be set to 'true',
     */
    public void setResolvableNotification(){
            activeAllowed = sides.getStartSide().isResolvable();
    }

    /**
     * Tests whether the given State has already a dependency on the current state, resulting in a circular dependency :
     *<p></p>
     * A circular dependency happen when a State A depends on a State B, and B depends again on A.
     *<p></p>
     * @param state the State to evaluate.
     * @throws LoopDependencyException if a circular (loop) dependency occurs ; or the given State is 'this' State.
     */
    private void validateLoopDependency(State state) throws LoopDependencyException {
        if(state.equals(this)){
            throw new LoopDependencyException("State has a circular dependency");
        }

        if(state instanceof DependencyState) {

            State[] toValidate = States.joinSets(((DependencyState)state).getStartTimeDepedencySet(),((DependencyState)state).getEndTimeDepedencySet());

            for (State s : toValidate) {

                if (s.equals(this)) {
                    throw new LoopDependencyException("State has a circular dependency");
                }

                if(s instanceof DependencyState){
                    if (((DependencyState)state).getStartTimeDepedencySet() != null && ((DependencyState)state).getEndTimeDepedencySet() != null) {
                        validateLoopDependency(s);
                    }
                }

            }
        }
    }


    /**
     * Removes from start side "dependency set" the given dependency State object.
     *<p></p>
     * This method will remove the relationship between the current State and the given State,
     <p></p>
     * The removal of a dependency from "dependency set" will lead to "resolution" process,
     * and depending on the Constraint and the Range Adjuster currently in use, the "resolution" can succeed or can fail,
     * if its fails the dependency will not be removed.
     * A lot of thing can go wrong during the invocation of this method, in that case the method will throw a Dependency Exception.
     *
     * Note: If the current state is considered "extinct", then the given dependency will still be removed but it will not involve any state property.
     *
     * @param state the given State to be removed.
     * @return true if the given State is successfully removed ; false if the "resolution" fail , or, an exception is thrown, or, an any other reason.
     */
    public boolean removeStartTimeDependency(State state) throws StateException {
        return removeDependency(sides.getStartSide(),state,false);
    }


    /**
     * Removes from end side "dependency set" the given dependency State object.
     *<p></p>
     * This method will remove the relationship between the current State and the given State.
     * The removal of a dependency from "dependency set" will lead to "resolution" process,
     * and depending on the Constraint and the Range Adjuster currently in use, the "resolution" can succeed or can fails,
     * if its fails the dependency will not be removed.
     * A lot of thing can go wrong during the invocation of this method, in that case the method will throw a Dependency Exception.
     *<p></p>
     * @param state the given State to be removed.
     * @return true if the given State is successfully removed ; false if the "resolution" fail , or, an exception is thrown, or, an any other reason.
     */
    public boolean removeEndTimeDependency(State state) throws StateException {
        return removeDependency(sides.getEndSide(),state,false);
    }


    /**
     * Removes from end side "dependency set" the given dependency State object.
     *<p></p>
     * This method will remove the relationship between the current State and the given State,
     * The removal of a dependency from "dependency set" will lead to "resolution" process,
     * and depending on the Constraint and the Range Adjuster currently in use, the "resolution" can succeed or can fails,
     * if its fails the entire "dependency set" will be cleared.
     * A lot of thing can go wrong during the invocation of this method, in that case the method will throw a Dependency Exception.
     *<p></p>
     * @param state the given State to be removed.
     * @return true if the given State is successfully removed ; false if the "resolution" fail , or, an exception is thrown, or, an any other reason.
     */
    public boolean forceRemoveEndTimeDependency(State state) {
            return forceRemoveDependency(sides.getEndSide(),state);
    }


    /**
     * Removes from start side "dependency set" the given dependency State object.
     *<p></p>
     * This method will remove the relationship between the current State and the given State,
     * The removal of a dependency from "dependency set" will lead to "resolution" process,
     * and depending on the Constraint and the Range Adjuster currently in use, the "resolution" can succeed or can fails,
     * if its fails the entire "dependency set" will be cleared.
     * A lot of thing can go wrong during the invocation of this method, in that case the method will throw a Dependency Exception.
     *<p></p>
     * @param state the given State to be removed.
     * @return true if the given State is successfully removed ; false if the "resolution" fail , or, an exception is thrown, or, an any other reason.
     */
    public boolean forceRemoveStartTimeDependency(State state){
            return forceRemoveDependency(sides.getStartSide(),state);
    }


    /**
     *  Removes a dependency from the given Dependency point (start-side-point ; end-side-pint).
     *<p></p>
     *  The removal of a dependency from a Dependency point can lead to "resolution" process,
     *  in case of "resolution" failure : the entire "dependency set" will be cleared if a "force remotion" is specified,
     *  otherwise the given dependency will not be removed.
     *<p></p>
     *  When a dependency is successfully removed, a State Observer and a Destruction Observer will be un-subscribed from that dependency
     *<p></p>
     * @param stateSide dependency point between (start-side-point ; end-side-pint) in which the given State will be removed.
     * @param state the given State to be removed.
     * @param forceReset true : if a "force remotion" will happen in case of "resolution" failure ; false : the given dependency will not be removed.
     * @throws NotAdjustableException if the "resolution" fails.
     * @return
     */
    private boolean removeDependency(StateDependencyPoint stateSide, State state, boolean forceReset) throws NotAdjustableException {

        if(isExtinct()){
            stateSide.removeDependency(state);
            if(state instanceof ObservableState){
                ((ObservableState)state).removeStateObserver(stateSide.getSideObserver(this));
            }
            return true;
        }

        BoundRangeWrapper toValidate = null;
        boolean result = false;

        if(!stateSide.containsState(state))
            return false;

        try {
            TimeRange[] remotionTest = null;

            //Here the method is just trying  to test (without actually removing anything) if the given dependency once removed will cause the "resolution failure",


            State[] toEvaluate = States.subtractFromSet(stateSide.getDependecySet(),state);
            if(toEvaluate.length == 0){
                remotionTest = new TimeRange[]{TimeRange.UNDEF_TIME_RANGE};
                activeAllowed = true;
            }else{
                remotionTest = stateSide.getConstraint().resolveConstraintRange(toEvaluate);
                // the "remotion test" is the "constraint range" that would be returned if the given dependency was removed.
            }
            switch (stateSide.getArg()){
                case 0:
                    toValidate = new BoundRangeWrapper(remotionTest,getBoundedRange().getEndSideValidationRanges());
                    break;
                case 1:
                    toValidate = new BoundRangeWrapper(getBoundedRange().getStartSideValidationRanges(),remotionTest);
                    break;
            }
        } catch (ConstraintException e) {
            //Assuming dependency removal will not cause an incompatibility by throwing an exception.
        }


        //force remotion
            if(forceReset){
                stateSide.reset();
                result = true;
            }else{
                if(setValidationRange(toValidate)){
                    result = stateSide.removeDependency(state);
                    if(state instanceof ObservableState){
                        ((ObservableState)state).removeStateObserver(stateSide.getSideObserver(this));
                    }

                }else{
                    throw new NotAdjustableException();
                }
            }

        return result;
    }

    /**
     *
     * @param startSide
     * @param state
     * @return
     */
    private boolean forceRemoveDependency(StateDependencyPoint startSide,State state){
        try {
            return removeDependency(startSide,state,true);
        } catch (StateException e) {

        }
        return false;
    }

    /**
     * Sets the "constraint function" implementation which will define the constraint logic on the current start side "dependency set".
     * @param startTimeConstraint The "constraint function" implementation.
     * @return true if it has been successfully added ; false if a "constraint function" is already present.
     */
    public boolean setStartTimeConstraint(Constraint startTimeConstraint){
        return setTimeConstraint(startTimeConstraint,Side.START);
    }

    /**
     * Sets the "constraint function" implementation which will define the constraint logic on the current end side "dependency set".
     * @param endTimeConstraint The "constraint function" implementation.
     * @return true if it has been successfully added ; false if a "constraint function" is already present.
     */
    public boolean setEndTimeConstraint(Constraint endTimeConstraint){
        return setTimeConstraint(endTimeConstraint,Side.END);
    }


    /**
     * Sets the "constraint function" implementation which will define the constraint logic on the given dependency point.
     * @param timeConstraint The "constraint function" implementation.
     * @param side specify which side between (start ; end).
     * @return
     */
    private boolean setTimeConstraint(Constraint timeConstraint, Side side){
        boolean operation = true;

        switch (side){
            case START:
                operation = sides.getStartSide().setConstraint(timeConstraint);
                break;
            case END:
                operation = sides.getEndSide().setConstraint(timeConstraint);
                break;
            default:
                break;
        }

        return operation;
    }



    @Override
    public boolean setStateValue(boolean value) {
        boolean result = false;
        if (value) {
            if (activeAllowed) {
                result = super.setStateValue(value);
            }else {
                return false;
            }
        }else {
            result =  super.setStateValue(value);
            if(result){
                removeAllObservers(sides.getStartSide());
                removeAllObservers(sides.getEndSide());
            }
        }
        return result;
    }



    @Override
    public Constraint getStartTimeConstraint() {
        return sides.getStartSide().getConstraint();
    }



    @Override
    public Constraint getEndTimeConstraint() {
        return sides.getEndSide().getConstraint();
    }



    @Override
    public State[] getEndTimeDepedencySet() {
        return sides.getEndSide().getDependecySet();
    }



    @Override
    public State[] getStartTimeDepedencySet() {
        return sides.getStartSide().getDependecySet();
    }


    /**
     * Returns an implementation of the Destroy Observer supposed to listen to "destroy" call-backs events generated by each State of "dependency set".
     * @return
     */
    private DestroyObserver getDestroyObserver(){
        if(singleDestroyObserver == null){
            singleDestroyObserver = new DependencyDestroyObserver(this);
        }
        return singleDestroyObserver;
    }


    /**
     * Registers a Destroy Observer to the current Dependency State,
     * and listen to "destroy" call-backs event generated when the State is going to be removed from runtime.
     *
     * @param observer
     */
    @Override
    public void addDestroyObserver(DestroyObserver observer) {
        destroyObserverList.add(observer);
    }

    /**
     * When this Dependency State is no longer needed in the runtime,
     * the user can call this method which will notify the interested object in the same environment to act accordingly.
     */
    @Override
    public void destroy(){
        for(DestroyObserver observer : destroyObserverList){
            observer.onDestory(this);
        }
    }

    @Override
    public void removeDestroyObserver(DestroyObserver observer) {
        destroyObserverList.remove(observer);
    }

    @Override
    public BoundedRange getBoundedRange() {
        return sides;
    }


}
