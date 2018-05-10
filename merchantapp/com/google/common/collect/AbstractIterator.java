package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstractIterator<T> extends UnmodifiableIterator<T> {
    private T next;
    private State state;
    final /* synthetic */ Predicate val$retainIfTrue;
    final /* synthetic */ Iterator val$unfiltered;

    enum State {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

    protected AbstractIterator() {
        this.state = State.NOT_READY;
    }

    protected final T endOfData() {
        this.state = State.DONE;
        return null;
    }

    public final boolean hasNext() {
        Preconditions.checkState(this.state != State.FAILED);
        switch (this.state) {
            case DONE:
                return false;
            case READY:
                return true;
            default:
                return tryToComputeNext();
        }
    }

    private boolean tryToComputeNext() {
        this.state = State.FAILED;
        this.next = computeNext();
        if (this.state == State.DONE) {
            return false;
        }
        this.state = State.READY;
        return true;
    }

    public final T next() {
        if (hasNext()) {
            this.state = State.NOT_READY;
            T t = this.next;
            this.next = null;
            return t;
        }
        throw new NoSuchElementException();
    }

    AbstractIterator(Iterator it, Predicate predicate) {
        this.val$unfiltered = it;
        this.val$retainIfTrue = predicate;
        this();
    }

    protected T computeNext() {
        while (this.val$unfiltered.hasNext()) {
            T next = this.val$unfiltered.next();
            if (this.val$retainIfTrue.apply(next)) {
                return next;
            }
        }
        return endOfData();
    }
}
