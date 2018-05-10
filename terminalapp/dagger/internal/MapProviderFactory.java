package dagger.internal;

import dagger.Lazy;
import java.util.Collections;
import java.util.Map;
import javax.inject.Provider;

public final class MapProviderFactory<K, V> implements Lazy<Map<K, Provider<V>>>, Factory<Map<K, Provider<V>>> {
    private static final MapProviderFactory<Object, Object> EMPTY = new MapProviderFactory(Collections.emptyMap());
    private final Map<K, Provider<V>> contributingMap;

    public static final class Builder<K, V> {
    }

    private MapProviderFactory(Map<K, Provider<V>> map) {
        this.contributingMap = Collections.unmodifiableMap(map);
    }

    public Map<K, Provider<V>> get() {
        return this.contributingMap;
    }
}
