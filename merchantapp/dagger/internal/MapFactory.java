package dagger.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Provider;

public final class MapFactory<K, V> implements Factory<Map<K, V>> {
    private final Map<K, Provider<V>> contributingMap;

    public Map<K, V> get() {
        Map newLinkedHashMapWithExpectedSize = DaggerCollections.newLinkedHashMapWithExpectedSize(this.contributingMap.size());
        for (Entry entry : this.contributingMap.entrySet()) {
            newLinkedHashMapWithExpectedSize.put(entry.getKey(), ((Provider) entry.getValue()).get());
        }
        return Collections.unmodifiableMap(newLinkedHashMapWithExpectedSize);
    }
}
