package com.google.commerce.tapandpay.merchantapp.paypass;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PayPassCrypto {
    private static final byte[] DEFAULT_IV = new byte[8];
    private final Cipher mDesEdeCipher;
    private final SecretKey mDesEdeKdCvc3;
    private final byte[] mIvCvc3Track1;
    private final byte[] mIvCvc3Track2;
    private final byte[] mKdCvc3;
    private final byte[] mPinIvCvc3Track1;
    private final byte[] mPinIvCvc3Track2;

    private PayPassCrypto(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, SecretKey secretKey, Cipher cipher) {
        this.mKdCvc3 = bArr;
        this.mIvCvc3Track1 = bArr2;
        this.mIvCvc3Track2 = bArr3;
        this.mPinIvCvc3Track1 = bArr4;
        this.mPinIvCvc3Track2 = bArr5;
        this.mDesEdeKdCvc3 = secretKey;
        this.mDesEdeCipher = cipher;
    }

    public static PayPassCrypto makeWithIvs(byte[] bArr, byte[] bArr2, byte[] bArr3) throws GeneralSecurityException {
        boolean z;
        Preconditions.checkArgument(bArr.length == 16);
        if (bArr2.length == 2) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        if (bArr3.length == 2) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        byte[] copyOf = Arrays.copyOf(bArr, 8);
        return new PayPassCrypto(bArr, bArr2, bArr3, bArr2, bArr3, new SecretKeySpec(CryptoUtils.adjustParity(Bytes.concat(bArr, copyOf)), "DESede"), Cipher.getInstance("DESede/ECB/NoPadding"));
    }

    public byte[] getKdCvc3() {
        return this.mKdCvc3;
    }

    public byte[] getIvCvc3Track1() {
        return this.mIvCvc3Track1;
    }

    public byte[] getIvCvc3Track2() {
        return this.mIvCvc3Track2;
    }

    public byte[] getCvc3Track1(byte[] bArr, int i) throws GeneralSecurityException {
        return getCvc3(bArr, i, this.mIvCvc3Track1);
    }

    public byte[] getCvc3Track2(byte[] bArr, int i) throws GeneralSecurityException {
        return getCvc3(bArr, i, this.mIvCvc3Track2);
    }

    public byte[] getPinCvc3Track1(byte[] bArr, int i) throws GeneralSecurityException {
        return getCvc3(bArr, i, this.mPinIvCvc3Track1);
    }

    public byte[] getPinCvc3Track2(byte[] bArr, int i) throws GeneralSecurityException {
        return getCvc3(bArr, i, this.mPinIvCvc3Track2);
    }

    private byte[] getCvc3(byte[] bArr, int i, byte[] bArr2) throws GeneralSecurityException {
        byte[] des3Encrypt = des3Encrypt(this.mDesEdeKdCvc3, ByteBuffer.allocate(8).put(bArr2).put(bArr).putShort((short) i).array());
        return Arrays.copyOfRange(des3Encrypt, des3Encrypt.length - 2, des3Encrypt.length);
    }

    private byte[] des3Encrypt(SecretKey secretKey, byte[] bArr) throws GeneralSecurityException {
        Preconditions.checkArgument(bArr.length == 8);
        this.mDesEdeCipher.init(1, secretKey);
        return this.mDesEdeCipher.doFinal(bArr);
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(Arrays.hashCode(this.mKdCvc3)), Integer.valueOf(Arrays.hashCode(this.mIvCvc3Track1)), Integer.valueOf(Arrays.hashCode(this.mIvCvc3Track2)));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PayPassCrypto)) {
            return false;
        }
        PayPassCrypto payPassCrypto = (PayPassCrypto) obj;
        if (Arrays.equals(this.mKdCvc3, payPassCrypto.mKdCvc3) && Arrays.equals(this.mIvCvc3Track1, payPassCrypto.mIvCvc3Track1) && Arrays.equals(this.mIvCvc3Track2, payPassCrypto.mIvCvc3Track2)) {
            return true;
        }
        return false;
    }
}
