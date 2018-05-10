package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatBase.Action.Factory;
import android.support.v4.os.BuildCompat;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationCompat {
    static final NotificationCompatImpl IMPL;

    public static class Action extends android.support.v4.app.NotificationCompatBase.Action {
        public static final Factory FACTORY = new Factory();
        public PendingIntent actionIntent;
        public int icon;
        private boolean mAllowGeneratedReplies;
        final Bundle mExtras;
        private final RemoteInput[] mRemoteInputs;
        public CharSequence title;

        public int getIcon() {
            return this.icon;
        }

        public CharSequence getTitle() {
            return this.title;
        }

        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        CharSequence mSummaryText;
        boolean mSummaryTextSet = false;

        public void addCompatExtras(Bundle bundle) {
        }
    }

    public static class BigPictureStyle extends Style {
        Bitmap mBigLargeIcon;
        boolean mBigLargeIconSet;
        Bitmap mPicture;
    }

    public static class BigTextStyle extends Style {
        CharSequence mBigText;
    }

    public static class Builder {
        public ArrayList<Action> mActions = new ArrayList();
        RemoteViews mBigContentView;
        String mCategory;
        int mColor = 0;
        public CharSequence mContentInfo;
        PendingIntent mContentIntent;
        public CharSequence mContentText;
        public CharSequence mContentTitle;
        RemoteViews mContentView;
        public Context mContext;
        Bundle mExtras;
        PendingIntent mFullScreenIntent;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        public Bitmap mLargeIcon;
        boolean mLocalOnly = false;
        public Notification mNotification = new Notification();
        public int mNumber;
        public ArrayList<String> mPeople;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        public CharSequence[] mRemoteInputHistory;
        boolean mShowWhen = true;
        String mSortKey;
        public Style mStyle;
        public CharSequence mSubText;
        RemoteViews mTickerView;
        public boolean mUseChronometer;
        int mVisibility = 0;

        public Builder(Context context) {
            this.mContext = context;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList();
        }

        public Builder setWhen(long j) {
            this.mNotification.when = j;
            return this;
        }

        public Builder setSmallIcon(int i) {
            this.mNotification.icon = i;
            return this;
        }

        public Builder setContentTitle(CharSequence charSequence) {
            this.mContentTitle = limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setContentText(CharSequence charSequence) {
            this.mContentText = limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setContentIntent(PendingIntent pendingIntent) {
            this.mContentIntent = pendingIntent;
            return this;
        }

        public Builder setTicker(CharSequence charSequence) {
            this.mNotification.tickerText = limitCharSequenceLength(charSequence);
            return this;
        }

        public Builder setAutoCancel(boolean z) {
            setFlag(16, z);
            return this;
        }

        private void setFlag(int i, boolean z) {
            if (z) {
                Notification notification = this.mNotification;
                notification.flags |= i;
                return;
            }
            notification = this.mNotification;
            notification.flags &= i ^ -1;
        }

        public Notification build() {
            return NotificationCompat.IMPL.build(this, getExtender());
        }

        protected BuilderExtender getExtender() {
            return new BuilderExtender();
        }

        protected static CharSequence limitCharSequenceLength(CharSequence charSequence) {
            if (charSequence != null && charSequence.length() > 5120) {
                return charSequence.subSequence(0, 5120);
            }
            return charSequence;
        }

        protected CharSequence resolveText() {
            return this.mContentText;
        }

        protected CharSequence resolveTitle() {
            return this.mContentTitle;
        }
    }

    public static class BuilderExtender {
        protected BuilderExtender() {
        }

        public Notification build(Builder builder, NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            Notification build = notificationBuilderWithBuilderAccessor.build();
            if (builder.mContentView != null) {
                build.contentView = builder.mContentView;
            }
            return build;
        }
    }

    public static class InboxStyle extends Style {
        ArrayList<CharSequence> mTexts = new ArrayList();
    }

    public static class MessagingStyle extends Style {
        CharSequence mConversationTitle;
        List<Message> mMessages = new ArrayList();
        CharSequence mUserDisplayName;

        public static final class Message {
            private String mDataMimeType;
            private Uri mDataUri;
            private final CharSequence mSender;
            private final CharSequence mText;
            private final long mTimestamp;

            public CharSequence getText() {
                return this.mText;
            }

            public long getTimestamp() {
                return this.mTimestamp;
            }

            public CharSequence getSender() {
                return this.mSender;
            }

            public String getDataMimeType() {
                return this.mDataMimeType;
            }

            public Uri getDataUri() {
                return this.mDataUri;
            }

            private Bundle toBundle() {
                Bundle bundle = new Bundle();
                if (this.mText != null) {
                    bundle.putCharSequence("text", this.mText);
                }
                bundle.putLong("time", this.mTimestamp);
                if (this.mSender != null) {
                    bundle.putCharSequence("sender", this.mSender);
                }
                if (this.mDataMimeType != null) {
                    bundle.putString("type", this.mDataMimeType);
                }
                if (this.mDataUri != null) {
                    bundle.putParcelable("uri", this.mDataUri);
                }
                return bundle;
            }

            static Bundle[] getBundleArrayForMessages(List<Message> list) {
                Bundle[] bundleArr = new Bundle[list.size()];
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    bundleArr[i] = ((Message) list.get(i)).toBundle();
                }
                return bundleArr;
            }
        }

        MessagingStyle() {
        }

        public void addCompatExtras(Bundle bundle) {
            super.addCompatExtras(bundle);
            if (this.mUserDisplayName != null) {
                bundle.putCharSequence("android.selfDisplayName", this.mUserDisplayName);
            }
            if (this.mConversationTitle != null) {
                bundle.putCharSequence("android.conversationTitle", this.mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                bundle.putParcelableArray("android.messages", Message.getBundleArrayForMessages(this.mMessages));
            }
        }
    }

    interface NotificationCompatImpl {
        NotificationCompatImpl() {
        }

        Notification build(Builder builder, BuilderExtender builderExtender) {
            Notification add = NotificationCompatBase.add(builder.mNotification, builder.mContext, builder.resolveTitle(), builder.resolveText(), builder.mContentIntent, builder.mFullScreenIntent);
            if (builder.mPriority > 0) {
                add.flags |= 128;
            }
            if (builder.mContentView != null) {
                add.contentView = builder.mContentView;
            }
            return add;
        }

        Bundle getExtras(Notification notification) {
            return null;
        }
    }

    static class NotificationCompatImplJellybean extends NotificationCompatImpl {
        NotificationCompatImplJellybean() {
        }

        public Notification build(Builder builder, BuilderExtender builderExtender) {
            NotificationBuilderWithBuilderAccessor builder2 = new android.support.v4.app.NotificationCompatJellybean.Builder(builder.mContext, builder.mNotification, builder.resolveTitle(), builder.resolveText(), builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mExtras, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            NotificationCompat.addStyleToBuilderJellybean(builder2, builder.mStyle);
            Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                Bundle extras = getExtras(build);
                if (extras != null) {
                    builder.mStyle.addCompatExtras(extras);
                }
            }
            return build;
        }

        public Bundle getExtras(Notification notification) {
            return NotificationCompatJellybean.getExtras(notification);
        }
    }

    static class NotificationCompatImplKitKat extends NotificationCompatImplJellybean {
        NotificationCompatImplKitKat() {
        }

        public Notification build(Builder builder, BuilderExtender builderExtender) {
            NotificationBuilderWithBuilderAccessor builder2 = new android.support.v4.app.NotificationCompatKitKat.Builder(builder.mContext, builder.mNotification, builder.resolveTitle(), builder.resolveText(), builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mPeople, builder.mExtras, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            NotificationCompat.addStyleToBuilderJellybean(builder2, builder.mStyle);
            return builderExtender.build(builder, builder2);
        }

        public Bundle getExtras(Notification notification) {
            return NotificationCompatKitKat.getExtras(notification);
        }
    }

    static class NotificationCompatImplApi20 extends NotificationCompatImplKitKat {
        NotificationCompatImplApi20() {
        }

        public Notification build(Builder builder, BuilderExtender builderExtender) {
            NotificationBuilderWithBuilderAccessor builder2 = new android.support.v4.app.NotificationCompatApi20.Builder(builder.mContext, builder.mNotification, builder.resolveTitle(), builder.resolveText(), builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mPeople, builder.mExtras, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            NotificationCompat.addStyleToBuilderJellybean(builder2, builder.mStyle);
            Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                builder.mStyle.addCompatExtras(getExtras(build));
            }
            return build;
        }
    }

    static class NotificationCompatImplApi21 extends NotificationCompatImplApi20 {
        NotificationCompatImplApi21() {
        }

        public Notification build(Builder builder, BuilderExtender builderExtender) {
            NotificationBuilderWithBuilderAccessor builder2 = new android.support.v4.app.NotificationCompatApi21.Builder(builder.mContext, builder.mNotification, builder.resolveTitle(), builder.resolveText(), builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mCategory, builder.mPeople, builder.mExtras, builder.mColor, builder.mVisibility, builder.mPublicVersion, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView, builder.mHeadsUpContentView);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            NotificationCompat.addStyleToBuilderJellybean(builder2, builder.mStyle);
            Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                builder.mStyle.addCompatExtras(getExtras(build));
            }
            return build;
        }
    }

    static class NotificationCompatImplApi24 extends NotificationCompatImplApi21 {
        NotificationCompatImplApi24() {
        }

        public Notification build(Builder builder, BuilderExtender builderExtender) {
            NotificationBuilderWithBuilderAccessor builder2 = new android.support.v4.app.NotificationCompatApi24.Builder(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mCategory, builder.mPeople, builder.mExtras, builder.mColor, builder.mVisibility, builder.mPublicVersion, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mRemoteInputHistory, builder.mContentView, builder.mBigContentView, builder.mHeadsUpContentView);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            NotificationCompat.addStyleToBuilderApi24(builder2, builder.mStyle);
            Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                builder.mStyle.addCompatExtras(getExtras(build));
            }
            return build;
        }
    }

    static class NotificationCompatImplHoneycomb extends NotificationCompatImpl {
        NotificationCompatImplHoneycomb() {
        }

        public Notification build(Builder builder, BuilderExtender builderExtender) {
            Notification add = NotificationCompatHoneycomb.add(builder.mContext, builder.mNotification, builder.resolveTitle(), builder.resolveText(), builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon);
            if (builder.mContentView != null) {
                add.contentView = builder.mContentView;
            }
            return add;
        }
    }

    static class NotificationCompatImplIceCreamSandwich extends NotificationCompatImpl {
        NotificationCompatImplIceCreamSandwich() {
        }

        public Notification build(Builder builder, BuilderExtender builderExtender) {
            return builderExtender.build(builder, new android.support.v4.app.NotificationCompatIceCreamSandwich.Builder(builder.mContext, builder.mNotification, builder.resolveTitle(), builder.resolveText(), builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate));
        }
    }

    static void addActionsToBuilder(NotificationBuilderWithActions notificationBuilderWithActions, ArrayList<Action> arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            notificationBuilderWithActions.addAction((Action) it.next());
        }
    }

    static void addStyleToBuilderJellybean(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, Style style) {
        if (style == null) {
            return;
        }
        if (style instanceof BigTextStyle) {
            BigTextStyle bigTextStyle = (BigTextStyle) style;
            NotificationCompatJellybean.addBigTextStyle(notificationBuilderWithBuilderAccessor, bigTextStyle.mBigContentTitle, bigTextStyle.mSummaryTextSet, bigTextStyle.mSummaryText, bigTextStyle.mBigText);
        } else if (style instanceof InboxStyle) {
            InboxStyle inboxStyle = (InboxStyle) style;
            NotificationCompatJellybean.addInboxStyle(notificationBuilderWithBuilderAccessor, inboxStyle.mBigContentTitle, inboxStyle.mSummaryTextSet, inboxStyle.mSummaryText, inboxStyle.mTexts);
        } else if (style instanceof BigPictureStyle) {
            BigPictureStyle bigPictureStyle = (BigPictureStyle) style;
            NotificationCompatJellybean.addBigPictureStyle(notificationBuilderWithBuilderAccessor, bigPictureStyle.mBigContentTitle, bigPictureStyle.mSummaryTextSet, bigPictureStyle.mSummaryText, bigPictureStyle.mPicture, bigPictureStyle.mBigLargeIcon, bigPictureStyle.mBigLargeIconSet);
        }
    }

    static void addStyleToBuilderApi24(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, Style style) {
        if (style == null) {
            return;
        }
        if (style instanceof MessagingStyle) {
            MessagingStyle messagingStyle = (MessagingStyle) style;
            List arrayList = new ArrayList();
            List arrayList2 = new ArrayList();
            List arrayList3 = new ArrayList();
            List arrayList4 = new ArrayList();
            List arrayList5 = new ArrayList();
            for (Message message : messagingStyle.mMessages) {
                arrayList.add(message.getText());
                arrayList2.add(Long.valueOf(message.getTimestamp()));
                arrayList3.add(message.getSender());
                arrayList4.add(message.getDataMimeType());
                arrayList5.add(message.getDataUri());
            }
            NotificationCompatApi24.addMessagingStyle(notificationBuilderWithBuilderAccessor, messagingStyle.mUserDisplayName, messagingStyle.mConversationTitle, arrayList, arrayList2, arrayList3, arrayList4, arrayList5);
            return;
        }
        addStyleToBuilderJellybean(notificationBuilderWithBuilderAccessor, style);
    }

    static {
        if (BuildCompat.isAtLeastN()) {
            IMPL = new NotificationCompatImplApi24();
        } else if (VERSION.SDK_INT >= 21) {
            IMPL = new NotificationCompatImplApi21();
        } else if (VERSION.SDK_INT >= 20) {
            IMPL = new NotificationCompatImplApi20();
        } else if (VERSION.SDK_INT >= 19) {
            IMPL = new NotificationCompatImplKitKat();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new NotificationCompatImplJellybean();
        } else if (VERSION.SDK_INT >= 14) {
            IMPL = new NotificationCompatImplIceCreamSandwich();
        } else if (VERSION.SDK_INT >= 11) {
            IMPL = new NotificationCompatImplHoneycomb();
        } else {
            IMPL = new NotificationCompatImpl();
        }
    }
}
