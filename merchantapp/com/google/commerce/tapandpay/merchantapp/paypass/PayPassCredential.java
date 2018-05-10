package com.google.commerce.tapandpay.merchantapp.paypass;

import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Arrays;

public class PayPassCredential {
    private final byte[] mAfl;
    private final byte[] mAip;
    private final byte mApplicationPriorityIndicator;
    private final short mAvn;
    private final byte mIssuerCodeTableIndex;
    private final byte mNatcTrack1;
    private final byte mNatcTrack2;
    private final PayPassCrypto mPayPassCrypto;
    private final byte[] mPcvc3Track1;
    private final byte[] mPcvc3Track2;
    private final byte[] mPunatcTrack1;
    private final byte[] mPunatcTrack2;
    private final int mReaderAtc;
    private final int mSecretAtc;
    private final byte[] mTrack1Data;
    private final byte[] mTrack2Data;
    private final byte[] mUdol;

    public static class Builder {
        private byte[] mAfl;
        private byte[] mAip;
        private byte mApplicationPriorityIndicator;
        private short mAvn;
        private boolean mHaveApplicationPriorityIndicator;
        private boolean mHaveAvn;
        private boolean mHaveIssuerCodeTableIndex;
        private boolean mHaveNatcTrack1;
        private boolean mHaveNatcTrack2;
        private byte mIssuerCodeTableIndex;
        private byte mNatcTrack1;
        private byte mNatcTrack2;
        private PayPassCrypto mPayPassCrypto;
        private byte[] mPcvc3Track1;
        private byte[] mPcvc3Track2;
        private byte[] mPunatcTrack1;
        private byte[] mPunatcTrack2;
        private int mReaderAtc;
        private int mSecretAtc;
        private byte[] mTrack1Data;
        private byte[] mTrack2Data;
        private byte[] mUdol;

        private Builder() {
        }

        public PayPassCredential build() {
            Preconditions.checkState(this.mHaveApplicationPriorityIndicator);
            Preconditions.checkState(this.mHaveIssuerCodeTableIndex);
            Preconditions.checkNotNull(this.mAip);
            Preconditions.checkNotNull(this.mAfl);
            Preconditions.checkState(this.mHaveAvn);
            Preconditions.checkNotNull(this.mPcvc3Track1);
            Preconditions.checkNotNull(this.mPunatcTrack1);
            Preconditions.checkNotNull(this.mTrack1Data);
            Preconditions.checkState(this.mHaveNatcTrack1);
            Preconditions.checkNotNull(this.mPcvc3Track2);
            Preconditions.checkNotNull(this.mPunatcTrack2);
            Preconditions.checkNotNull(this.mTrack2Data);
            Preconditions.checkState(this.mHaveNatcTrack2);
            Preconditions.checkNotNull(this.mPayPassCrypto);
            return new PayPassCredential(this.mApplicationPriorityIndicator, this.mIssuerCodeTableIndex, this.mAip, this.mAfl, this.mAvn, this.mPcvc3Track1, this.mPunatcTrack1, this.mTrack1Data, this.mNatcTrack1, this.mPcvc3Track2, this.mPunatcTrack2, this.mTrack2Data, this.mNatcTrack2, this.mUdol, this.mPayPassCrypto, this.mSecretAtc, this.mReaderAtc);
        }

        public Builder setApplicationPriorityIndicator(byte b) {
            this.mHaveApplicationPriorityIndicator = true;
            this.mApplicationPriorityIndicator = b;
            return this;
        }

        public Builder setIssuerCodeTableIndex(byte b) {
            this.mHaveIssuerCodeTableIndex = true;
            this.mIssuerCodeTableIndex = b;
            return this;
        }

        public Builder setAip(byte[] bArr) {
            Preconditions.checkArgument(bArr.length == 2, "Unexpected AIP length: %s", bArr.length);
            this.mAip = bArr;
            return this;
        }

        public Builder setAfl(byte[] bArr) {
            Preconditions.checkArgument(bArr.length <= 252, "Unexpected AFL length: %s", bArr.length);
            this.mAfl = bArr;
            return this;
        }

        public Builder setAvn(short s) {
            this.mHaveAvn = true;
            this.mAvn = s;
            return this;
        }

        public Builder setPcvc3Track1(byte[] bArr) {
            Preconditions.checkArgument(bArr.length == 6, "Unexpected PCVC3(track1) length: %s", bArr.length);
            this.mPcvc3Track1 = bArr;
            return this;
        }

        public Builder setPunatcTrack1(byte[] bArr) {
            Preconditions.checkArgument(bArr.length == 6, "Unexpected PUNATC(track1) length: %s", bArr.length);
            this.mPunatcTrack1 = bArr;
            return this;
        }

        public Builder setTrack1Data(byte[] bArr) {
            Preconditions.checkArgument(bArr.length <= 76, "Unexpected track1 length: %s", bArr.length);
            this.mTrack1Data = bArr;
            return this;
        }

        public Builder setNatcTrack1(byte b) {
            this.mHaveNatcTrack1 = true;
            this.mNatcTrack1 = b;
            return this;
        }

        public Builder setPcvc3Track2(byte[] bArr) {
            Preconditions.checkArgument(bArr.length == 2, "Unexpected PCVC3(track2) length: %s", bArr.length);
            this.mPcvc3Track2 = bArr;
            return this;
        }

        public Builder setPunatcTrack2(byte[] bArr) {
            Preconditions.checkArgument(bArr.length == 2, "Unexpected PUNATC(track2) length: %s", bArr.length);
            this.mPunatcTrack2 = bArr;
            return this;
        }

        public Builder setTrack2Data(byte[] bArr) {
            Preconditions.checkArgument(bArr.length <= 19, "Unexpected track2 length: %s", bArr.length);
            this.mTrack2Data = bArr;
            return this;
        }

        public Builder setNatcTrack2(byte b) {
            this.mHaveNatcTrack2 = true;
            this.mNatcTrack2 = b;
            return this;
        }

        public Builder setUdol(byte[] bArr) {
            Preconditions.checkArgument(bArr.length <= 252, "Unexpected UDOL length: %s", bArr.length);
            this.mUdol = bArr;
            return this;
        }

        public Builder setPayPassCrypto(PayPassCrypto payPassCrypto) {
            this.mPayPassCrypto = payPassCrypto;
            return this;
        }

        public Builder fillWithDefaults() {
            setApplicationPriorityIndicator((byte) 1);
            setIssuerCodeTableIndex((byte) 1);
            setAip(Hex.decode("0000"));
            setAfl(Hex.decode("08010100"));
            setAvn((short) 1);
            setUdol(PayPassConstants.EXPECTED_UDOL);
            return this;
        }
    }

    private PayPassCredential(byte b, byte b2, byte[] bArr, byte[] bArr2, short s, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte b3, byte[] bArr6, byte[] bArr7, byte[] bArr8, byte b4, byte[] bArr9, PayPassCrypto payPassCrypto, int i, int i2) {
        this.mApplicationPriorityIndicator = b;
        this.mIssuerCodeTableIndex = b2;
        this.mAip = bArr;
        this.mAfl = bArr2;
        this.mAvn = s;
        this.mPcvc3Track1 = bArr3;
        this.mPunatcTrack1 = bArr4;
        this.mTrack1Data = bArr5;
        this.mNatcTrack1 = b3;
        this.mPcvc3Track2 = bArr6;
        this.mPunatcTrack2 = bArr7;
        this.mTrack2Data = bArr8;
        this.mNatcTrack2 = b4;
        this.mUdol = bArr9;
        this.mPayPassCrypto = payPassCrypto;
        this.mSecretAtc = i;
        this.mReaderAtc = i2;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public byte getApplicationPriorityIndicator() {
        return this.mApplicationPriorityIndicator;
    }

    public byte getIssuerCodeTableIndex() {
        return this.mIssuerCodeTableIndex;
    }

    public byte[] getAip() {
        return this.mAip;
    }

    public byte[] getAfl() {
        return this.mAfl;
    }

    public short getAvn() {
        return this.mAvn;
    }

    public byte[] getPcvc3Track1() {
        return this.mPcvc3Track1;
    }

    public byte[] getPunatcTrack1() {
        return this.mPunatcTrack1;
    }

    public byte[] getTrack1Data() {
        return this.mTrack1Data;
    }

    public byte getNatcTrack1() {
        return this.mNatcTrack1;
    }

    public byte[] getPcvc3Track2() {
        return this.mPcvc3Track2;
    }

    public byte[] getPunatcTrack2() {
        return this.mPunatcTrack2;
    }

    public byte[] getTrack2Data() {
        return this.mTrack2Data;
    }

    public byte getNatcTrack2() {
        return this.mNatcTrack2;
    }

    public byte[] getUdol() {
        return this.mUdol;
    }

    public PayPassCrypto getPayPassCrypto() {
        return this.mPayPassCrypto;
    }

    public int getSecretAtc() {
        return this.mSecretAtc;
    }

    public int getReaderAtc() {
        return this.mReaderAtc;
    }

    private ToStringHelper allNonSensitiveFields() {
        return MoreObjects.toStringHelper(this).add("mApplicationPriorityIndicator", this.mApplicationPriorityIndicator).add("mIssuerCodeTableIndex", this.mIssuerCodeTableIndex).add("mAip", Hex.encode(this.mAip)).add("mAfl", Hex.encode(this.mAfl)).add("mAvn", this.mAvn).add("mPcvc3Track1", Hex.encode(this.mPcvc3Track1)).add("mPunatcTrack1", Hex.encode(this.mPunatcTrack1)).add("mNatcTrack1", this.mNatcTrack1).add("mPcvc3Track2", Hex.encode(this.mPcvc3Track2)).add("mPunatcTrack2", Hex.encode(this.mPunatcTrack2)).add("mNatcTrack2", this.mNatcTrack2).add("mUdol", Hex.encode(this.mUdol)).add("mReaderAtc", this.mReaderAtc);
    }

    public String toString() {
        return allNonSensitiveFields().add("mTrack1Data", Hex.encode(this.mTrack1Data)).add("mTrack2Data", Hex.encode(this.mTrack2Data)).add("mKdcvc3", Hex.encode(this.mPayPassCrypto.getKdCvc3())).add("mIvcvc3_track1", Hex.encode(this.mPayPassCrypto.getIvCvc3Track1())).add("mIvcvc3_track2", Hex.encode(this.mPayPassCrypto.getIvCvc3Track2())).toString();
    }

    public int hashCode() {
        return Objects.hashCode(Byte.valueOf(this.mApplicationPriorityIndicator), Byte.valueOf(this.mIssuerCodeTableIndex), Integer.valueOf(Arrays.hashCode(this.mAip)), Integer.valueOf(Arrays.hashCode(this.mAfl)), Short.valueOf(this.mAvn), Integer.valueOf(Arrays.hashCode(this.mPcvc3Track1)), Integer.valueOf(Arrays.hashCode(this.mPunatcTrack1)), Integer.valueOf(Arrays.hashCode(this.mTrack1Data)), Byte.valueOf(this.mNatcTrack1), Integer.valueOf(Arrays.hashCode(this.mPcvc3Track2)), Integer.valueOf(Arrays.hashCode(this.mPunatcTrack2)), Integer.valueOf(Arrays.hashCode(this.mTrack2Data)), Byte.valueOf(this.mNatcTrack2), Integer.valueOf(Arrays.hashCode(this.mUdol)), this.mPayPassCrypto, Integer.valueOf(this.mSecretAtc), Integer.valueOf(this.mReaderAtc));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PayPassCredential)) {
            return false;
        }
        PayPassCredential payPassCredential = (PayPassCredential) obj;
        if (this.mApplicationPriorityIndicator == payPassCredential.mApplicationPriorityIndicator && this.mIssuerCodeTableIndex == payPassCredential.mIssuerCodeTableIndex && Arrays.equals(this.mAip, payPassCredential.mAip) && Arrays.equals(this.mAfl, payPassCredential.mAfl) && this.mAvn == payPassCredential.mAvn && Arrays.equals(this.mPcvc3Track1, payPassCredential.mPcvc3Track1) && Arrays.equals(this.mPunatcTrack1, payPassCredential.mPunatcTrack1) && Arrays.equals(this.mTrack1Data, payPassCredential.mTrack1Data) && this.mNatcTrack1 == payPassCredential.mNatcTrack1 && Arrays.equals(this.mPcvc3Track2, payPassCredential.mPcvc3Track2) && Arrays.equals(this.mPunatcTrack2, payPassCredential.mPunatcTrack2) && Arrays.equals(this.mTrack2Data, payPassCredential.mTrack2Data) && this.mNatcTrack2 == payPassCredential.mNatcTrack2 && Arrays.equals(this.mUdol, payPassCredential.mUdol) && Objects.equal(this.mPayPassCrypto, payPassCredential.mPayPassCrypto) && this.mSecretAtc == payPassCredential.mSecretAtc && this.mReaderAtc == payPassCredential.mReaderAtc) {
            return true;
        }
        return false;
    }
}
