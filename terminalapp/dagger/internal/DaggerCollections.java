package dagger.internal;

import java.util.HashSet;
import java.util.LinkedHashMap;

public final class DaggerCollections {
    private DaggerCollections() {
    }

    static <T> HashSet<T> newHashSetWithExpectedSize(int i) {
        return new HashSet(calculateInitialCapacity(i));
    }

    static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int i) {
        return new LinkedHashMap(calculateInitialCapacity(i));
    }

    private static int calculateInitialCapacity(int i) {
        if (i < 3) {
            return i + 1;
        }
        if (i < 1073741824) {
            return (int) ((((float) i) / 0.75f) + 1.0f);
        }
        return Integer.MAX_VALUE;
    }
}
