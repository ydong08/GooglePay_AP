package com.google.android.apps.common.inject;

import android.app.Application;
import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.List;

public abstract class InjectedApplication extends Application {
    private volatile ObjectGraph mApplicationGraph;

    protected abstract List<Object> getModules();

    public void inject(Object obj) {
        this.mApplicationGraph.inject(obj);
    }

    protected synchronized ObjectGraph initGraph() {
        if (this.mApplicationGraph == null) {
            List arrayList = new ArrayList();
            arrayList.add(new ApplicationModule(this));
            arrayList.addAll(getModules());
            this.mApplicationGraph = createGraph(arrayList.toArray());
        }
        return this.mApplicationGraph;
    }

    @Deprecated
    protected ObjectGraph createGraph(Object... objArr) {
        return ObjectGraph.create(objArr);
    }
}
