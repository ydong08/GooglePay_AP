package dagger.internal;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;

public final class ProblemDetector {

    static class ArraySet<T> extends AbstractSet<T> {
        private final ArrayList<T> list = new ArrayList();

        ArraySet() {
        }

        public boolean add(T t) {
            this.list.add(t);
            return true;
        }

        public Iterator<T> iterator() {
            return this.list.iterator();
        }

        public int size() {
            throw new UnsupportedOperationException();
        }
    }
}
