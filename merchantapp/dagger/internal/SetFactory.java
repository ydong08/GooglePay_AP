package dagger.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;

public final class SetFactory<T> implements Factory<Set<T>> {
    private static final Factory<Set<Object>> EMPTY_FACTORY = InstanceFactory.create(Collections.emptySet());
    private final List<Provider<Collection<T>>> collectionProviders;
    private final List<Provider<T>> individualProviders;

    public static final class Builder<T> {
    }

    public Set<T> get() {
        int i;
        int i2 = 0;
        int size = this.individualProviders.size();
        List arrayList = new ArrayList(this.collectionProviders.size());
        int size2 = this.collectionProviders.size();
        int i3 = size;
        for (i = 0; i < size2; i++) {
            Collection collection = (Collection) ((Provider) this.collectionProviders.get(i)).get();
            i3 += collection.size();
            arrayList.add(collection);
        }
        Set newHashSetWithExpectedSize = DaggerCollections.newHashSetWithExpectedSize(i3);
        size2 = this.individualProviders.size();
        for (i = 0; i < size2; i++) {
            newHashSetWithExpectedSize.add(Preconditions.checkNotNull(((Provider) this.individualProviders.get(i)).get()));
        }
        i = arrayList.size();
        while (i2 < i) {
            for (Object checkNotNull : (Collection) arrayList.get(i2)) {
                newHashSetWithExpectedSize.add(Preconditions.checkNotNull(checkNotNull));
            }
            i2++;
        }
        return Collections.unmodifiableSet(newHashSetWithExpectedSize);
    }
}
