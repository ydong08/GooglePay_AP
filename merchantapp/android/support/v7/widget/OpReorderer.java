package android.support.v7.widget;

import android.support.v4.util.Pools.Pool;
import java.util.ArrayList;
import java.util.List;

class OpReorderer {
    final Callback mCallback;

    interface Callback {
        final AdapterHelper$Callback mCallback;
        final boolean mDisableRecycler;
        private int mExistingUpdateTypes;
        Runnable mOnItemProcessedCallback;
        final OpReorderer mOpReorderer;
        final ArrayList<AdapterHelper$UpdateOp> mPendingUpdates;
        final ArrayList<AdapterHelper$UpdateOp> mPostponedList;
        private Pool<AdapterHelper$UpdateOp> mUpdateOpPool;

        Callback(AdapterHelper$Callback adapterHelper$Callback) {
            this(adapterHelper$Callback, false);
        }

        void reset() {
            recycleUpdateOpsAndClearList(this.mPendingUpdates);
            recycleUpdateOpsAndClearList(this.mPostponedList);
            this.mExistingUpdateTypes = 0;
        }

        void preProcess() {
            this.mOpReorderer.reorderOps(this.mPendingUpdates);
            int size = this.mPendingUpdates.size();
            for (int i = 0; i < size; i++) {
                AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mPendingUpdates.get(i);
                switch (adapterHelper$UpdateOp.cmd) {
                    case 1:
                        applyAdd(adapterHelper$UpdateOp);
                        break;
                    case 2:
                        applyRemove(adapterHelper$UpdateOp);
                        break;
                    case 4:
                        applyUpdate(adapterHelper$UpdateOp);
                        break;
                    case 8:
                        applyMove(adapterHelper$UpdateOp);
                        break;
                }
                if (this.mOnItemProcessedCallback != null) {
                    this.mOnItemProcessedCallback.run();
                }
            }
            this.mPendingUpdates.clear();
        }

        void consumePostponedUpdates() {
            int size = this.mPostponedList.size();
            for (int i = 0; i < size; i++) {
                this.mCallback.onDispatchSecondPass((AdapterHelper$UpdateOp) this.mPostponedList.get(i));
            }
            recycleUpdateOpsAndClearList(this.mPostponedList);
            this.mExistingUpdateTypes = 0;
        }

        private void applyMove(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
            postponeAndUpdateViewHolders(adapterHelper$UpdateOp);
        }

        private void applyRemove(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
            int i = adapterHelper$UpdateOp.positionStart;
            int i2 = adapterHelper$UpdateOp.positionStart + adapterHelper$UpdateOp.itemCount;
            Object obj = -1;
            int i3 = adapterHelper$UpdateOp.positionStart;
            int i4 = 0;
            while (i3 < i2) {
                Object obj2;
                int i5;
                if (this.mCallback.findViewHolder(i3) != null || canFindInPreLayout(i3)) {
                    if (obj == null) {
                        dispatchAndUpdateViewHolders(obtainUpdateOp(2, i, i4, null));
                        obj2 = 1;
                    } else {
                        obj2 = null;
                    }
                    obj = 1;
                } else {
                    if (obj == 1) {
                        postponeAndUpdateViewHolders(obtainUpdateOp(2, i, i4, null));
                        obj2 = 1;
                    } else {
                        obj2 = null;
                    }
                    obj = null;
                }
                if (obj2 != null) {
                    i5 = i3 - i4;
                    i3 = i2 - i4;
                    i2 = 1;
                } else {
                    int i6 = i3;
                    i3 = i2;
                    i2 = i4 + 1;
                    i5 = i6;
                }
                i4 = i2;
                i2 = i3;
                i3 = i5 + 1;
            }
            if (i4 != adapterHelper$UpdateOp.itemCount) {
                recycleUpdateOp(adapterHelper$UpdateOp);
                adapterHelper$UpdateOp = obtainUpdateOp(2, i, i4, null);
            }
            if (obj == null) {
                dispatchAndUpdateViewHolders(adapterHelper$UpdateOp);
            } else {
                postponeAndUpdateViewHolders(adapterHelper$UpdateOp);
            }
        }

        private void applyUpdate(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
            int i = adapterHelper$UpdateOp.positionStart;
            int i2 = adapterHelper$UpdateOp.positionStart + adapterHelper$UpdateOp.itemCount;
            int i3 = adapterHelper$UpdateOp.positionStart;
            Object obj = -1;
            int i4 = 0;
            while (i3 < i2) {
                int i5;
                Object obj2;
                if (this.mCallback.findViewHolder(i3) != null || canFindInPreLayout(i3)) {
                    if (obj == null) {
                        dispatchAndUpdateViewHolders(obtainUpdateOp(4, i, i4, adapterHelper$UpdateOp.payload));
                        i4 = 0;
                        i = i3;
                    }
                    i5 = i;
                    i = i4;
                    obj2 = 1;
                } else {
                    if (obj == 1) {
                        postponeAndUpdateViewHolders(obtainUpdateOp(4, i, i4, adapterHelper$UpdateOp.payload));
                        i4 = 0;
                        i = i3;
                    }
                    i5 = i;
                    i = i4;
                    obj2 = null;
                }
                i3++;
                Object obj3 = obj2;
                i4 = i + 1;
                i = i5;
                obj = obj3;
            }
            if (i4 != adapterHelper$UpdateOp.itemCount) {
                Object obj4 = adapterHelper$UpdateOp.payload;
                recycleUpdateOp(adapterHelper$UpdateOp);
                adapterHelper$UpdateOp = obtainUpdateOp(4, i, i4, obj4);
            }
            if (obj == null) {
                dispatchAndUpdateViewHolders(adapterHelper$UpdateOp);
            } else {
                postponeAndUpdateViewHolders(adapterHelper$UpdateOp);
            }
        }

        private void dispatchAndUpdateViewHolders(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
            if (adapterHelper$UpdateOp.cmd == 1 || adapterHelper$UpdateOp.cmd == 8) {
                throw new IllegalArgumentException("should not dispatch add or move for pre layout");
            }
            int i;
            int updatePositionWithPostponed = updatePositionWithPostponed(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.cmd);
            int i2 = adapterHelper$UpdateOp.positionStart;
            switch (adapterHelper$UpdateOp.cmd) {
                case 2:
                    i = 0;
                    break;
                case 4:
                    i = 1;
                    break;
                default:
                    throw new IllegalArgumentException("op should be remove or update." + adapterHelper$UpdateOp);
            }
            int i3 = 1;
            int i4 = updatePositionWithPostponed;
            updatePositionWithPostponed = i2;
            for (i2 = 1; i2 < adapterHelper$UpdateOp.itemCount; i2++) {
                Object obj;
                int updatePositionWithPostponed2 = updatePositionWithPostponed(adapterHelper$UpdateOp.positionStart + (i * i2), adapterHelper$UpdateOp.cmd);
                int i5;
                switch (adapterHelper$UpdateOp.cmd) {
                    case 2:
                        if (updatePositionWithPostponed2 != i4) {
                            obj = null;
                            break;
                        } else {
                            i5 = 1;
                            break;
                        }
                    case 4:
                        if (updatePositionWithPostponed2 != i4 + 1) {
                            obj = null;
                            break;
                        } else {
                            i5 = 1;
                            break;
                        }
                    default:
                        obj = null;
                        break;
                }
                if (obj != null) {
                    i3++;
                } else {
                    AdapterHelper$UpdateOp obtainUpdateOp = obtainUpdateOp(adapterHelper$UpdateOp.cmd, i4, i3, adapterHelper$UpdateOp.payload);
                    dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp, updatePositionWithPostponed);
                    recycleUpdateOp(obtainUpdateOp);
                    if (adapterHelper$UpdateOp.cmd == 4) {
                        updatePositionWithPostponed += i3;
                    }
                    i3 = 1;
                    i4 = updatePositionWithPostponed2;
                }
            }
            Object obj2 = adapterHelper$UpdateOp.payload;
            recycleUpdateOp(adapterHelper$UpdateOp);
            if (i3 > 0) {
                AdapterHelper$UpdateOp obtainUpdateOp2 = obtainUpdateOp(adapterHelper$UpdateOp.cmd, i4, i3, obj2);
                dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp2, updatePositionWithPostponed);
                recycleUpdateOp(obtainUpdateOp2);
            }
        }

        void dispatchFirstPassAndUpdateViewHolders(AdapterHelper$UpdateOp adapterHelper$UpdateOp, int i) {
            this.mCallback.onDispatchFirstPass(adapterHelper$UpdateOp);
            switch (adapterHelper$UpdateOp.cmd) {
                case 2:
                    this.mCallback.offsetPositionsForRemovingInvisible(i, adapterHelper$UpdateOp.itemCount);
                    return;
                case 4:
                    this.mCallback.markViewHoldersUpdated(i, adapterHelper$UpdateOp.itemCount, adapterHelper$UpdateOp.payload);
                    return;
                default:
                    throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
            }
        }

        private int updatePositionWithPostponed(int i, int i2) {
            int i3;
            int i4 = i;
            for (int size = this.mPostponedList.size() - 1; size >= 0; size--) {
                AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mPostponedList.get(size);
                if (adapterHelper$UpdateOp.cmd == 8) {
                    int i5;
                    int i6;
                    if (adapterHelper$UpdateOp.positionStart < adapterHelper$UpdateOp.itemCount) {
                        i5 = adapterHelper$UpdateOp.positionStart;
                        i3 = adapterHelper$UpdateOp.itemCount;
                    } else {
                        i5 = adapterHelper$UpdateOp.itemCount;
                        i3 = adapterHelper$UpdateOp.positionStart;
                    }
                    if (i4 < i5 || i4 > r2) {
                        if (i4 < adapterHelper$UpdateOp.positionStart) {
                            if (i2 == 1) {
                                adapterHelper$UpdateOp.positionStart++;
                                adapterHelper$UpdateOp.itemCount++;
                                i6 = i4;
                            } else if (i2 == 2) {
                                adapterHelper$UpdateOp.positionStart--;
                                adapterHelper$UpdateOp.itemCount--;
                            }
                        }
                        i6 = i4;
                    } else if (i5 == adapterHelper$UpdateOp.positionStart) {
                        if (i2 == 1) {
                            adapterHelper$UpdateOp.itemCount++;
                        } else if (i2 == 2) {
                            adapterHelper$UpdateOp.itemCount--;
                        }
                        i6 = i4 + 1;
                    } else {
                        if (i2 == 1) {
                            adapterHelper$UpdateOp.positionStart++;
                        } else if (i2 == 2) {
                            adapterHelper$UpdateOp.positionStart--;
                        }
                        i6 = i4 - 1;
                    }
                    i4 = i6;
                } else if (adapterHelper$UpdateOp.positionStart <= i4) {
                    if (adapterHelper$UpdateOp.cmd == 1) {
                        i4 -= adapterHelper$UpdateOp.itemCount;
                    } else if (adapterHelper$UpdateOp.cmd == 2) {
                        i4 += adapterHelper$UpdateOp.itemCount;
                    }
                } else if (i2 == 1) {
                    adapterHelper$UpdateOp.positionStart++;
                } else if (i2 == 2) {
                    adapterHelper$UpdateOp.positionStart--;
                }
            }
            for (i3 = this.mPostponedList.size() - 1; i3 >= 0; i3--) {
                adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mPostponedList.get(i3);
                if (adapterHelper$UpdateOp.cmd == 8) {
                    if (adapterHelper$UpdateOp.itemCount == adapterHelper$UpdateOp.positionStart || adapterHelper$UpdateOp.itemCount < 0) {
                        this.mPostponedList.remove(i3);
                        recycleUpdateOp(adapterHelper$UpdateOp);
                    }
                } else if (adapterHelper$UpdateOp.itemCount <= 0) {
                    this.mPostponedList.remove(i3);
                    recycleUpdateOp(adapterHelper$UpdateOp);
                }
            }
            return i4;
        }

        private boolean canFindInPreLayout(int i) {
            int size = this.mPostponedList.size();
            for (int i2 = 0; i2 < size; i2++) {
                AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mPostponedList.get(i2);
                if (adapterHelper$UpdateOp.cmd == 8) {
                    if (findPositionOffset(adapterHelper$UpdateOp.itemCount, i2 + 1) == i) {
                        return true;
                    }
                } else if (adapterHelper$UpdateOp.cmd == 1) {
                    int i3 = adapterHelper$UpdateOp.positionStart + adapterHelper$UpdateOp.itemCount;
                    for (int i4 = adapterHelper$UpdateOp.positionStart; i4 < i3; i4++) {
                        if (findPositionOffset(i4, i2 + 1) == i) {
                            return true;
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            }
            return false;
        }

        private void applyAdd(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
            postponeAndUpdateViewHolders(adapterHelper$UpdateOp);
        }

        private void postponeAndUpdateViewHolders(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
            this.mPostponedList.add(adapterHelper$UpdateOp);
            switch (adapterHelper$UpdateOp.cmd) {
                case 1:
                    this.mCallback.offsetPositionsForAdd(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                    return;
                case 2:
                    this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                    return;
                case 4:
                    this.mCallback.markViewHoldersUpdated(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount, adapterHelper$UpdateOp.payload);
                    return;
                case 8:
                    this.mCallback.offsetPositionsForMove(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown update op type for " + adapterHelper$UpdateOp);
            }
        }

        boolean hasPendingUpdates() {
            return this.mPendingUpdates.size() > 0;
        }

        boolean hasAnyUpdateTypes(int i) {
            return (this.mExistingUpdateTypes & i) != 0;
        }

        int findPositionOffset(int i) {
            return findPositionOffset(i, 0);
        }

        void consumeUpdatesInOnePass() {
            consumePostponedUpdates();
            int size = this.mPendingUpdates.size();
            for (int i = 0; i < size; i++) {
                AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mPendingUpdates.get(i);
                switch (adapterHelper$UpdateOp.cmd) {
                    case 1:
                        this.mCallback.onDispatchSecondPass(adapterHelper$UpdateOp);
                        this.mCallback.offsetPositionsForAdd(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                        break;
                    case 2:
                        this.mCallback.onDispatchSecondPass(adapterHelper$UpdateOp);
                        this.mCallback.offsetPositionsForRemovingInvisible(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                        break;
                    case 4:
                        this.mCallback.onDispatchSecondPass(adapterHelper$UpdateOp);
                        this.mCallback.markViewHoldersUpdated(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount, adapterHelper$UpdateOp.payload);
                        break;
                    case 8:
                        this.mCallback.onDispatchSecondPass(adapterHelper$UpdateOp);
                        this.mCallback.offsetPositionsForMove(adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                        break;
                }
                if (this.mOnItemProcessedCallback != null) {
                    this.mOnItemProcessedCallback.run();
                }
            }
            recycleUpdateOpsAndClearList(this.mPendingUpdates);
            this.mExistingUpdateTypes = 0;
        }

        int applyPendingUpdatesToPosition(int i) {
            int size = this.mPendingUpdates.size();
            int i2 = i;
            for (int i3 = 0; i3 < size; i3++) {
                AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mPendingUpdates.get(i3);
                switch (adapterHelper$UpdateOp.cmd) {
                    case 1:
                        if (adapterHelper$UpdateOp.positionStart > i2) {
                            break;
                        }
                        i2 += adapterHelper$UpdateOp.itemCount;
                        break;
                    case 2:
                        if (adapterHelper$UpdateOp.positionStart <= i2) {
                            if (adapterHelper$UpdateOp.positionStart + adapterHelper$UpdateOp.itemCount <= i2) {
                                i2 -= adapterHelper$UpdateOp.itemCount;
                                break;
                            }
                            return -1;
                        }
                        continue;
                    case 8:
                        if (adapterHelper$UpdateOp.positionStart != i2) {
                            if (adapterHelper$UpdateOp.positionStart < i2) {
                                i2--;
                            }
                            if (adapterHelper$UpdateOp.itemCount > i2) {
                                break;
                            }
                            i2++;
                            break;
                        }
                        i2 = adapterHelper$UpdateOp.itemCount;
                        break;
                    default:
                        break;
                }
            }
            return i2;
        }

        boolean hasUpdates() {
            return (this.mPostponedList.isEmpty() || this.mPendingUpdates.isEmpty()) ? false : true;
        }

        AdapterHelper$UpdateOp obtainUpdateOp(int i, int i2, int i3, Object obj) {
            AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mUpdateOpPool.acquire();
            if (adapterHelper$UpdateOp == null) {
                return new AdapterHelper$UpdateOp(i, i2, i3, obj);
            }
            adapterHelper$UpdateOp.cmd = i;
            adapterHelper$UpdateOp.positionStart = i2;
            adapterHelper$UpdateOp.itemCount = i3;
            adapterHelper$UpdateOp.payload = obj;
            return adapterHelper$UpdateOp;
        }

        void recycleUpdateOp(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
            if (!this.mDisableRecycler) {
                adapterHelper$UpdateOp.payload = null;
                this.mUpdateOpPool.release(adapterHelper$UpdateOp);
            }
        }

        void recycleUpdateOpsAndClearList(List<AdapterHelper$UpdateOp> list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                recycleUpdateOp((AdapterHelper$UpdateOp) list.get(i));
            }
            list.clear();
        }

        Callback(AdapterHelper$Callback adapterHelper$Callback, boolean z) {
            this.mUpdateOpPool = new Pool(30);
            this.mPendingUpdates = new ArrayList();
            this.mPostponedList = new ArrayList();
            this.mExistingUpdateTypes = 0;
            this.mCallback = adapterHelper$Callback;
            this.mDisableRecycler = z;
            this.mOpReorderer = new OpReorderer(this);
        }

        int findPositionOffset(int i, int i2) {
            int size = this.mPostponedList.size();
            int i3 = i;
            while (i2 < size) {
                AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) this.mPostponedList.get(i2);
                if (adapterHelper$UpdateOp.cmd == 8) {
                    if (adapterHelper$UpdateOp.positionStart == i3) {
                        i3 = adapterHelper$UpdateOp.itemCount;
                    } else {
                        if (adapterHelper$UpdateOp.positionStart < i3) {
                            i3--;
                        }
                        if (adapterHelper$UpdateOp.itemCount <= i3) {
                            i3++;
                        }
                    }
                } else if (adapterHelper$UpdateOp.positionStart > i3) {
                    continue;
                } else if (adapterHelper$UpdateOp.cmd == 2) {
                    if (i3 < adapterHelper$UpdateOp.positionStart + adapterHelper$UpdateOp.itemCount) {
                        return -1;
                    }
                    i3 -= adapterHelper$UpdateOp.itemCount;
                } else if (adapterHelper$UpdateOp.cmd == 1) {
                    i3 += adapterHelper$UpdateOp.itemCount;
                }
                i2++;
            }
            return i3;
        }
    }

    public OpReorderer(Callback callback) {
        this.mCallback = callback;
    }

    void reorderOps(List<AdapterHelper$UpdateOp> list) {
        while (true) {
            int lastMoveOutOfOrder = getLastMoveOutOfOrder(list);
            if (lastMoveOutOfOrder != -1) {
                swapMoveOp(list, lastMoveOutOfOrder, lastMoveOutOfOrder + 1);
            } else {
                return;
            }
        }
    }

    private void swapMoveOp(List<AdapterHelper$UpdateOp> list, int i, int i2) {
        AdapterHelper$UpdateOp adapterHelper$UpdateOp = (AdapterHelper$UpdateOp) list.get(i);
        AdapterHelper$UpdateOp adapterHelper$UpdateOp2 = (AdapterHelper$UpdateOp) list.get(i2);
        switch (adapterHelper$UpdateOp2.cmd) {
            case 1:
                swapMoveAdd(list, i, adapterHelper$UpdateOp, i2, adapterHelper$UpdateOp2);
                return;
            case 2:
                swapMoveRemove(list, i, adapterHelper$UpdateOp, i2, adapterHelper$UpdateOp2);
                return;
            case 4:
                swapMoveUpdate(list, i, adapterHelper$UpdateOp, i2, adapterHelper$UpdateOp2);
                return;
            default:
                return;
        }
    }

    void swapMoveRemove(List<AdapterHelper$UpdateOp> list, int i, AdapterHelper$UpdateOp adapterHelper$UpdateOp, int i2, AdapterHelper$UpdateOp adapterHelper$UpdateOp2) {
        int i3;
        AdapterHelper$UpdateOp adapterHelper$UpdateOp3;
        int i4 = 0;
        if (adapterHelper$UpdateOp.positionStart < adapterHelper$UpdateOp.itemCount) {
            i3 = (adapterHelper$UpdateOp2.positionStart == adapterHelper$UpdateOp.positionStart && adapterHelper$UpdateOp2.itemCount == adapterHelper$UpdateOp.itemCount - adapterHelper$UpdateOp.positionStart) ? 1 : 0;
        } else if (adapterHelper$UpdateOp2.positionStart == adapterHelper$UpdateOp.itemCount + 1 && adapterHelper$UpdateOp2.itemCount == adapterHelper$UpdateOp.positionStart - adapterHelper$UpdateOp.itemCount) {
            i4 = 1;
            i3 = 1;
        } else {
            i3 = 0;
            i4 = 1;
        }
        if (adapterHelper$UpdateOp.itemCount < adapterHelper$UpdateOp2.positionStart) {
            adapterHelper$UpdateOp2.positionStart--;
        } else if (adapterHelper$UpdateOp.itemCount < adapterHelper$UpdateOp2.positionStart + adapterHelper$UpdateOp2.itemCount) {
            adapterHelper$UpdateOp2.itemCount--;
            adapterHelper$UpdateOp.cmd = 2;
            adapterHelper$UpdateOp.itemCount = 1;
            if (adapterHelper$UpdateOp2.itemCount == 0) {
                list.remove(i2);
                this.mCallback.recycleUpdateOp(adapterHelper$UpdateOp2);
                return;
            }
            return;
        }
        if (adapterHelper$UpdateOp.positionStart <= adapterHelper$UpdateOp2.positionStart) {
            adapterHelper$UpdateOp2.positionStart++;
            adapterHelper$UpdateOp3 = null;
        } else if (adapterHelper$UpdateOp.positionStart < adapterHelper$UpdateOp2.positionStart + adapterHelper$UpdateOp2.itemCount) {
            adapterHelper$UpdateOp3 = this.mCallback.obtainUpdateOp(2, adapterHelper$UpdateOp.positionStart + 1, (adapterHelper$UpdateOp2.positionStart + adapterHelper$UpdateOp2.itemCount) - adapterHelper$UpdateOp.positionStart, null);
            adapterHelper$UpdateOp2.itemCount = adapterHelper$UpdateOp.positionStart - adapterHelper$UpdateOp2.positionStart;
        } else {
            adapterHelper$UpdateOp3 = null;
        }
        if (i3 != 0) {
            list.set(i, adapterHelper$UpdateOp2);
            list.remove(i2);
            this.mCallback.recycleUpdateOp(adapterHelper$UpdateOp);
            return;
        }
        if (i4 != 0) {
            if (adapterHelper$UpdateOp3 != null) {
                if (adapterHelper$UpdateOp.positionStart > adapterHelper$UpdateOp3.positionStart) {
                    adapterHelper$UpdateOp.positionStart -= adapterHelper$UpdateOp3.itemCount;
                }
                if (adapterHelper$UpdateOp.itemCount > adapterHelper$UpdateOp3.positionStart) {
                    adapterHelper$UpdateOp.itemCount -= adapterHelper$UpdateOp3.itemCount;
                }
            }
            if (adapterHelper$UpdateOp.positionStart > adapterHelper$UpdateOp2.positionStart) {
                adapterHelper$UpdateOp.positionStart -= adapterHelper$UpdateOp2.itemCount;
            }
            if (adapterHelper$UpdateOp.itemCount > adapterHelper$UpdateOp2.positionStart) {
                adapterHelper$UpdateOp.itemCount -= adapterHelper$UpdateOp2.itemCount;
            }
        } else {
            if (adapterHelper$UpdateOp3 != null) {
                if (adapterHelper$UpdateOp.positionStart >= adapterHelper$UpdateOp3.positionStart) {
                    adapterHelper$UpdateOp.positionStart -= adapterHelper$UpdateOp3.itemCount;
                }
                if (adapterHelper$UpdateOp.itemCount >= adapterHelper$UpdateOp3.positionStart) {
                    adapterHelper$UpdateOp.itemCount -= adapterHelper$UpdateOp3.itemCount;
                }
            }
            if (adapterHelper$UpdateOp.positionStart >= adapterHelper$UpdateOp2.positionStart) {
                adapterHelper$UpdateOp.positionStart -= adapterHelper$UpdateOp2.itemCount;
            }
            if (adapterHelper$UpdateOp.itemCount >= adapterHelper$UpdateOp2.positionStart) {
                adapterHelper$UpdateOp.itemCount -= adapterHelper$UpdateOp2.itemCount;
            }
        }
        list.set(i, adapterHelper$UpdateOp2);
        if (adapterHelper$UpdateOp.positionStart != adapterHelper$UpdateOp.itemCount) {
            list.set(i2, adapterHelper$UpdateOp);
        } else {
            list.remove(i2);
        }
        if (adapterHelper$UpdateOp3 != null) {
            list.add(i, adapterHelper$UpdateOp3);
        }
    }

    private void swapMoveAdd(List<AdapterHelper$UpdateOp> list, int i, AdapterHelper$UpdateOp adapterHelper$UpdateOp, int i2, AdapterHelper$UpdateOp adapterHelper$UpdateOp2) {
        int i3 = 0;
        if (adapterHelper$UpdateOp.itemCount < adapterHelper$UpdateOp2.positionStart) {
            i3 = -1;
        }
        if (adapterHelper$UpdateOp.positionStart < adapterHelper$UpdateOp2.positionStart) {
            i3++;
        }
        if (adapterHelper$UpdateOp2.positionStart <= adapterHelper$UpdateOp.positionStart) {
            adapterHelper$UpdateOp.positionStart += adapterHelper$UpdateOp2.itemCount;
        }
        if (adapterHelper$UpdateOp2.positionStart <= adapterHelper$UpdateOp.itemCount) {
            adapterHelper$UpdateOp.itemCount += adapterHelper$UpdateOp2.itemCount;
        }
        adapterHelper$UpdateOp2.positionStart = i3 + adapterHelper$UpdateOp2.positionStart;
        list.set(i, adapterHelper$UpdateOp2);
        list.set(i2, adapterHelper$UpdateOp);
    }

    void swapMoveUpdate(List<AdapterHelper$UpdateOp> list, int i, AdapterHelper$UpdateOp adapterHelper$UpdateOp, int i2, AdapterHelper$UpdateOp adapterHelper$UpdateOp2) {
        Object obj;
        Object obj2 = null;
        if (adapterHelper$UpdateOp.itemCount < adapterHelper$UpdateOp2.positionStart) {
            adapterHelper$UpdateOp2.positionStart--;
            obj = null;
        } else if (adapterHelper$UpdateOp.itemCount < adapterHelper$UpdateOp2.positionStart + adapterHelper$UpdateOp2.itemCount) {
            adapterHelper$UpdateOp2.itemCount--;
            obj = this.mCallback.obtainUpdateOp(4, adapterHelper$UpdateOp.positionStart, 1, adapterHelper$UpdateOp2.payload);
        } else {
            obj = null;
        }
        if (adapterHelper$UpdateOp.positionStart <= adapterHelper$UpdateOp2.positionStart) {
            adapterHelper$UpdateOp2.positionStart++;
        } else if (adapterHelper$UpdateOp.positionStart < adapterHelper$UpdateOp2.positionStart + adapterHelper$UpdateOp2.itemCount) {
            int i3 = (adapterHelper$UpdateOp2.positionStart + adapterHelper$UpdateOp2.itemCount) - adapterHelper$UpdateOp.positionStart;
            obj2 = this.mCallback.obtainUpdateOp(4, adapterHelper$UpdateOp.positionStart + 1, i3, adapterHelper$UpdateOp2.payload);
            adapterHelper$UpdateOp2.itemCount -= i3;
        }
        list.set(i2, adapterHelper$UpdateOp);
        if (adapterHelper$UpdateOp2.itemCount > 0) {
            list.set(i, adapterHelper$UpdateOp2);
        } else {
            list.remove(i);
            this.mCallback.recycleUpdateOp(adapterHelper$UpdateOp2);
        }
        if (obj != null) {
            list.add(i, obj);
        }
        if (obj2 != null) {
            list.add(i, obj2);
        }
    }

    private int getLastMoveOutOfOrder(List<AdapterHelper$UpdateOp> list) {
        Object obj = null;
        int size = list.size() - 1;
        while (size >= 0) {
            Object obj2;
            if (((AdapterHelper$UpdateOp) list.get(size)).cmd != 8) {
                obj2 = 1;
            } else if (obj != null) {
                return size;
            } else {
                obj2 = obj;
            }
            size--;
            obj = obj2;
        }
        return -1;
    }
}
