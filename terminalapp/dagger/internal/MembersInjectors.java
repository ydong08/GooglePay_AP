package dagger.internal;

import dagger.MembersInjector;

public final class MembersInjectors {

    enum NoOpMembersInjector implements MembersInjector<Object> {
        INSTANCE;

        public void injectMembers(Object obj) {
            Preconditions.checkNotNull(obj);
        }
    }

    private MembersInjectors() {
    }
}
