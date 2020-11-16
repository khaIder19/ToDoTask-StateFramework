package com.core.model.impl.adjustable.dependent.exc;

public class LoopDependencyException extends DependencyException {

    public LoopDependencyException() {
    }

    public LoopDependencyException(String message) {
        super(message);
    }

}
