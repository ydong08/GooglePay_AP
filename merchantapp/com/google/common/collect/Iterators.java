package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {
    static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator<Object>() {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return false;
        }

        public Object previous() {
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return 0;
        }

        public int previousIndex() {
            return -1;
        }
    };
    private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator<Object>() {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            CollectPreconditions.checkRemove(false);
        }
    };

    class AnonymousClass10 extends AbstractIndexedListIterator<T> {
        final /* synthetic */ Object[] val$array;
        final /* synthetic */ int val$offset;

        AnonymousClass10(int i, int i2, Object[] objArr, int i3) {
            this.val$array = objArr;
            this.val$offset = i3;
            super(i, i2);
        }

        protected T get(int i) {
            return this.val$array[this.val$offset + i];
        }
    }

    class AnonymousClass11 extends UnmodifiableIterator<T> {
        boolean done;
        final /* synthetic */ Object val$value;

        AnonymousClass11(Object obj) {
            this.val$value = obj;
        }

        public boolean hasNext() {
            return !this.done;
        }

        public T next() {
            if (this.done) {
                throw new NoSuchElementException();
            }
            this.done = true;
            return this.val$value;
        }
    }

    class AnonymousClass3 extends UnmodifiableIterator<T> {
        final /* synthetic */ Iterator val$iterator;

        AnonymousClass3(Iterator it) {
            this.val$iterator = it;
        }

        public boolean hasNext() {
            return this.val$iterator.hasNext();
        }

        public T next() {
            return this.val$iterator.next();
        }
    }

    class AnonymousClass7 extends TransformedIterator<F, T> {
        final /* synthetic */ Function val$function;

        AnonymousClass7(Iterator it, Function function) {
            this.val$function = function;
            super(it);
        }

        T transform(F f) {
            return this.val$function.apply(f);
        }
    }

    static class PeekingImpl<E> implements PeekingIterator<E> {
        private boolean hasPeeked;
        private final Iterator<? extends E> iterator;
        private E peekedElement;

        public PeekingImpl(Iterator<? extends E> it) {
            this.iterator = (Iterator) Preconditions.checkNotNull(it);
        }

        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }

        public E next() {
            if (!this.hasPeeked) {
                return this.iterator.next();
            }
            E e = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return e;
        }

        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }

        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = this.iterator.next();
                this.hasPeeked = true;
            }
            return this.peekedElement;
        }
    }

    private Iterators() {
    }

    static <T> UnmodifiableIterator<T> emptyIterator() {
        return emptyListIterator();
    }

    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return EMPTY_LIST_ITERATOR;
    }

    static <T> Iterator<T> emptyModifiableIterator() {
        return EMPTY_MODIFIABLE_ITERATOR;
    }

    public static <T> UnmodifiableIterator<T> unmodifiableIterator(Iterator<? extends T> it) {
        Preconditions.checkNotNull(it);
        if (it instanceof UnmodifiableIterator) {
            return (UnmodifiableIterator) it;
        }
        return new AnonymousClass3(it);
    }

    public static boolean contains(Iterator<?> it, Object obj) {
        return any(it, Predicates.equalTo(obj));
    }

    public static boolean removeAll(Iterator<?> it, Collection<?> collection) {
        return removeIf(it, Predicates.in(collection));
    }

    public static <T> boolean removeIf(Iterator<T> it, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean z = false;
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static boolean elementsEqual(Iterator<?> it, Iterator<?> it2) {
        while (it.hasNext()) {
            if (!it2.hasNext()) {
                return false;
            }
            if (!Objects.equal(it.next(), it2.next())) {
                return false;
            }
        }
        if (it2.hasNext()) {
            return false;
        }
        return true;
    }

    public static String toString(Iterator<?> it) {
        return Collections2.STANDARD_JOINER.appendTo(new StringBuilder().append('['), (Iterator) it).append(']').toString();
    }

    public static <T> T getOnlyElement(Iterator<T> it) {
        T next = it.next();
        if (!it.hasNext()) {
            return next;
        }
        StringBuilder append = new StringBuilder().append("expected one element but was: <").append(next);
        for (int i = 0; i < 4 && it.hasNext(); i++) {
            append.append(", ").append(it.next());
        }
        if (it.hasNext()) {
            append.append(", ...");
        }
        append.append('>');
        throw new IllegalArgumentException(append.toString());
    }

    public static <T> boolean addAll(Collection<T> collection, Iterator<? extends T> it) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(it);
        boolean z = false;
        while (it.hasNext()) {
            z |= collection.add(it.next());
        }
        return z;
    }

    public static <T> UnmodifiableIterator<T> filter(Iterator<T> it, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(it);
        Preconditions.checkNotNull(predicate);
        return new AbstractIterator(it, predicate);
    }

    public static <T> boolean any(Iterator<T> it, Predicate<? super T> predicate) {
        return indexOf(it, predicate) != -1;
    }

    public static <T> T find(Iterator<T> it, Predicate<? super T> predicate) {
        return filter(it, predicate).next();
    }

    public static <T> int indexOf(Iterator<T> it, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        int i = 0;
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <F, T> Iterator<T> transform(Iterator<F> it, Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new AnonymousClass7(it, function);
    }

    public static <T> T getNext(Iterator<? extends T> it, T t) {
        return it.hasNext() ? it.next() : t;
    }

    static <T> T pollNext(Iterator<T> it) {
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        it.remove();
        return next;
    }

    static void clear(Iterator<?> it) {
        Preconditions.checkNotNull(it);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @SafeVarargs
    public static <T> UnmodifiableIterator<T> forArray(T... tArr) {
        return forArray(tArr, 0, tArr.length, 0);
    }

    static <T> UnmodifiableListIterator<T> forArray(T[] tArr, int i, int i2, int i3) {
        Preconditions.checkArgument(i2 >= 0);
        Preconditions.checkPositionIndexes(i, i + i2, tArr.length);
        Preconditions.checkPositionIndex(i3, i2);
        if (i2 == 0) {
            return emptyListIterator();
        }
        return new AnonymousClass10(i2, i3, tArr, i);
    }

    public static <T> UnmodifiableIterator<T> singletonIterator(T t) {
        return new AnonymousClass11(t);
    }

    public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> it) {
        if (it instanceof PeekingImpl) {
            return (PeekingImpl) it;
        }
        return new PeekingImpl(it);
    }
}
