package android.support.v7.widget;

import android.support.v4.os.TraceCompat;
import android.support.v7.widget.RecyclerView.LayoutManager.LayoutPrefetchRegistry;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

final class GapWorker implements Runnable {
    static final ThreadLocal<GapWorker> sGapWorker = new ThreadLocal();
    static Comparator<Task> sTaskComparator = new Comparator<Task>() {
        public int compare(Task task, Task task2) {
            int i = -1;
            if ((task.view == null ? 1 : 0) != (task2.view == null ? 1 : 0)) {
                return task.view == null ? 1 : -1;
            } else {
                if (task.immediate != task2.immediate) {
                    if (!task.immediate) {
                        i = 1;
                    }
                    return i;
                }
                int i2 = task2.viewVelocity - task.viewVelocity;
                if (i2 != 0) {
                    return i2;
                }
                i2 = task.distanceToItem - task2.distanceToItem;
                if (i2 == 0) {
                    return 0;
                }
                return i2;
            }
        }
    };
    long mFrameIntervalNs;
    long mPostTimeNs;
    ArrayList<RecyclerView> mRecyclerViews = new ArrayList();
    private ArrayList<Task> mTasks = new ArrayList();

    static class Task {
        public int distanceToItem;
        public boolean immediate;
        public int position;
        public RecyclerView view;
        public int viewVelocity;

        Task() {
        }

        public void clear() {
            this.immediate = false;
            this.viewVelocity = 0;
            this.distanceToItem = 0;
            this.view = null;
            this.position = 0;
        }
    }

    GapWorker() {
    }

    public void add(RecyclerView recyclerView) {
        this.mRecyclerViews.add(recyclerView);
    }

    public void remove(RecyclerView recyclerView) {
        this.mRecyclerViews.remove(recyclerView);
    }

    void postFromTraversal(RecyclerView recyclerView, int i, int i2) {
        if (recyclerView.isAttachedToWindow() && this.mPostTimeNs == 0) {
            this.mPostTimeNs = recyclerView.getNanoTime();
            recyclerView.post(this);
        }
        recyclerView.mPrefetchRegistry$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNKEOBGATNN4QR5E8I4OOBPDTQN8K3ICLJ6AT33D196APR9EDQ74UA9DLO6OEO_0.setPrefetchVector(i, i2);
    }

    private void buildTaskList() {
        int size = this.mRecyclerViews.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            RecyclerView recyclerView = (RecyclerView) this.mRecyclerViews.get(i2);
            recyclerView.mPrefetchRegistry$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNKEOBGATNN4QR5E8I4OOBPDTQN8K3ICLJ6AT33D196APR9EDQ74UA9DLO6OEO_0.collectPrefetchPositionsFromView(recyclerView, false);
            i += recyclerView.mPrefetchRegistry$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNKEOBGATNN4QR5E8I4OOBPDTQN8K3ICLJ6AT33D196APR9EDQ74UA9DLO6OEO_0.mCount;
        }
        this.mTasks.ensureCapacity(i);
        int i3 = 0;
        boolean z = false;
        while (i3 < size) {
            recyclerView = (RecyclerView) this.mRecyclerViews.get(i3);
            LayoutPrefetchRegistry layoutPrefetchRegistry = recyclerView.mPrefetchRegistry$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNKEOBGATNN4QR5E8I4OOBPDTQN8K3ICLJ6AT33D196APR9EDQ74UA9DLO6OEO_0;
            int abs = Math.abs(layoutPrefetchRegistry.mPrefetchDx) + Math.abs(layoutPrefetchRegistry.mPrefetchDy);
            boolean z2 = z;
            for (i = 0; i < layoutPrefetchRegistry.mCount * 2; i += 2) {
                Task task;
                boolean z3;
                if (z2 >= this.mTasks.size()) {
                    task = new Task();
                    this.mTasks.add(task);
                } else {
                    task = (Task) this.mTasks.get(z2);
                }
                int i4 = layoutPrefetchRegistry.mPrefetchArray[i + 1];
                if (i4 <= abs) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                task.immediate = z3;
                task.viewVelocity = abs;
                task.distanceToItem = i4;
                task.view = recyclerView;
                task.position = layoutPrefetchRegistry.mPrefetchArray[i];
                z2++;
            }
            i3++;
            z = z2;
        }
        Collections.sort(this.mTasks, sTaskComparator);
    }

    static boolean isPrefetchPositionAttached(RecyclerView recyclerView, int i) {
        int unfilteredChildCount = recyclerView.mChildHelper.getUnfilteredChildCount();
        for (int i2 = 0; i2 < unfilteredChildCount; i2++) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.getUnfilteredChildAt(i2));
            if (childViewHolderInt.mPosition == i && !childViewHolderInt.isInvalid()) {
                return true;
            }
        }
        return false;
    }

    private ViewHolder prefetchPositionWithDeadline(RecyclerView recyclerView, int i, long j) {
        if (isPrefetchPositionAttached(recyclerView, i)) {
            return null;
        }
        Recycler recycler = recyclerView.mRecycler;
        ViewHolder tryGetViewHolderForPositionByDeadline = recycler.tryGetViewHolderForPositionByDeadline(i, false, j);
        if (tryGetViewHolderForPositionByDeadline == null) {
            return tryGetViewHolderForPositionByDeadline;
        }
        if (tryGetViewHolderForPositionByDeadline.isBound()) {
            recycler.recycleView(tryGetViewHolderForPositionByDeadline.itemView);
            return tryGetViewHolderForPositionByDeadline;
        }
        recycler.addViewHolderToRecycledViewPool(tryGetViewHolderForPositionByDeadline, false);
        return tryGetViewHolderForPositionByDeadline;
    }

    private void prefetchInnerRecyclerViewWithDeadline(RecyclerView recyclerView, long j) {
        if (recyclerView != null) {
            if (recyclerView.mDataSetHasChangedAfterLayout && recyclerView.mChildHelper.getUnfilteredChildCount() != 0) {
                recyclerView.removeAndRecycleViews();
            }
            LayoutPrefetchRegistry layoutPrefetchRegistry = recyclerView.mPrefetchRegistry$9HGMSP3IDTKM8BRJELO70RRIEGNNCDPFETKM8PR5EGNKEOBGATNN4QR5E8I4OOBPDTQN8K3ICLJ6AT33D196APR9EDQ74UA9DLO6OEO_0;
            layoutPrefetchRegistry.collectPrefetchPositionsFromView(recyclerView, true);
            if (layoutPrefetchRegistry.mCount != 0) {
                try {
                    TraceCompat.beginSection("RV Nested Prefetch");
                    recyclerView.mState.prepareForNestedPrefetch(recyclerView.mAdapter);
                    for (int i = 0; i < layoutPrefetchRegistry.mCount * 2; i += 2) {
                        prefetchPositionWithDeadline(recyclerView, layoutPrefetchRegistry.mPrefetchArray[i], j);
                    }
                } finally {
                    TraceCompat.endSection();
                }
            }
        }
    }

    private void flushTaskWithDeadline(Task task, long j) {
        ViewHolder prefetchPositionWithDeadline = prefetchPositionWithDeadline(task.view, task.position, task.immediate ? Long.MAX_VALUE : j);
        if (prefetchPositionWithDeadline != null && prefetchPositionWithDeadline.mNestedRecyclerView != null) {
            prefetchInnerRecyclerViewWithDeadline((RecyclerView) prefetchPositionWithDeadline.mNestedRecyclerView.get(), j);
        }
    }

    private void flushTasksWithDeadline(long j) {
        int i = 0;
        while (i < this.mTasks.size()) {
            Task task = (Task) this.mTasks.get(i);
            if (task.view != null) {
                flushTaskWithDeadline(task, j);
                task.clear();
                i++;
            } else {
                return;
            }
        }
    }

    void prefetch(long j) {
        buildTaskList();
        flushTasksWithDeadline(j);
    }

    public void run() {
        try {
            TraceCompat.beginSection("RV Prefetch");
            if (!this.mRecyclerViews.isEmpty()) {
                long toNanos = TimeUnit.MILLISECONDS.toNanos(((RecyclerView) this.mRecyclerViews.get(0)).getDrawingTime());
                if (toNanos == 0) {
                    this.mPostTimeNs = 0;
                    TraceCompat.endSection();
                    return;
                }
                prefetch(toNanos + this.mFrameIntervalNs);
                this.mPostTimeNs = 0;
                TraceCompat.endSection();
            }
        } finally {
            this.mPostTimeNs = 0;
            TraceCompat.endSection();
        }
    }
}
