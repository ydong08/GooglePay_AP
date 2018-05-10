package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

final class AutoValue_TransactionalDetails extends TransactionalDetails {
    private final byte authenticationBitmap;
    private final byte customerPreferenceBitmap;
    private final byte encryptionBitmap;
    private final byte transactionModeBitmap;

    private AutoValue_TransactionalDetails(byte b, byte b2, byte b3, byte b4) {
        this.authenticationBitmap = b;
        this.encryptionBitmap = b2;
        this.customerPreferenceBitmap = b3;
        this.transactionModeBitmap = b4;
    }

    protected byte authenticationBitmap() {
        return this.authenticationBitmap;
    }

    protected byte encryptionBitmap() {
        return this.encryptionBitmap;
    }

    protected byte customerPreferenceBitmap() {
        return this.customerPreferenceBitmap;
    }

    protected byte transactionModeBitmap() {
        return this.transactionModeBitmap;
    }

    public String toString() {
        byte b = this.authenticationBitmap;
        byte b2 = this.encryptionBitmap;
        byte b3 = this.customerPreferenceBitmap;
        return "TransactionalDetails{authenticationBitmap=" + b + ", encryptionBitmap=" + b2 + ", customerPreferenceBitmap=" + b3 + ", transactionModeBitmap=" + this.transactionModeBitmap + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TransactionalDetails)) {
            return false;
        }
        TransactionalDetails transactionalDetails = (TransactionalDetails) obj;
        if (this.authenticationBitmap == transactionalDetails.authenticationBitmap() && this.encryptionBitmap == transactionalDetails.encryptionBitmap() && this.customerPreferenceBitmap == transactionalDetails.customerPreferenceBitmap() && this.transactionModeBitmap == transactionalDetails.transactionModeBitmap()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((((this.authenticationBitmap ^ 1000003) * 1000003) ^ this.encryptionBitmap) * 1000003) ^ this.customerPreferenceBitmap) * 1000003) ^ this.transactionModeBitmap;
    }
}
