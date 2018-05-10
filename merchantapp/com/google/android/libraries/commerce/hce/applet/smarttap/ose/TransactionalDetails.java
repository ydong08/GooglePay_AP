package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

public abstract class TransactionalDetails {
    public static final TransactionalDetails DEFAULT = builder().build();

    public static abstract class Builder {
        private Byte authenticationBitmap;
        private Byte customerPreferenceBitmap;
        private Byte encryptionBitmap;
        private Byte transactionModeBitmap;

        public Builder setGenericKeyAuth(boolean z) {
            int authenticationBitmap;
            if (z) {
                authenticationBitmap = authenticationBitmap() | -128;
            } else {
                authenticationBitmap = authenticationBitmap() & 127;
            }
            setAuthenticationBitmap((byte) authenticationBitmap);
            return this;
        }

        public Builder setEcies(boolean z) {
            int encryptionBitmap;
            if (z) {
                encryptionBitmap = encryptionBitmap() | -128;
            } else {
                encryptionBitmap = encryptionBitmap() & 127;
            }
            setEncryptionBitmap((byte) encryptionBitmap);
            return this;
        }

        public Builder setPaymentEnabled(boolean z) {
            int transactionModeBitmap;
            if (z) {
                transactionModeBitmap = transactionModeBitmap() | -128;
            } else {
                transactionModeBitmap = transactionModeBitmap() & 127;
            }
            setTransactionModeBitmap((byte) transactionModeBitmap);
            return this;
        }

        public Builder setPaymentRequested(boolean z) {
            int transactionModeBitmap;
            if (z) {
                transactionModeBitmap = transactionModeBitmap() | 64;
            } else {
                transactionModeBitmap = transactionModeBitmap() & -65;
            }
            setTransactionModeBitmap((byte) transactionModeBitmap);
            return this;
        }

        public Builder setVasEnabled(boolean z) {
            int transactionModeBitmap;
            if (z) {
                transactionModeBitmap = transactionModeBitmap() | 8;
            } else {
                transactionModeBitmap = transactionModeBitmap() & -9;
            }
            setTransactionModeBitmap((byte) transactionModeBitmap);
            return this;
        }

        public Builder setVasRequested(boolean z) {
            int transactionModeBitmap;
            if (z) {
                transactionModeBitmap = transactionModeBitmap() | 4;
            } else {
                transactionModeBitmap = transactionModeBitmap() & -5;
            }
            setTransactionModeBitmap((byte) transactionModeBitmap);
            return this;
        }

        Builder(byte b) {
            this();
        }

        public Builder setAuthenticationBitmap(byte b) {
            this.authenticationBitmap = Byte.valueOf(b);
            return this;
        }

        public byte authenticationBitmap() {
            if (this.authenticationBitmap != null) {
                return this.authenticationBitmap.byteValue();
            }
            throw new IllegalStateException("Property \"authenticationBitmap\" has not been set");
        }

        public Builder setEncryptionBitmap(byte b) {
            this.encryptionBitmap = Byte.valueOf(b);
            return this;
        }

        public byte encryptionBitmap() {
            if (this.encryptionBitmap != null) {
                return this.encryptionBitmap.byteValue();
            }
            throw new IllegalStateException("Property \"encryptionBitmap\" has not been set");
        }

        public Builder setCustomerPreferenceBitmap(byte b) {
            this.customerPreferenceBitmap = Byte.valueOf(b);
            return this;
        }

        public Builder setTransactionModeBitmap(byte b) {
            this.transactionModeBitmap = Byte.valueOf(b);
            return this;
        }

        public byte transactionModeBitmap() {
            if (this.transactionModeBitmap != null) {
                return this.transactionModeBitmap.byteValue();
            }
            throw new IllegalStateException("Property \"transactionModeBitmap\" has not been set");
        }

        public TransactionalDetails build() {
            String str = "";
            if (this.authenticationBitmap == null) {
                str = String.valueOf(str).concat(" authenticationBitmap");
            }
            if (this.encryptionBitmap == null) {
                str = String.valueOf(str).concat(" encryptionBitmap");
            }
            if (this.customerPreferenceBitmap == null) {
                str = String.valueOf(str).concat(" customerPreferenceBitmap");
            }
            if (this.transactionModeBitmap == null) {
                str = String.valueOf(str).concat(" transactionModeBitmap");
            }
            if (str.isEmpty()) {
                return new AutoValue_TransactionalDetails(this.authenticationBitmap.byteValue(), this.encryptionBitmap.byteValue(), this.customerPreferenceBitmap.byteValue(), this.transactionModeBitmap.byteValue());
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }
    }

    protected abstract byte authenticationBitmap();

    protected abstract byte customerPreferenceBitmap();

    protected abstract byte encryptionBitmap();

    protected abstract byte transactionModeBitmap();

    public static Builder builder() {
        return new Builder((byte) 0).setAuthenticationBitmap(Byte.MIN_VALUE).setEncryptionBitmap(Byte.MIN_VALUE).setCustomerPreferenceBitmap((byte) 0).setTransactionModeBitmap((byte) -52);
    }

    public static TransactionalDetails fromBytes(byte[] bArr) {
        if (bArr != null && bArr.length == 8) {
            return new Builder((byte) 0).setAuthenticationBitmap(bArr[7]).setEncryptionBitmap(bArr[6]).setCustomerPreferenceBitmap(bArr[5]).setTransactionModeBitmap(bArr[0]).build();
        }
        String str = "Transactional Details bytes must be %s bytes long. Found %s.";
        Object[] objArr = new Object[2];
        objArr[0] = Integer.valueOf(8);
        objArr[1] = bArr == null ? "null" : Integer.valueOf(bArr.length);
        throw new IllegalArgumentException(String.format(str, objArr));
    }

    public byte[] toBytes() {
        return new byte[]{transactionModeBitmap(), (byte) 0, (byte) 0, (byte) 0, (byte) 0, customerPreferenceBitmap(), encryptionBitmap(), authenticationBitmap()};
    }

    public boolean genericKeyAuth() {
        return (authenticationBitmap() & -128) != 0;
    }

    public boolean ecies() {
        return (encryptionBitmap() & -128) != 0;
    }

    public boolean paymentEnabled() {
        return (transactionModeBitmap() & -128) != 0;
    }

    public boolean paymentRequested() {
        return (transactionModeBitmap() & 64) != 0;
    }

    public boolean vasEnabled() {
        return (transactionModeBitmap() & 8) != 0;
    }

    public boolean vasRequested() {
        return (transactionModeBitmap() & 4) != 0;
    }
}
