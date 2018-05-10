package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.content.Loader;

public abstract class LoaderManager {

    public interface LoaderCallbacks<D> {
        Loader<D> onCreateLoader(int i, Bundle bundle);
    }

    public boolean hasRunningLoaders() {
        return false;
    }
}
