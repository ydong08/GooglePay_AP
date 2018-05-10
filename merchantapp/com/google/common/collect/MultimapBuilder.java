package com.google.common.collect;

import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class MultimapBuilder<K0, V0> {

    public static abstract class MultimapBuilderWithKeys<K0> {
        abstract <K extends K0, V> Map<K, Collection<V>> createMap();

        MultimapBuilderWithKeys() {
        }

        public ListMultimapBuilder<K0, Object> arrayListValues() {
            return arrayListValues(2);
        }

        public ListMultimapBuilder<K0, Object> arrayListValues(int i) {
            CollectPreconditions.checkNonnegative(i, "expectedValuesPerKey");
            return new ListMultimapBuilder(this, i);
        }
    }

    class AnonymousClass1 extends MultimapBuilderWithKeys<Object> {
        final /* synthetic */ int val$expectedKeys;

        <K, V> Map<K, Collection<V>> createMap() {
            return Maps.newHashMapWithExpectedSize(this.val$expectedKeys);
        }
    }

    class AnonymousClass2 extends MultimapBuilderWithKeys<Object> {
        final /* synthetic */ int val$expectedKeys;

        AnonymousClass2(int i) {
            this.val$expectedKeys = i;
        }

        <K, V> Map<K, Collection<V>> createMap() {
            return Maps.newLinkedHashMapWithExpectedSize(this.val$expectedKeys);
        }
    }

    static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable {
        private final int expectedValuesPerKey;

        ArrayListSupplier(int i) {
            this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(i, "expectedValuesPerKey");
        }

        public List<V> get() {
            return new ArrayList(this.expectedValuesPerKey);
        }
    }

    public static abstract class ListMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0> {
        final /* synthetic */ MultimapBuilderWithKeys this$0;
        final /* synthetic */ int val$expectedValuesPerKey;

        ListMultimapBuilder() {
            super();
        }

        ListMultimapBuilder(MultimapBuilderWithKeys multimapBuilderWithKeys, int i) {
            this.this$0 = multimapBuilderWithKeys;
            this.val$expectedValuesPerKey = i;
            this();
        }

        public <K extends K0, V> ListMultimap<K, V> build() {
            return Multimaps.newListMultimap(this.this$0.createMap(), new ArrayListSupplier(this.val$expectedValuesPerKey));
        }
    }

    private MultimapBuilder() {
    }

    public static MultimapBuilderWithKeys<Object> linkedHashKeys() {
        return linkedHashKeys(8);
    }

    public static MultimapBuilderWithKeys<Object> linkedHashKeys(int i) {
        CollectPreconditions.checkNonnegative(i, "expectedKeys");
        return new AnonymousClass2(i);
    }
}
