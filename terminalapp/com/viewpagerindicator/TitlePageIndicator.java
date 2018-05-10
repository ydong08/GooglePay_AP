package com.viewpagerindicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import java.util.ArrayList;

public class TitlePageIndicator extends View implements OnPageChangeListener {
    private int mActivePointerId;
    private boolean mBoldText;
    private final Rect mBounds;
    private OnCenterItemClickListener mCenterItemClickListener;
    private float mClipPadding;
    private int mColorSelected;
    private int mColorText;
    private int mCurrentPage;
    private float mFooterIndicatorHeight;
    private IndicatorStyle mFooterIndicatorStyle;
    private float mFooterIndicatorUnderlinePadding;
    private float mFooterLineHeight;
    private float mFooterPadding;
    private boolean mIsDragging;
    private float mLastMotionX;
    private LinePosition mLinePosition;
    private OnPageChangeListener mListener;
    private float mPageOffset;
    private final Paint mPaintFooterIndicator;
    private final Paint mPaintFooterLine;
    private final Paint mPaintText;
    private Path mPath;
    private int mScrollState;
    private float mTitlePadding;
    private float mTopPadding;
    private int mTouchSlop;
    private ViewPager mViewPager;

    public enum IndicatorStyle {
        None(0),
        Triangle(1),
        Underline(2);
        
        public final int value;

        private IndicatorStyle(int i) {
            this.value = i;
        }

        public static IndicatorStyle fromValue(int i) {
            for (IndicatorStyle indicatorStyle : values()) {
                if (indicatorStyle.value == i) {
                    return indicatorStyle;
                }
            }
            return null;
        }
    }

    public enum LinePosition {
        Bottom(0),
        Top(1);
        
        public final int value;

        private LinePosition(int i) {
            this.value = i;
        }

        public static LinePosition fromValue(int i) {
            for (LinePosition linePosition : values()) {
                if (linePosition.value == i) {
                    return linePosition;
                }
            }
            return null;
        }
    }

    public interface OnCenterItemClickListener {
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int currentPage;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.currentPage = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.currentPage);
        }
    }

    public TitlePageIndicator(Context context) {
        this(context, null);
    }

    public TitlePageIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.vpiTitlePageIndicatorStyle);
    }

    public TitlePageIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentPage = -1;
        this.mPaintText = new Paint();
        this.mPath = new Path();
        this.mBounds = new Rect();
        this.mPaintFooterLine = new Paint();
        this.mPaintFooterIndicator = new Paint();
        this.mLastMotionX = -1.0f;
        this.mActivePointerId = -1;
        if (!isInEditMode()) {
            Resources resources = getResources();
            int color = resources.getColor(R.color.default_title_indicator_footer_color);
            float dimension = resources.getDimension(R.dimen.default_title_indicator_footer_line_height);
            int integer = resources.getInteger(R.integer.default_title_indicator_footer_indicator_style);
            float dimension2 = resources.getDimension(R.dimen.default_title_indicator_footer_indicator_height);
            float dimension3 = resources.getDimension(R.dimen.default_title_indicator_footer_indicator_underline_padding);
            float dimension4 = resources.getDimension(R.dimen.default_title_indicator_footer_padding);
            int integer2 = resources.getInteger(R.integer.default_title_indicator_line_position);
            int color2 = resources.getColor(R.color.default_title_indicator_selected_color);
            boolean z = resources.getBoolean(R.bool.default_title_indicator_selected_bold);
            int color3 = resources.getColor(R.color.default_title_indicator_text_color);
            float dimension5 = resources.getDimension(R.dimen.default_title_indicator_text_size);
            float dimension6 = resources.getDimension(R.dimen.default_title_indicator_title_padding);
            float dimension7 = resources.getDimension(R.dimen.default_title_indicator_clip_padding);
            float dimension8 = resources.getDimension(R.dimen.default_title_indicator_top_padding);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TitlePageIndicator, i, 0);
            this.mFooterLineHeight = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_footerLineHeight, dimension);
            this.mFooterIndicatorStyle = IndicatorStyle.fromValue(obtainStyledAttributes.getInteger(R.styleable.TitlePageIndicator_footerIndicatorStyle, integer));
            this.mFooterIndicatorHeight = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_footerIndicatorHeight, dimension2);
            this.mFooterIndicatorUnderlinePadding = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_footerIndicatorUnderlinePadding, dimension3);
            this.mFooterPadding = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_footerPadding, dimension4);
            this.mLinePosition = LinePosition.fromValue(obtainStyledAttributes.getInteger(R.styleable.TitlePageIndicator_linePosition, integer2));
            this.mTopPadding = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_topPadding, dimension8);
            this.mTitlePadding = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_titlePadding, dimension6);
            this.mClipPadding = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_clipPadding, dimension7);
            this.mColorSelected = obtainStyledAttributes.getColor(R.styleable.TitlePageIndicator_selectedColor, color2);
            this.mColorText = obtainStyledAttributes.getColor(R.styleable.TitlePageIndicator_android_textColor, color3);
            this.mBoldText = obtainStyledAttributes.getBoolean(R.styleable.TitlePageIndicator_selectedBold, z);
            dimension8 = obtainStyledAttributes.getDimension(R.styleable.TitlePageIndicator_android_textSize, dimension5);
            color = obtainStyledAttributes.getColor(R.styleable.TitlePageIndicator_footerColor, color);
            this.mPaintText.setTextSize(dimension8);
            this.mPaintText.setAntiAlias(true);
            this.mPaintFooterLine.setStyle(Style.FILL_AND_STROKE);
            this.mPaintFooterLine.setStrokeWidth(this.mFooterLineHeight);
            this.mPaintFooterLine.setColor(color);
            this.mPaintFooterIndicator.setStyle(Style.FILL_AND_STROKE);
            this.mPaintFooterIndicator.setColor(color);
            Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.TitlePageIndicator_android_background);
            if (drawable != null) {
                setBackgroundDrawable(drawable);
            }
            obtainStyledAttributes.recycle();
            this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        }
    }

    public void setFooterColor(int i) {
        this.mPaintFooterLine.setColor(i);
        this.mPaintFooterIndicator.setColor(i);
        invalidate();
    }

    public void setFooterLineHeight(float f) {
        this.mFooterLineHeight = f;
        this.mPaintFooterLine.setStrokeWidth(this.mFooterLineHeight);
        invalidate();
    }

    public void setFooterIndicatorHeight(float f) {
        this.mFooterIndicatorHeight = f;
        invalidate();
    }

    public void setFooterIndicatorPadding(float f) {
        this.mFooterPadding = f;
        invalidate();
    }

    public void setFooterIndicatorStyle(IndicatorStyle indicatorStyle) {
        this.mFooterIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public void setLinePosition(LinePosition linePosition) {
        this.mLinePosition = linePosition;
        invalidate();
    }

    public void setSelectedColor(int i) {
        this.mColorSelected = i;
        invalidate();
    }

    public void setSelectedBold(boolean z) {
        this.mBoldText = z;
        invalidate();
    }

    public void setTextColor(int i) {
        this.mPaintText.setColor(i);
        this.mColorText = i;
        invalidate();
    }

    public void setTextSize(float f) {
        this.mPaintText.setTextSize(f);
        invalidate();
    }

    public void setTitlePadding(float f) {
        this.mTitlePadding = f;
        invalidate();
    }

    public void setTopPadding(float f) {
        this.mTopPadding = f;
        invalidate();
    }

    public void setClipPadding(float f) {
        this.mClipPadding = f;
        invalidate();
    }

    public void setTypeface(Typeface typeface) {
        this.mPaintText.setTypeface(typeface);
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mViewPager != null) {
            int count = this.mViewPager.getAdapter().getCount();
            if (count != 0) {
                if (this.mCurrentPage == -1 && this.mViewPager != null) {
                    this.mCurrentPage = this.mViewPager.getCurrentItem();
                }
                ArrayList calculateAllBounds = calculateAllBounds(this.mPaintText);
                int size = calculateAllBounds.size();
                if (this.mCurrentPage >= size) {
                    setCurrentItem(size - 1);
                    return;
                }
                int i;
                float f;
                Object obj;
                int i2;
                Rect rect;
                int i3 = count - 1;
                float width = ((float) getWidth()) / 2.0f;
                int left = getLeft();
                float f2 = ((float) left) + this.mClipPadding;
                int width2 = getWidth();
                int height = getHeight();
                int i4 = left + width2;
                float f3 = ((float) i4) - this.mClipPadding;
                int i5 = this.mCurrentPage;
                if (((double) this.mPageOffset) <= 0.5d) {
                    i = i5;
                    f = this.mPageOffset;
                } else {
                    i = i5 + 1;
                    f = 1.0f - this.mPageOffset;
                }
                Object obj2 = f <= 0.25f ? 1 : null;
                if (f <= 0.05f) {
                    obj = 1;
                } else {
                    obj = null;
                }
                float f4 = (0.25f - f) / 0.25f;
                Rect rect2 = (Rect) calculateAllBounds.get(this.mCurrentPage);
                f = (float) (rect2.right - rect2.left);
                if (((float) rect2.left) < f2) {
                    clipViewOnTheLeft(rect2, f, left);
                }
                if (((float) rect2.right) > f3) {
                    clipViewOnTheRight(rect2, f, i4);
                }
                if (this.mCurrentPage > 0) {
                    for (i2 = this.mCurrentPage - 1; i2 >= 0; i2--) {
                        rect2 = (Rect) calculateAllBounds.get(i2);
                        if (((float) rect2.left) < f2) {
                            int i6 = rect2.right - rect2.left;
                            clipViewOnTheLeft(rect2, (float) i6, left);
                            rect = (Rect) calculateAllBounds.get(i2 + 1);
                            if (((float) rect2.right) + this.mTitlePadding > ((float) rect.left)) {
                                rect2.left = (int) (((float) (rect.left - i6)) - this.mTitlePadding);
                                rect2.right = rect2.left + i6;
                            }
                        }
                    }
                }
                if (this.mCurrentPage < i3) {
                    for (i2 = this.mCurrentPage + 1; i2 < count; i2++) {
                        rect2 = (Rect) calculateAllBounds.get(i2);
                        if (((float) rect2.right) > f3) {
                            i3 = rect2.right - rect2.left;
                            clipViewOnTheRight(rect2, (float) i3, i4);
                            rect = (Rect) calculateAllBounds.get(i2 - 1);
                            if (((float) rect2.left) - this.mTitlePadding < ((float) rect.right)) {
                                rect2.left = (int) (((float) rect.right) + this.mTitlePadding);
                                rect2.right = rect2.left + i3;
                            }
                        }
                    }
                }
                int i7 = this.mColorText >>> 24;
                int i8 = 0;
                while (i8 < count) {
                    Rect rect3 = (Rect) calculateAllBounds.get(i8);
                    if ((rect3.left > left && rect3.left < i4) || (rect3.right > left && rect3.right < i4)) {
                        Object obj3 = i8 == i ? 1 : null;
                        CharSequence title = getTitle(i8);
                        Paint paint = this.mPaintText;
                        boolean z = (obj3 == null || obj == null || !this.mBoldText) ? false : true;
                        paint.setFakeBoldText(z);
                        this.mPaintText.setColor(this.mColorText);
                        if (!(obj3 == null || obj2 == null)) {
                            this.mPaintText.setAlpha(i7 - ((int) (((float) i7) * f4)));
                        }
                        if (i8 < size - 1) {
                            rect2 = (Rect) calculateAllBounds.get(i8 + 1);
                            if (((float) rect3.right) + this.mTitlePadding > ((float) rect2.left)) {
                                i2 = rect3.right - rect3.left;
                                rect3.left = (int) (((float) (rect2.left - i2)) - this.mTitlePadding);
                                rect3.right = rect3.left + i2;
                            }
                        }
                        canvas.drawText(title, 0, title.length(), (float) rect3.left, this.mTopPadding + ((float) rect3.bottom), this.mPaintText);
                        if (!(obj3 == null || obj2 == null)) {
                            this.mPaintText.setColor(this.mColorSelected);
                            this.mPaintText.setAlpha((int) (((float) (this.mColorSelected >>> 24)) * f4));
                            canvas.drawText(title, 0, title.length(), (float) rect3.left, this.mTopPadding + ((float) rect3.bottom), this.mPaintText);
                        }
                    }
                    i8++;
                }
                f = this.mFooterLineHeight;
                float f5 = this.mFooterIndicatorHeight;
                float f6;
                if (this.mLinePosition == LinePosition.Top) {
                    i2 = 0;
                    f6 = -f5;
                    f5 = -f;
                    f = f6;
                } else {
                    i2 = height;
                    f6 = f;
                    f = f5;
                    f5 = f6;
                }
                this.mPath.reset();
                this.mPath.moveTo(0.0f, ((float) i2) - (f5 / 2.0f));
                this.mPath.lineTo((float) width2, ((float) i2) - (f5 / 2.0f));
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mPaintFooterLine);
                float f7 = ((float) i2) - f5;
                switch (this.mFooterIndicatorStyle) {
                    case Triangle:
                        this.mPath.reset();
                        this.mPath.moveTo(width, f7 - f);
                        this.mPath.lineTo(width + f, f7);
                        this.mPath.lineTo(width - f, f7);
                        this.mPath.close();
                        canvas.drawPath(this.mPath, this.mPaintFooterIndicator);
                        return;
                    case Underline:
                        if (obj2 != null && i < size) {
                            rect2 = (Rect) calculateAllBounds.get(i);
                            float f8 = ((float) rect2.right) + this.mFooterIndicatorUnderlinePadding;
                            f5 = ((float) rect2.left) - this.mFooterIndicatorUnderlinePadding;
                            f = f7 - f;
                            this.mPath.reset();
                            this.mPath.moveTo(f5, f7);
                            this.mPath.lineTo(f8, f7);
                            this.mPath.lineTo(f8, f);
                            this.mPath.lineTo(f5, f);
                            this.mPath.close();
                            this.mPaintFooterIndicator.setAlpha((int) (255.0f * f4));
                            canvas.drawPath(this.mPath, this.mPaintFooterIndicator);
                            this.mPaintFooterIndicator.setAlpha(255);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i = 0;
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.mViewPager == null || this.mViewPager.getAdapter().getCount() == 0) {
            return false;
        }
        int action = motionEvent.getAction() & 255;
        switch (action) {
            case 0:
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                this.mLastMotionX = motionEvent.getX();
                return true;
            case 1:
            case 3:
                if (!this.mIsDragging) {
                    int count = this.mViewPager.getAdapter().getCount();
                    int width = getWidth();
                    float f = ((float) width) / 2.0f;
                    float f2 = ((float) width) / 6.0f;
                    float f3 = f - f2;
                    f2 += f;
                    f = motionEvent.getX();
                    if (f < f3) {
                        if (this.mCurrentPage > 0) {
                            if (action == 3) {
                                return true;
                            }
                            this.mViewPager.setCurrentItem(this.mCurrentPage - 1);
                            return true;
                        }
                    } else if (f > f2 && this.mCurrentPage < count - 1) {
                        if (action == 3) {
                            return true;
                        }
                        this.mViewPager.setCurrentItem(this.mCurrentPage + 1);
                        return true;
                    }
                }
                this.mIsDragging = false;
                this.mActivePointerId = -1;
                if (!this.mViewPager.isFakeDragging()) {
                    return true;
                }
                this.mViewPager.endFakeDrag();
                return true;
            case 2:
                float x = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                float f4 = x - this.mLastMotionX;
                if (!this.mIsDragging && Math.abs(f4) > ((float) this.mTouchSlop)) {
                    this.mIsDragging = true;
                }
                if (!this.mIsDragging) {
                    return true;
                }
                this.mLastMotionX = x;
                if (!this.mViewPager.isFakeDragging() && !this.mViewPager.beginFakeDrag()) {
                    return true;
                }
                this.mViewPager.fakeDragBy(f4);
                return true;
            case 5:
                i = MotionEventCompat.getActionIndex(motionEvent);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, i);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i);
                return true;
            case 6:
                action = MotionEventCompat.getActionIndex(motionEvent);
                if (MotionEventCompat.getPointerId(motionEvent, action) == this.mActivePointerId) {
                    if (action == 0) {
                        i = 1;
                    }
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i);
                }
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                return true;
            default:
                return true;
        }
    }

    private void clipViewOnTheRight(Rect rect, float f, int i) {
        rect.right = (int) (((float) i) - this.mClipPadding);
        rect.left = (int) (((float) rect.right) - f);
    }

    private void clipViewOnTheLeft(Rect rect, float f, int i) {
        rect.left = (int) (((float) i) + this.mClipPadding);
        rect.right = (int) (this.mClipPadding + f);
    }

    private ArrayList<Rect> calculateAllBounds(Paint paint) {
        ArrayList<Rect> arrayList = new ArrayList();
        int count = this.mViewPager.getAdapter().getCount();
        int width = getWidth();
        int i = width / 2;
        for (int i2 = 0; i2 < count; i2++) {
            Rect calcBounds = calcBounds(i2, paint);
            int i3 = calcBounds.right - calcBounds.left;
            int i4 = calcBounds.bottom - calcBounds.top;
            calcBounds.left = (int) ((((float) i) - (((float) i3) / 2.0f)) + ((((float) (i2 - this.mCurrentPage)) - this.mPageOffset) * ((float) width)));
            calcBounds.right = i3 + calcBounds.left;
            calcBounds.top = 0;
            calcBounds.bottom = i4;
            arrayList.add(calcBounds);
        }
        return arrayList;
    }

    private Rect calcBounds(int i, Paint paint) {
        Rect rect = new Rect();
        CharSequence title = getTitle(i);
        rect.right = (int) paint.measureText(title, 0, title.length());
        rect.bottom = (int) (paint.descent() - paint.ascent());
        return rect;
    }

    public void setViewPager(ViewPager viewPager) {
        if (this.mViewPager != viewPager) {
            if (this.mViewPager != null) {
                this.mViewPager.setOnPageChangeListener(null);
            }
            if (viewPager.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.mViewPager = viewPager;
            this.mViewPager.setOnPageChangeListener(this);
            invalidate();
        }
    }

    public void setViewPager(ViewPager viewPager, int i) {
        setViewPager(viewPager);
        setCurrentItem(i);
    }

    public void setOnCenterItemClickListener(OnCenterItemClickListener onCenterItemClickListener) {
        this.mCenterItemClickListener = onCenterItemClickListener;
    }

    public void setCurrentItem(int i) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mViewPager.setCurrentItem(i);
        this.mCurrentPage = i;
        invalidate();
    }

    public void onPageScrollStateChanged(int i) {
        this.mScrollState = i;
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(i);
        }
    }

    public void onPageScrolled(int i, float f, int i2) {
        this.mCurrentPage = i;
        this.mPageOffset = f;
        invalidate();
        if (this.mListener != null) {
            this.mListener.onPageScrolled(i, f, i2);
        }
    }

    public void onPageSelected(int i) {
        if (this.mScrollState == 0) {
            this.mCurrentPage = i;
            invalidate();
        }
        if (this.mListener != null) {
            this.mListener.onPageSelected(i);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mListener = onPageChangeListener;
    }

    protected void onMeasure(int i, int i2) {
        float size;
        int size2 = MeasureSpec.getSize(i);
        if (MeasureSpec.getMode(i2) == 1073741824) {
            size = (float) MeasureSpec.getSize(i2);
        } else {
            this.mBounds.setEmpty();
            this.mBounds.bottom = (int) (this.mPaintText.descent() - this.mPaintText.ascent());
            size = ((((float) (this.mBounds.bottom - this.mBounds.top)) + this.mFooterLineHeight) + this.mFooterPadding) + this.mTopPadding;
            if (this.mFooterIndicatorStyle != IndicatorStyle.None) {
                size += this.mFooterIndicatorHeight;
            }
        }
        setMeasuredDimension(size2, (int) size);
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        Parcelable savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPage = this.mCurrentPage;
        return savedState;
    }

    private CharSequence getTitle(int i) {
        CharSequence pageTitle = this.mViewPager.getAdapter().getPageTitle(i);
        if (pageTitle == null) {
            return "";
        }
        return pageTitle;
    }
}
