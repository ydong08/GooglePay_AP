package com.google.internal.tapandpay.v1.valuables.nano;

import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;

public interface CommonProto {

    public static final class MerchantAuthenticationKey extends MessageNano {
        private static volatile MerchantAuthenticationKey[] _emptyArray;
        public byte[] compressedPublicKey;
        public int id;

        public static MerchantAuthenticationKey[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new MerchantAuthenticationKey[0];
                    }
                }
            }
            return _emptyArray;
        }

        public MerchantAuthenticationKey() {
            clear();
        }

        public MerchantAuthenticationKey clear() {
            this.compressedPublicKey = WireFormatNano.EMPTY_BYTES;
            this.id = 0;
            this.cachedSize = -1;
            return this;
        }
    }

    public static final class SmartTap extends MessageNano {
        public String asciiCvc;
        public String asciiPin;
        public String asciiValue;
        public boolean authenticationRequired;
        public String bcdStringCvc;
        public String bcdStringPin;
        public String bcdStringValue;
        public byte[] binaryCvc;
        public byte[] binaryPin;
        public String binaryStringCvc;
        public String binaryStringPin;
        public String binaryStringValue;
        public byte[] binaryValue;
        public boolean encryptionRequired;
        public int expirationMonth;
        public int expirationYear;
        public MerchantAuthenticationKey[] merchantAuthenticationKey;
        public long programId;
        public long smarttapMerchantId;
        public int type;
        public boolean unlockRequired;

        public SmartTap() {
            clear();
        }

        public SmartTap clear() {
            this.smarttapMerchantId = 0;
            this.type = 0;
            this.programId = 0;
            this.asciiValue = "";
            this.bcdStringValue = "";
            this.binaryStringValue = "";
            this.binaryValue = WireFormatNano.EMPTY_BYTES;
            this.asciiPin = "";
            this.bcdStringPin = "";
            this.binaryStringPin = "";
            this.binaryPin = WireFormatNano.EMPTY_BYTES;
            this.asciiCvc = "";
            this.bcdStringCvc = "";
            this.binaryStringCvc = "";
            this.binaryCvc = WireFormatNano.EMPTY_BYTES;
            this.expirationMonth = 0;
            this.expirationYear = 0;
            this.encryptionRequired = false;
            this.authenticationRequired = false;
            this.merchantAuthenticationKey = MerchantAuthenticationKey.emptyArray();
            this.unlockRequired = false;
            this.cachedSize = -1;
            return this;
        }
    }
}
