package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.resultview.ResultPageFragment;
import java.util.ArrayList;
import java.util.List;

public class FragmentStatePagerAdapter extends PagerAdapter {
    private final Context context;
    private Result expectedResult;
    private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem;
    private final FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;
    private ArrayList<SavedState> mSavedState;
    private List<Result> results;

    public FragmentStatePagerAdapter(FragmentManager fragmentManager) {
        this.mCurTransaction = null;
        this.mSavedState = new ArrayList();
        this.mFragments = new ArrayList();
        this.mCurrentPrimaryItem = null;
        this.mFragmentManager = fragmentManager;
    }

    public void startUpdate(ViewGroup viewGroup) {
        if (viewGroup.getId() == -1) {
            throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
        }
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        if (this.mFragments.size() > i) {
            Fragment fragment = (Fragment) this.mFragments.get(i);
            if (fragment != null) {
                return fragment;
            }
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        Fragment item = getItem(i);
        if (this.mSavedState.size() > i) {
            SavedState savedState = (SavedState) this.mSavedState.get(i);
            if (savedState != null) {
                item.setInitialSavedState(savedState);
            }
        }
        while (this.mFragments.size() <= i) {
            this.mFragments.add(null);
        }
        item.setMenuVisibility(false);
        item.setUserVisibleHint(false);
        this.mFragments.set(i, item);
        this.mCurTransaction.add(viewGroup.getId(), item);
        return item;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        Object saveFragmentInstanceState;
        Fragment fragment = (Fragment) obj;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        while (this.mSavedState.size() <= i) {
            this.mSavedState.add(null);
        }
        ArrayList arrayList = this.mSavedState;
        if (fragment.isAdded()) {
            saveFragmentInstanceState = this.mFragmentManager.saveFragmentInstanceState(fragment);
        } else {
            saveFragmentInstanceState = null;
        }
        arrayList.set(i, saveFragmentInstanceState);
        this.mFragments.set(i, null);
        this.mCurTransaction.remove(fragment);
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        Fragment fragment = (Fragment) obj;
        if (fragment != this.mCurrentPrimaryItem) {
            if (this.mCurrentPrimaryItem != null) {
                this.mCurrentPrimaryItem.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = fragment;
        }
    }

    public void finishUpdate(ViewGroup viewGroup) {
        if (this.mCurTransaction != null) {
            this.mCurTransaction.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
        }
    }

    public boolean isViewFromObject(View view, Object obj) {
        return ((Fragment) obj).getView() == view;
    }

    public Parcelable saveState() {
        Bundle bundle = null;
        if (this.mSavedState.size() > 0) {
            bundle = new Bundle();
            Parcelable[] parcelableArr = new SavedState[this.mSavedState.size()];
            this.mSavedState.toArray(parcelableArr);
            bundle.putParcelableArray("states", parcelableArr);
        }
        Parcelable parcelable = bundle;
        for (int i = 0; i < this.mFragments.size(); i++) {
            Fragment fragment = (Fragment) this.mFragments.get(i);
            if (fragment != null && fragment.isAdded()) {
                if (parcelable == null) {
                    parcelable = new Bundle();
                }
                this.mFragmentManager.putFragment(parcelable, "f" + i, fragment);
            }
        }
        return parcelable;
    }

    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        if (parcelable != null) {
            Bundle bundle = (Bundle) parcelable;
            bundle.setClassLoader(classLoader);
            Parcelable[] parcelableArray = bundle.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if (parcelableArray != null) {
                for (Parcelable parcelable2 : parcelableArray) {
                    this.mSavedState.add((SavedState) parcelable2);
                }
            }
            for (String str : bundle.keySet()) {
                if (str.startsWith("f")) {
                    int parseInt = Integer.parseInt(str.substring(1));
                    Fragment fragment = this.mFragmentManager.getFragment(bundle, str);
                    if (fragment != null) {
                        while (this.mFragments.size() <= parseInt) {
                            this.mFragments.add(null);
                        }
                        fragment.setMenuVisibility(false);
                        this.mFragments.set(parseInt, fragment);
                    } else {
                        Log.w("FragmentStatePagerAdapter", "Bad fragment at key " + str);
                    }
                }
            }
        }
    }

    public FragmentStatePagerAdapter(FragmentManager fragmentManager, Context context, List<Result> list, Result result) {
        this(fragmentManager);
        this.context = context;
        this.results = list;
        this.expectedResult = result;
    }

    public void setResults(List<Result> list) {
        this.results = list;
        notifyDataSetChanged();
    }

    public void updateExpectedResult(Result result) {
        this.expectedResult = result;
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(ResultPageFragment.createExpectedResultBroadcast(result));
    }

    public Fragment getItem(int i) {
        Fragment resultPageFragment = new ResultPageFragment();
        resultPageFragment.setArguments(ResultPageFragment.createArgs((Result) this.results.get(i), this.expectedResult));
        return resultPageFragment;
    }

    public int getCount() {
        return this.results.size();
    }
}
