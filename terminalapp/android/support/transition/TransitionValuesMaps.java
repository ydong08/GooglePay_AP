package android.support.transition;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;

class TransitionValuesMaps {
    SparseArray<TransitionValues> mIdValues = new SparseArray();
    LongSparseArray<TransitionValues> mItemIdValues = new LongSparseArray();
    ArrayMap<View, TransitionValues> mViewValues = new ArrayMap();

    TransitionValuesMaps() {
    }
}
