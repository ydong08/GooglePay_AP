package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListFragment extends Fragment {
    ListAdapter mAdapter;
    CharSequence mEmptyText;
    View mEmptyView;
    private final Handler mHandler = new Handler();
    ListView mList;
    View mListContainer;
    boolean mListShown;
    private final OnItemClickListener mOnClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            ListFragment.this.onListItemClick((ListView) adapterView, view, i, j);
        }
    };
    View mProgressContainer;
    private final Runnable mRequestFocus = new Runnable() {
        public void run() {
            ListFragment.this.mList.focusableViewAvailable(ListFragment.this.mList);
        }
    };
    TextView mStandardEmptyView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Context context = getContext();
        View frameLayout = new FrameLayout(context);
        View linearLayout = new LinearLayout(context);
        linearLayout.setId(16711682);
        linearLayout.setOrientation(1);
        linearLayout.setVisibility(8);
        linearLayout.setGravity(17);
        linearLayout.addView(new ProgressBar(context, null, 16842874), new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout, new LayoutParams(-1, -1));
        linearLayout = new FrameLayout(context);
        linearLayout.setId(16711683);
        View textView = new TextView(context);
        textView.setId(16711681);
        textView.setGravity(17);
        linearLayout.addView(textView, new LayoutParams(-1, -1));
        textView = new ListView(context);
        textView.setId(16908298);
        textView.setDrawSelectorOnTop(false);
        linearLayout.addView(textView, new LayoutParams(-1, -1));
        frameLayout.addView(linearLayout, new LayoutParams(-1, -1));
        frameLayout.setLayoutParams(new LayoutParams(-1, -1));
        return frameLayout;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ensureList();
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mList = null;
        this.mListShown = false;
        this.mListContainer = null;
        this.mProgressContainer = null;
        this.mEmptyView = null;
        this.mStandardEmptyView = null;
        super.onDestroyView();
    }

    public void onListItemClick(ListView listView, View view, int i, long j) {
    }

    public void setListAdapter(ListAdapter listAdapter) {
        boolean z = false;
        boolean z2 = this.mAdapter != null;
        this.mAdapter = listAdapter;
        if (this.mList != null) {
            this.mList.setAdapter(listAdapter);
            if (!this.mListShown && !z2) {
                if (getView().getWindowToken() != null) {
                    z = true;
                }
                setListShown(true, z);
            }
        }
    }

    private void setListShown(boolean z, boolean z2) {
        ensureList();
        if (this.mProgressContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        } else if (this.mListShown != z) {
            this.mListShown = z;
            if (z) {
                if (z2) {
                    this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432577));
                    this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432576));
                } else {
                    this.mProgressContainer.clearAnimation();
                    this.mListContainer.clearAnimation();
                }
                this.mProgressContainer.setVisibility(8);
                this.mListContainer.setVisibility(0);
                return;
            }
            if (z2) {
                this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432576));
                this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432577));
            } else {
                this.mProgressContainer.clearAnimation();
                this.mListContainer.clearAnimation();
            }
            this.mProgressContainer.setVisibility(0);
            this.mListContainer.setVisibility(8);
        }
    }

    private void ensureList() {
        if (this.mList == null) {
            View view = getView();
            if (view == null) {
                throw new IllegalStateException("Content view not yet created");
            }
            if (view instanceof ListView) {
                this.mList = (ListView) view;
            } else {
                this.mStandardEmptyView = (TextView) view.findViewById(16711681);
                if (this.mStandardEmptyView == null) {
                    this.mEmptyView = view.findViewById(16908292);
                } else {
                    this.mStandardEmptyView.setVisibility(8);
                }
                this.mProgressContainer = view.findViewById(16711682);
                this.mListContainer = view.findViewById(16711683);
                view = view.findViewById(16908298);
                if (view instanceof ListView) {
                    this.mList = (ListView) view;
                    if (this.mEmptyView != null) {
                        this.mList.setEmptyView(this.mEmptyView);
                    } else if (this.mEmptyText != null) {
                        this.mStandardEmptyView.setText(this.mEmptyText);
                        this.mList.setEmptyView(this.mStandardEmptyView);
                    }
                } else if (view == null) {
                    throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
                } else {
                    throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
                }
            }
            this.mListShown = true;
            this.mList.setOnItemClickListener(this.mOnClickListener);
            if (this.mAdapter != null) {
                ListAdapter listAdapter = this.mAdapter;
                this.mAdapter = null;
                setListAdapter(listAdapter);
            } else if (this.mProgressContainer != null) {
                setListShown(false, false);
            }
            this.mHandler.post(this.mRequestFocus);
        }
    }
}
