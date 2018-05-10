package android.support.v7.widget;

import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;

class AdapterHelper$Callback {
    final /* synthetic */ RecyclerView this$0;

    AdapterHelper$Callback(RecyclerView recyclerView) {
        this.this$0 = recyclerView;
    }

    public ViewHolder findViewHolder(int i) {
        ViewHolder findViewHolderForPosition = this.this$0.findViewHolderForPosition(i, true);
        if (findViewHolderForPosition == null || this.this$0.mChildHelper.isHidden(findViewHolderForPosition.itemView)) {
            return null;
        }
        return findViewHolderForPosition;
    }

    public void offsetPositionsForRemovingInvisible(int i, int i2) {
        this.this$0.offsetPositionRecordsForRemove(i, i2, true);
        this.this$0.mItemsAddedOrRemoved = true;
        State state = this.this$0.mState;
        state.mDeletedInvisibleItemCountSincePreviousLayout += i2;
    }

    public void offsetPositionsForRemovingLaidOutOrNewView(int i, int i2) {
        this.this$0.offsetPositionRecordsForRemove(i, i2, false);
        this.this$0.mItemsAddedOrRemoved = true;
    }

    public void markViewHoldersUpdated(int i, int i2, Object obj) {
        this.this$0.viewRangeUpdate(i, i2, obj);
        this.this$0.mItemsChanged = true;
    }

    public void onDispatchFirstPass(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
        dispatchUpdate(adapterHelper$UpdateOp);
    }

    void dispatchUpdate(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
        switch (adapterHelper$UpdateOp.cmd) {
            case 1:
                this.this$0.mLayout.onItemsAdded(this.this$0, adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                return;
            case 2:
                this.this$0.mLayout.onItemsRemoved(this.this$0, adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount);
                return;
            case 4:
                this.this$0.mLayout.onItemsUpdated(this.this$0, adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount, adapterHelper$UpdateOp.payload);
                return;
            case 8:
                this.this$0.mLayout.onItemsMoved(this.this$0, adapterHelper$UpdateOp.positionStart, adapterHelper$UpdateOp.itemCount, 1);
                return;
            default:
                return;
        }
    }

    public void onDispatchSecondPass(AdapterHelper$UpdateOp adapterHelper$UpdateOp) {
        dispatchUpdate(adapterHelper$UpdateOp);
    }

    public void offsetPositionsForAdd(int i, int i2) {
        this.this$0.offsetPositionRecordsForInsert(i, i2);
        this.this$0.mItemsAddedOrRemoved = true;
    }

    public void offsetPositionsForMove(int i, int i2) {
        this.this$0.offsetPositionRecordsForMove(i, i2);
        this.this$0.mItemsAddedOrRemoved = true;
    }
}
