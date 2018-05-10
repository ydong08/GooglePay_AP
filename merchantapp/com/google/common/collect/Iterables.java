package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Iterator;

public final class Iterables {

    class AnonymousClass4 extends FluentIterable<T> {
        final /* synthetic */ Predicate val$retainIfTrue;
        final /* synthetic */ Iterable val$unfiltered;

        AnonymousClass4(Iterable iterable, Predicate predicate) {
            this.val$unfiltered = iterable;
            this.val$retainIfTrue = predicate;
        }

        public Iterator<T> iterator() {
            return Iterators.filter(this.val$unfiltered.iterator(), this.val$retainIfTrue);
        }
    }

    class AnonymousClass6 extends FluentIterable<T> {
        final /* synthetic */ Iterable val$fromIterable;
        final /* synthetic */ Function val$function;

        AnonymousClass6(Iterable iterable, Function function) {
            this.val$fromIterable = iterable;
            this.val$function = function;
        }

        public Iterator<T> iterator() {
            return Iterators.transform(this.val$fromIterable.iterator(), this.val$function);
        }
    }

    private Iterables() {
    }

    public static String toString(Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }

    public static <T> T getOnlyElement(Iterable<T> iterable) {
        return Iterators.getOnlyElement(iterable.iterator());
    }

    static <T> T[] toArray(Iterable<? extends T> iterable, T[] tArr) {
        return castOrCopyToCollection(iterable).toArray(tArr);
    }

    static Object[] toArray(Iterable<?> iterable) {
        return castOrCopyToCollection(iterable).toArray();
    }

    private static <E> Collection<E> castOrCopyToCollection(Iterable<E> iterable) {
        if (iterable instanceof Collection) {
            return (Collection) iterable;
        }
        return Lists.newArrayList(iterable.iterator());
    }

    public static <T> Iterable<T> filter(Iterable<T> iterable, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(predicate);
        return new AnonymousClass4(iterable, predicate);
    }

    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.find(iterable.iterator(), predicate);
    }

    public static <F, T> Iterable<T> transform(Iterable<F> iterable, Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(function);
        return new AnonymousClass6(iterable, function);
    }

    public static <T> T getFirst(Iterable<? extends T> iterable, T t) {
        return Iterators.getNext(iterable.iterator(), t);
    }
}
