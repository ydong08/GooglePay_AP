package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.app.Fragment.SavedState;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class FragmentManager {
    public abstract FragmentTransaction beginTransaction();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract boolean executePendingTransactions();

    public abstract Fragment findFragmentByTag(String str);

    public abstract Fragment getFragment(Bundle bundle, String str);

    public abstract void popBackStack(int i, int i2);

    public abstract boolean popBackStackImmediate();

    public abstract void putFragment(Bundle bundle, String str, Fragment fragment);

    public abstract SavedState saveFragmentInstanceState(Fragment fragment);
}
