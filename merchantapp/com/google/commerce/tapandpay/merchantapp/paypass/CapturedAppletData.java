package com.google.commerce.tapandpay.merchantapp.paypass;

import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;

public class CapturedAppletData {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private String mAcquirer;
    private String mApplicationVersion;
    private String mCtq;
    private String mCvr;
    private String mMerchantCategoryCode;
    private String mMerchantIdentifier;
    private String mMerchantName;
    private String mMessage;
    private String mPosEntryMode;
    private String mSelectedAid;
    private String mTerminalCapabilities;
    private String mTerminalIdentifier;
    private String mTerminalSerialNumber;
    private String mTerminalType;
    private String mTransactionDate;
    private String mTransactionType;
    private String mTtq;
    private String mTvr;

    public String toString() {
        String str = this.mMessage;
        String str2 = this.mAcquirer;
        String str3 = this.mApplicationVersion;
        String str4 = this.mCtq;
        String str5 = this.mCvr;
        String str6 = this.mMerchantCategoryCode;
        String str7 = this.mMerchantIdentifier;
        String str8 = this.mMerchantName;
        String str9 = this.mPosEntryMode;
        String str10 = this.mTerminalCapabilities;
        String str11 = this.mTerminalIdentifier;
        String str12 = this.mTerminalSerialNumber;
        String str13 = this.mTerminalType;
        String str14 = this.mTransactionDate;
        String str15 = this.mTransactionType;
        String str16 = this.mTtq;
        String str17 = this.mTvr;
        String str18 = this.mSelectedAid;
        return new StringBuilder((((((((((((((((((String.valueOf(str).length() + 344) + String.valueOf(str2).length()) + String.valueOf(str3).length()) + String.valueOf(str4).length()) + String.valueOf(str5).length()) + String.valueOf(str6).length()) + String.valueOf(str7).length()) + String.valueOf(str8).length()) + String.valueOf(str9).length()) + String.valueOf(str10).length()) + String.valueOf(str11).length()) + String.valueOf(str12).length()) + String.valueOf(str13).length()) + String.valueOf(str14).length()) + String.valueOf(str15).length()) + String.valueOf(str16).length()) + String.valueOf(str17).length()) + String.valueOf(str18).length()).append("CapturedAppletData{mMessage='").append(str).append("'").append(", mAcquirer='").append(str2).append("'").append(", mApplicationVersion='").append(str3).append("'").append(", mCtq='").append(str4).append("'").append(", mCvr='").append(str5).append("'").append(", mMerchantCategoryCode='").append(str6).append("'").append(", mMerchantIdentifier='").append(str7).append("'").append(", mMerchantName='").append(str8).append("'").append(", mPosEntryMode='").append(str9).append("'").append(", mTerminalCapabilities='").append(str10).append("'").append(", mTerminalIdentifier='").append(str11).append("'").append(", mTerminalSerialNumber='").append(str12).append("'").append(", mTerminalType='").append(str13).append("'").append(", mTransactionDate='").append(str14).append("'").append(", mTransactionType='").append(str15).append("'").append(", mTtq='").append(str16).append("'").append(", mTvr='").append(str17).append("'").append(", mSelectedAid='").append(str18).append("'").append("}").toString();
    }
}
