package android.support.v4.app;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class FragmentManager {
    public abstract FragmentTransaction beginTransaction();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract boolean executePendingTransactions();

    public abstract Fragment findFragmentByTag(String str);

    public abstract void popBackStack(int i, int i2);

    public abstract boolean popBackStackImmediate();
}
