package com.google.security.payments.tap2pay;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.Arrays;

public abstract class CryptoClient {
    public static final ECParameterSpec EC_SPEC = getNistP256Params();
    public static final int FIELD_SIZE_IN_BYTES = ((EC_SPEC.getCurve().getField().getFieldSize() + 7) / 8);

    public static KeyPair generateKeyPair() throws GeneralSecurityException {
        KeyPairGenerator instance = KeyPairGenerator.getInstance("EC", "AndroidOpenSSL");
        instance.initialize(new ECGenParameterSpec("prime256v1"), new SecureRandom());
        return instance.genKeyPair();
    }

    private static ECParameterSpec getNistP256Params() {
        BigInteger bigInteger = new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");
        return new ECParameterSpec(new EllipticCurve(new ECFieldFp(bigInteger), bigInteger.subtract(new BigInteger("3")), new BigInteger("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16)), new ECPoint(new BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16), new BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16)), new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), 1);
    }

    public static byte[] compressPoint(ECPoint eCPoint, EllipticCurve ellipticCurve) throws GeneralSecurityException {
        int fieldSizeInBytes = fieldSizeInBytes(ellipticCurve);
        Object obj = new byte[(fieldSizeInBytes + 1)];
        Object toByteArray = eCPoint.getAffineX().toByteArray();
        System.arraycopy(toByteArray, 0, obj, (fieldSizeInBytes + 1) - toByteArray.length, toByteArray.length);
        obj[0] = (byte) (eCPoint.getAffineY().testBit(0) ? 3 : 2);
        return obj;
    }

    public static ECPoint decompressPoint(byte[] bArr, ECParameterSpec eCParameterSpec) throws GeneralSecurityException {
        EllipticCurve curve = eCParameterSpec.getCurve();
        BigInteger modulus = getModulus(curve);
        if (bArr.length != ((modulus.subtract(BigInteger.ONE).bitLength() + 7) / 8) + 1) {
            throw new GeneralSecurityException("compressed point has wrong length");
        }
        boolean z;
        switch (bArr[0]) {
            case (byte) 2:
                z = false;
                break;
            case (byte) 3:
                z = true;
                break;
            default:
                throw new GeneralSecurityException("Invalid format");
        }
        BigInteger bigInteger = new BigInteger(1, Arrays.copyOfRange(bArr, 1, bArr.length));
        if (bigInteger.signum() == -1 || bigInteger.compareTo(modulus) != -1) {
            throw new GeneralSecurityException("x is out of range");
        }
        BigInteger mod;
        BigInteger modSqrt = modSqrt(bigInteger.multiply(bigInteger).add(curve.getA()).multiply(bigInteger).add(curve.getB()).mod(modulus), modulus);
        if (z != modSqrt.testBit(0)) {
            mod = modulus.subtract(modSqrt).mod(modulus);
        } else {
            mod = modSqrt;
        }
        return new ECPoint(bigInteger, mod);
    }

    private static BigInteger getModulus(EllipticCurve ellipticCurve) throws GeneralSecurityException {
        ECField field = ellipticCurve.getField();
        if (field instanceof ECFieldFp) {
            return ((ECFieldFp) field).getP();
        }
        throw new GeneralSecurityException("Only curves over prime order fields are supported");
    }

    private static BigInteger modSqrt(BigInteger bigInteger, BigInteger bigInteger2) throws GeneralSecurityException {
        int i = 0;
        if (bigInteger2.signum() != 1) {
            throw new InvalidAlgorithmParameterException("p must be positive");
        }
        BigInteger mod = bigInteger.mod(bigInteger2);
        BigInteger bigInteger3 = null;
        if (mod.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }
        if (bigInteger2.testBit(0) && bigInteger2.testBit(1)) {
            bigInteger3 = mod.modPow(bigInteger2.add(BigInteger.ONE).shiftRight(2), bigInteger2);
        } else if (bigInteger2.testBit(0) && !bigInteger2.testBit(1)) {
            BigInteger mod2;
            BigInteger modPow;
            bigInteger3 = BigInteger.ONE;
            BigInteger shiftRight = bigInteger2.subtract(BigInteger.ONE).shiftRight(1);
            while (true) {
                mod2 = bigInteger3.multiply(bigInteger3).subtract(mod).mod(bigInteger2);
                if (mod2.equals(BigInteger.ZERO)) {
                    return bigInteger3;
                }
                modPow = mod2.modPow(shiftRight, bigInteger2);
                if (modPow.add(BigInteger.ONE).equals(bigInteger2)) {
                    break;
                } else if (modPow.equals(BigInteger.ONE)) {
                    bigInteger3 = bigInteger3.add(BigInteger.ONE);
                    i++;
                    if (i == 128 && !bigInteger2.isProbablePrime(80)) {
                        throw new InvalidAlgorithmParameterException("p is not prime");
                    }
                } else {
                    throw new InvalidAlgorithmParameterException("p is not prime");
                }
            }
            BigInteger shiftRight2 = bigInteger2.add(BigInteger.ONE).shiftRight(1);
            shiftRight = BigInteger.ONE;
            int bitLength = shiftRight2.bitLength() - 2;
            BigInteger bigInteger4 = bigInteger3;
            while (bitLength >= 0) {
                BigInteger multiply = bigInteger4.multiply(shiftRight);
                modPow = bigInteger4.multiply(bigInteger4).add(shiftRight.multiply(shiftRight).mod(bigInteger2).multiply(mod2)).mod(bigInteger2);
                bigInteger4 = multiply.add(multiply).mod(bigInteger2);
                if (shiftRight2.testBit(bitLength)) {
                    shiftRight = modPow.multiply(bigInteger3).add(bigInteger4.multiply(mod2)).mod(bigInteger2);
                    bigInteger4 = bigInteger3.multiply(bigInteger4).add(modPow).mod(bigInteger2);
                } else {
                    shiftRight = modPow;
                }
                bitLength--;
                BigInteger bigInteger5 = bigInteger4;
                bigInteger4 = shiftRight;
                shiftRight = bigInteger5;
            }
            bigInteger3 = bigInteger4;
        }
        if (bigInteger3 == null || bigInteger3.multiply(bigInteger3).mod(bigInteger2).compareTo(mod) == 0) {
            return bigInteger3;
        }
        throw new GeneralSecurityException("Could not find a modular square root");
    }

    private static int fieldSizeInBits(EllipticCurve ellipticCurve) throws GeneralSecurityException {
        return getModulus(ellipticCurve).subtract(BigInteger.ONE).bitLength();
    }

    private static int fieldSizeInBytes(EllipticCurve ellipticCurve) throws GeneralSecurityException {
        return (fieldSizeInBits(ellipticCurve) + 7) / 8;
    }
}
